package Application.adapters.useCases;

import Application.domain.enums.ProductStatus;
import Application.domain.model.Inventory;
import Application.domain.ports.in.SupervisorPort;
import Application.domain.services.ReportService;
import org.springframework.stereotype.Component;

import java.util.List;

// reportes de solo lectura para el supervisor. SupervisorPort no recibe
// ningun id de actor (ningun metodo esta "filtrado por supervisor", son
// numeros globales de la plataforma), asi que no hay nada que resolver
// ni validar aca: es pura delegacion directa a ReportService
@Component
public class SupervisorUseCaseAdapter implements SupervisorPort {

    private final ReportService reportService;

    public SupervisorUseCaseAdapter(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public double getTotalSales() {
        return reportService.getTotalSales();
    }

    @Override
    public long countPendingOrders() {
        return reportService.countPendingOrders();
    }

    @Override
    public long countProductsByStatus(ProductStatus status) {
        return reportService.countProductsByStatus(status);
    }

    @Override
    public List<Inventory> getLowStock(int threshold) {
        return reportService.getLowStock(threshold);
    }
}
