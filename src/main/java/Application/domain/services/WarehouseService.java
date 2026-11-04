package Application.domain.services;

import Application.domain.model.Warehouse;
import Application.domain.model.user.Address;
import Application.domain.model.user.Seller;
import Application.domain.ports.out.WarehouseRepositoryPort;

// registrar bodegas. se separo de AdministrationService porque esa
// clase es sobre gestionar USUARIOS (vendedores, operadores) y esta es
// sobre gestionar un RECURSO (bodegas) - son responsabilidades
// distintas, aunque las dos las dispare un administrador
public class WarehouseService {

    private final WarehouseRepositoryPort warehouseRepositoryPort;

    public WarehouseService(WarehouseRepositoryPort warehouseRepositoryPort) {
        this.warehouseRepositoryPort = warehouseRepositoryPort;
    }

    public Warehouse registerWarehouse(String name, Address location, Seller owner) {
        Warehouse warehouse = new Warehouse(name, location, owner);
        return warehouseRepositoryPort.save(warehouse);
    }
}
