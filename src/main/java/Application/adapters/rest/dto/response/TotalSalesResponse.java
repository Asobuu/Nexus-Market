package Application.adapters.rest.dto.response;

import lombok.Getter;

// GET /api/reports/sales -> { "totalSales": ... } (ver Api-rest-endpoints.md)
@Getter
public class TotalSalesResponse {

    private final double totalSales;

    public TotalSalesResponse(double totalSales) {
        this.totalSales = totalSales;
    }
}
