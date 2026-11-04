package Application.adapters.rest;

import Application.adapters.rest.dto.request.AddVariantRequest;
import Application.adapters.rest.dto.request.ChangeProductStatusRequest;
import Application.adapters.rest.dto.request.PublishProductRequest;
import Application.adapters.rest.dto.response.ProductResponse;
import Application.adapters.rest.dto.response.ProductVariantResponse;
import Application.domain.model.Product;
import Application.domain.model.ProductVariant;
import Application.domain.ports.in.PublicAccessPort;
import Application.domain.ports.in.SellerPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// GET /api/products es publico (catalogo), el resto es del vendedor
// dueño del producto (ver Api-rest-endpoints.md)
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final PublicAccessPort publicAccessPort;
    private final SellerPort sellerPort;

    public ProductController(PublicAccessPort publicAccessPort, SellerPort sellerPort) {
        this.publicAccessPort = publicAccessPort;
        this.sellerPort = sellerPort;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listPublished() {
        List<ProductResponse> products = publicAccessPort.listPublishedProducts().stream()
                .map(ProductResponse::from).toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ProductResponse>> viewMine(@AuthenticationPrincipal Long sellerId) {
        List<ProductResponse> products = sellerPort.viewMyProducts(sellerId).stream()
                .map(ProductResponse::from).toList();
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> publish(@AuthenticationPrincipal Long sellerId,
                                                     @Valid @RequestBody PublishProductRequest request) {
        Product product = sellerPort.publishProduct(sellerId, request.getName(), request.getDescription(),
                request.getType(), request.getPrice(), request.getWarehouseId(), request.getInitialStock());
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.from(product));
    }

    @PostMapping("/{id}/variants")
    public ResponseEntity<ProductVariantResponse> addVariant(@AuthenticationPrincipal Long sellerId,
                                                               @PathVariable Long id,
                                                               @Valid @RequestBody AddVariantRequest request) {
        ProductVariant variant = sellerPort.addVariant(sellerId, id, request.getColor(), request.getSize(),
                request.getModel());
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductVariantResponse.from(variant));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductResponse> changeStatus(@AuthenticationPrincipal Long sellerId, @PathVariable Long id,
                                                          @Valid @RequestBody ChangeProductStatusRequest request) {
        // ChangeProductStatusRequest ya valido que "status" sea SUSPENDED o
        // DISCONTINUED (son los unicos dos que expone SellerPort)
        Product product = "SUSPENDED".equals(request.getStatus())
                ? sellerPort.suspendProduct(sellerId, id)
                : sellerPort.discontinueProduct(sellerId, id);
        return ResponseEntity.ok(ProductResponse.from(product));
    }
}
