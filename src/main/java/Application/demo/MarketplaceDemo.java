package Application.demo;

import Application.enums.ProductType;
import Application.model.Cart;
import Application.model.CartItem;
import Application.model.Inventory;
import Application.model.Invoice;
import Application.model.Order;
import Application.model.OrderItem;
import Application.model.Product;
import Application.model.Shipment;
import Application.model.Warehouse;
import Application.model.user.Address;
import Application.model.user.Buyer;
import Application.model.user.Seller;
import Application.repository.BuyerRepository;
import Application.repository.CartRepository;
import Application.repository.InventoryRepository;
import Application.repository.InvoiceRepository;
import Application.repository.OrderRepository;
import Application.repository.ProductRepository;
import Application.repository.SellerRepository;
import Application.repository.ShipmentRepository;
import Application.repository.WarehouseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

// esto corre solo una vez cuando arranca la app (CommandLineRunner),
// nada mas para probar que las clases quedan bien guardadas en MySQL.
// no reemplaza a NexusMApplication, solo se engancha con @Component
@Component
public class MarketplaceDemo implements CommandLineRunner {

    private final SellerRepository sellerRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final BuyerRepository buyerRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;
    private final ShipmentRepository shipmentRepository;

    public MarketplaceDemo(SellerRepository sellerRepository, WarehouseRepository warehouseRepository,
                            ProductRepository productRepository, InventoryRepository inventoryRepository,
                            BuyerRepository buyerRepository, CartRepository cartRepository,
                            OrderRepository orderRepository, InvoiceRepository invoiceRepository,
                            ShipmentRepository shipmentRepository) {
        this.sellerRepository = sellerRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.buyerRepository = buyerRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    public void run(String... args) {
        // vendedor + bodega
        Seller seller = new Seller("Andes Electronics", "sales@andeselectronics.com");
        seller = sellerRepository.save(seller);

        Address warehouseAddress = new Address("Cra 45 #10-20", "Medellin", "Colombia");
        Warehouse warehouse = new Warehouse("Main Warehouse", warehouseAddress, seller);
        warehouse = warehouseRepository.save(warehouse);

        // producto con stock
        Product product = new Product("Wireless Mouse", "Ergonomic mouse", ProductType.PHYSICAL, 25.0, seller);
        product = productRepository.save(product);

        Inventory inventory = new Inventory(product, warehouse, 50);
        inventory = inventoryRepository.save(inventory);

        // comprador arma el carrito
        Address buyerAddress = new Address("Calle 100 #15-30", "Bogota", "Colombia");
        Buyer buyer = new Buyer("Julian Perez", "julian@example.com", buyerAddress);
        buyer = buyerRepository.save(buyer);

        Cart cart = new Cart(buyer);
        cart.addItem(new CartItem(product, 2));
        cart = cartRepository.save(cart);
        System.out.println("Total del carrito: " + cart.getTotal());

        // pasa a pedido y se reserva el inventario
        OrderItem orderItem = new OrderItem(product, 2);
        Order order = new Order(buyer, List.of(orderItem), buyer.getAddress());
        inventory.reserve(2);
        inventoryRepository.save(inventory);
        order = orderRepository.save(order);

        // paga, se genera factura, se despacha y se entrega
        order.pay();
        order = orderRepository.save(order);

        Invoice invoice = new Invoice(order);
        invoice = invoiceRepository.save(invoice);

        Shipment shipment = new Shipment(order, warehouse);
        shipment.ship();
        order.ship();
        shipment.deliver();
        order.deliver();
        shipmentRepository.save(shipment);
        orderRepository.save(order);

        System.out.println("Estado del pedido: " + order.getStatus());
        System.out.println("Total de la factura: " + invoice.getTotalAmount());
        System.out.println("Stock que queda: " + inventoryRepository.findById(inventory.getId()).get().getQuantity());
    }
}
