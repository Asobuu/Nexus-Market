package Application.domain.validators;

import Application.domain.exceptions.InvalidWarehouseException;
import Application.domain.model.OrderItem;
import Application.domain.model.Warehouse;
import Application.domain.model.user.Seller;

import java.util.List;

// confirma que una bodega de origen sea valida para un grupo de items
// (los que le corresponden a un vendedor dentro de un pedido). sin este
// chequeo, un vendedor podria terminar despachando desde la bodega de
// otro vendedor.
//
// si la bodega es del marketplace (owner == null, ver Warehouse) la
// puede usar cualquier vendedor; si es privada de un vendedor, solo
// sirve para despachar productos de ese mismo vendedor
public class WarehouseOwnershipValidator {

    public void validate(List<OrderItem> items, Warehouse warehouse) {
        Seller owner = warehouse.getOwner();
        if (owner == null) {
            return;
        }
        for (OrderItem item : items) {
            Long productSellerId = item.getProduct().getSeller().getId();
            if (!productSellerId.equals(owner.getId())) {
                throw new InvalidWarehouseException(
                        "la bodega " + warehouse.getName() + " no pertenece al vendedor del producto "
                                + item.getProduct().getName());
            }
        }
    }
}
