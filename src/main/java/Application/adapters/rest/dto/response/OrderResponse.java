package Application.adapters.rest.dto.response;

import Application.domain.enums.OrderStatus;
import Application.domain.model.Order;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderResponse {

    private final Long id;
    private final Long buyerId;
    private final List<OrderItemResponse> items;
    private final OrderStatus status;
    private final AddressResponse deliveryAddress;
    private final double total;

    private OrderResponse(Long id, Long buyerId, List<OrderItemResponse> items, OrderStatus status,
                           AddressResponse deliveryAddress, double total) {
        this.id = id;
        this.buyerId = buyerId;
        this.items = items;
        this.status = status;
        this.deliveryAddress = deliveryAddress;
        this.total = total;
    }

    public static OrderResponse from(Order order) {
        List<OrderItemResponse> items = order.getItems().stream().map(OrderItemResponse::from).toList();
        return new OrderResponse(order.getId(), order.getBuyer().getId(), items, order.getStatus(),
                AddressResponse.from(order.getDeliveryAddress()), order.getTotal());
    }
}
