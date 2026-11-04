package Application.adapters.useCases;

import Application.domain.exceptions.EntityNotFoundException;
import Application.domain.exceptions.UnauthorizedActionException;
import Application.domain.model.Cart;
import Application.domain.model.CartItem;
import Application.domain.model.Inventory;
import Application.domain.model.Invoice;
import Application.domain.model.Order;
import Application.domain.model.Product;
import Application.domain.model.Return;
import Application.domain.model.user.Address;
import Application.domain.model.user.Buyer;
import Application.domain.ports.in.BuyerPort;
import Application.domain.ports.out.BuyerRepositoryPort;
import Application.domain.ports.out.CartRepositoryPort;
import Application.domain.ports.out.InventoryRepositoryPort;
import Application.domain.ports.out.OrderRepositoryPort;
import Application.domain.ports.out.ProductRepositoryPort;
import Application.domain.services.CartService;
import Application.domain.services.CheckoutService;
import Application.domain.services.OrderService;
import Application.domain.services.PaymentService;
import Application.domain.services.ReturnService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// todo lo que puede hacer un comprador autenticado. este adaptador no
// decide nada de negocio (eso vive en los servicios de dominio), solo
// resuelve los ids que llegan del puerto (buyerId/productId/orderId) a
// los objetos de dominio reales, valida que el comprador sea dueño de
// lo que esta tocando, y delega.
//
// el SDD solo lista CartService, CheckoutService, OrderService,
// ReturnService e InventoryRepositoryPort como dependencias de este
// adaptador, pero para poder resolver esos ids tambien necesita
// BuyerRepositoryPort, ProductRepositoryPort, CartRepositoryPort y
// OrderRepositoryPort (son puertos de lectura, no logica de negocio).
// tambien necesita PaymentService: BuyerPort.payOrder tiene que cobrar
// el pedido y eso vive en PaymentService, no en OrderService
// (ver el comentario de PaymentService sobre por que se separo de Order)
@Component
public class BuyerUseCaseAdapter implements BuyerPort {

    private final BuyerRepositoryPort buyerRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final CartRepositoryPort cartRepositoryPort;
    private final CartService cartService;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final CheckoutService checkoutService;
    private final OrderRepositoryPort orderRepositoryPort;
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final ReturnService returnService;

    public BuyerUseCaseAdapter(BuyerRepositoryPort buyerRepositoryPort, ProductRepositoryPort productRepositoryPort,
                                CartRepositoryPort cartRepositoryPort, CartService cartService,
                                InventoryRepositoryPort inventoryRepositoryPort, CheckoutService checkoutService,
                                OrderRepositoryPort orderRepositoryPort, PaymentService paymentService,
                                OrderService orderService, ReturnService returnService) {
        this.buyerRepositoryPort = buyerRepositoryPort;
        this.productRepositoryPort = productRepositoryPort;
        this.cartRepositoryPort = cartRepositoryPort;
        this.cartService = cartService;
        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.checkoutService = checkoutService;
        this.orderRepositoryPort = orderRepositoryPort;
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.returnService = returnService;
    }

    @Override
    public Cart addToCart(Long buyerId, Long productId, int quantity) {
        Buyer buyer = requireBuyer(buyerId);
        Product product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("producto", productId));
        Cart cart = cartRepositoryPort.findByBuyerId(buyerId).orElseGet(() -> new Cart(buyer));
        return cartService.addToCart(cart, product, quantity);
    }

    @Override
    public Cart viewCart(Long buyerId) {
        Buyer buyer = requireBuyer(buyerId);
        // si todavia no tiene carrito guardado (nunca agrego nada), se
        // devuelve uno vacio sin guardar en vez de fallar
        return cartRepositoryPort.findByBuyerId(buyerId).orElseGet(() -> new Cart(buyer));
    }

    @Override
    public Order checkout(Long buyerId, Address deliveryAddress) {
        requireBuyer(buyerId);
        Cart cart = cartRepositoryPort.findByBuyerId(buyerId)
                .orElseThrow(() -> new EntityNotFoundException("carrito del comprador", buyerId));
        Map<Long, Inventory> inventoryByProductId = resolveInventoryForCart(cart);
        return checkoutService.checkout(cart, inventoryByProductId, deliveryAddress);
    }

    @Override
    public Invoice payOrder(Long buyerId, Long orderId) {
        Order order = requireOwnedOrder(buyerId, orderId);
        Invoice invoice = paymentService.payOrder(order);
        // apenas se confirma el pago se arman los envios (uno por cada
        // bodega/vendedor involucrado en el pedido, ver OrderService)
        orderService.createShipments(order);
        return invoice;
    }

    @Override
    public List<Order> viewMyOrders(Long buyerId) {
        return orderRepositoryPort.findByBuyerId(buyerId);
    }

    @Override
    public Return requestReturn(Long buyerId, Long orderId, Long productId, String reason) {
        Order order = requireOwnedOrder(buyerId, orderId);
        Product product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("producto", productId));
        return returnService.requestReturn(order, product, reason);
    }

    // BuyerRepositoryPort solo devuelve algo si el id corresponde a un
    // Buyer (herencia de tabla unica: un id de Seller u otro rol no
    // aparece aca), asi que esto valida existencia e identidad de rol
    // a la vez
    private Buyer requireBuyer(Long buyerId) {
        return buyerRepositoryPort.findById(buyerId)
                .orElseThrow(() -> new UnauthorizedActionException("el comprador " + buyerId + " no existe"));
    }

    private Order requireOwnedOrder(Long buyerId, Long orderId) {
        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("pedido", orderId));
        if (!order.getBuyer().getId().equals(buyerId)) {
            throw new UnauthorizedActionException("el pedido " + orderId + " no pertenece al comprador " + buyerId);
        }
        return order;
    }

    // CheckoutService espera que YA se le diga de que Inventory (bodega)
    // sale cada producto fisico del carrito (ver el comentario de
    // CheckoutService.checkout: el inventario elegido aca queda grabado
    // en el OrderItem y de ahi sale el Shipment). cuando el mismo
    // producto tiene stock en varias bodegas, se elige la que tenga mas
    // cantidad disponible, para reducir el riesgo de quedarse sin stock;
    // si ninguna alcanza, InventoryService.reserveStock lo rechaza igual
    private Map<Long, Inventory> resolveInventoryForCart(Cart cart) {
        Map<Long, Inventory> inventoryByProductId = new HashMap<>();
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            if (!product.needsShipping()) {
                continue;
            }
            Inventory chosen = null;
            for (Inventory candidate : inventoryRepositoryPort.findByProductId(product.getId())) {
                if (chosen == null || candidate.getQuantity() > chosen.getQuantity()) {
                    chosen = candidate;
                }
            }
            if (chosen == null) {
                throw new EntityNotFoundException("inventario del producto", product.getId());
            }
            inventoryByProductId.put(product.getId(), chosen);
        }
        return inventoryByProductId;
    }
}
