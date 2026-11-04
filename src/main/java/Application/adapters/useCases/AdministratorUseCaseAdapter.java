package Application.adapters.useCases;

import Application.domain.exceptions.EntityNotFoundException;
import Application.domain.exceptions.UnauthorizedActionException;
import Application.domain.model.Inventory;
import Application.domain.model.OrderItem;
import Application.domain.model.Product;
import Application.domain.model.Return;
import Application.domain.model.Warehouse;
import Application.domain.model.user.Address;
import Application.domain.model.user.Administrator;
import Application.domain.model.user.LogisticsOperator;
import Application.domain.model.user.Seller;
import Application.domain.model.user.User;
import Application.domain.ports.in.AdministratorPort;
import Application.domain.ports.out.InventoryRepositoryPort;
import Application.domain.ports.out.ReturnRepositoryPort;
import Application.domain.ports.out.SellerRepositoryPort;
import Application.domain.ports.out.UserRepositoryPort;
import Application.domain.services.AdministrationService;
import Application.domain.services.ReturnService;
import Application.domain.services.WarehouseService;
import org.springframework.stereotype.Component;

// todo lo que puede hacer un administrador. el SDD solo lista
// AdministrationService y ReturnService, pero registerWarehouse en
// realidad vive en WarehouseService (ver el comentario de esa clase:
// gestionar bodegas es un recurso, no un usuario, y el profesor marco
// que son responsabilidades distintas aunque las dispare el mismo rol).
// tambien hace falta SellerRepositoryPort (para resolver el vendedor
// dueño de una bodega nueva) e InventoryRepositoryPort (para encontrar
// el inventario de origen al cerrar una devolucion).
//
// Administrator no tiene su propio repositorio de salida (no hay
// "listar todos los administradores" en ningun lado del dominio), asi
// que el actor se resuelve con el UserRepositoryPort generico y se
// confirma el rol con instanceof
@Component
public class AdministratorUseCaseAdapter implements AdministratorPort {

    private final UserRepositoryPort userRepositoryPort;
    private final SellerRepositoryPort sellerRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final ReturnRepositoryPort returnRepositoryPort;
    private final AdministrationService administrationService;
    private final WarehouseService warehouseService;
    private final ReturnService returnService;

    public AdministratorUseCaseAdapter(UserRepositoryPort userRepositoryPort, SellerRepositoryPort sellerRepositoryPort,
                                        InventoryRepositoryPort inventoryRepositoryPort,
                                        ReturnRepositoryPort returnRepositoryPort,
                                        AdministrationService administrationService,
                                        WarehouseService warehouseService, ReturnService returnService) {
        this.userRepositoryPort = userRepositoryPort;
        this.sellerRepositoryPort = sellerRepositoryPort;
        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.returnRepositoryPort = returnRepositoryPort;
        this.administrationService = administrationService;
        this.warehouseService = warehouseService;
        this.returnService = returnService;
    }

    @Override
    public Seller registerSeller(Long adminId, String fullName, String email, String rawPassword) {
        requireAdmin(adminId);
        return administrationService.registerSeller(fullName, email, rawPassword);
    }

    @Override
    public LogisticsOperator registerLogisticsOperator(Long adminId, String fullName, String email,
                                                         String rawPassword) {
        requireAdmin(adminId);
        return administrationService.registerLogisticsOperator(fullName, email, rawPassword);
    }

    @Override
    public Warehouse registerWarehouse(Long adminId, String name, Address location, Long ownerSellerId) {
        requireAdmin(adminId);
        // ownerSellerId null = bodega del marketplace, abierta a cualquier
        // vendedor (ver Warehouse: "si owner es null, la bodega es del marketplace")
        Seller owner = null;
        if (ownerSellerId != null) {
            owner = sellerRepositoryPort.findById(ownerSellerId)
                    .orElseThrow(() -> new EntityNotFoundException("vendedor", ownerSellerId));
        }
        return warehouseService.registerWarehouse(name, location, owner);
    }

    @Override
    public Return approveReturn(Long adminId, Long returnId, double refundAmount) {
        Administrator admin = requireAdmin(adminId);
        Return returnRequest = requireReturn(returnId);
        return returnService.approveReturn(returnRequest, refundAmount, admin.getEmail());
    }

    @Override
    public Return rejectReturn(Long adminId, Long returnId) {
        Administrator admin = requireAdmin(adminId);
        Return returnRequest = requireReturn(returnId);
        return returnService.rejectReturn(returnRequest, admin.getEmail());
    }

    @Override
    public Return completeRefund(Long adminId, Long returnId) {
        Administrator admin = requireAdmin(adminId);
        Return returnRequest = requireReturn(returnId);
        Inventory inventory = resolveReturnInventory(returnRequest);
        return returnService.completeRefund(returnRequest, admin.getEmail(), inventory);
    }

    private Administrator requireAdmin(Long adminId) {
        User user = userRepositoryPort.findById(adminId)
                .orElseThrow(() -> new UnauthorizedActionException("el administrador " + adminId + " no existe"));
        if (!(user instanceof Administrator administrator)) {
            throw new UnauthorizedActionException("el usuario " + adminId + " no tiene rol de administrador");
        }
        return administrator;
    }

    private Return requireReturn(Long returnId) {
        return returnRepositoryPort.findById(returnId)
                .orElseThrow(() -> new EntityNotFoundException("devolucion", returnId));
    }

    // encuentra el Inventory de la misma bodega de donde salio el
    // producto devuelto (ver OrderItem.warehouse), para que
    // ReturnService pueda reponer el stock en el lugar correcto.
    // ReturnService.completeRefund vuelve a validar esto por su cuenta
    // (ver ReturnService.validateInventoryMatchesOrigin), aca solo se
    // busca el candidato correcto para pasarle
    private Inventory resolveReturnInventory(Return returnRequest) {
        Product product = returnRequest.getProduct();
        if (!product.needsShipping()) {
            return null;
        }
        OrderItem purchasedItem = findPurchasedItem(returnRequest);
        Warehouse originWarehouse = purchasedItem.getWarehouse();
        if (originWarehouse == null) {
            return null;
        }
        for (Inventory candidate : inventoryRepositoryPort.findByProductId(product.getId())) {
            if (candidate.getWarehouse().getId().equals(originWarehouse.getId())) {
                return candidate;
            }
        }
        throw new EntityNotFoundException("inventario de origen de la devolucion", returnRequest.getId());
    }

    private OrderItem findPurchasedItem(Return returnRequest) {
        for (OrderItem item : returnRequest.getOrder().getItems()) {
            if (item.getProduct().getId().equals(returnRequest.getProduct().getId())) {
                return item;
            }
        }
        throw new EntityNotFoundException("item del pedido para la devolucion", returnRequest.getId());
    }
}
