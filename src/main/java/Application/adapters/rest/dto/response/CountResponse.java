package Application.adapters.rest.dto.response;

import lombok.Getter;

// GET /api/reports/orders/pending y /api/reports/products/status ->
// { "count": ... } (ver Api-rest-endpoints.md)
@Getter
public class CountResponse {

    private final long count;

    public CountResponse(long count) {
        this.count = count;
    }
}
