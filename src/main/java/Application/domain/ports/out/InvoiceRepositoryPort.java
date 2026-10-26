package Application.domain.ports.out;

import Application.domain.model.Invoice;

import java.util.List;

public interface InvoiceRepositoryPort {
    Invoice save(Invoice invoice);
    List<Invoice> findAll();
}
