package tn.esprit.devops_project.RepositoryTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierCategory;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.SupplierRepository;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SupplierRepositoryTests {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;



    @Test
    public void addSupplierWithInvoices() {
        // Given
        Supplier supplier = new Supplier();
        supplier.setCode("S001");
        supplier.setLabel("ABC Electronics");
        supplier.setSupplierCategory(SupplierCategory.CONVENTIONNE);

        Set<Invoice> invoices = new HashSet<>();
        Invoice invoice1 = new Invoice();
        invoice1.setAmountDiscount(10.0f);
        invoice1.setAmountInvoice(100.0f);
        invoice1.setSupplier(supplier);

        Invoice invoice2 = new Invoice();
        invoice2.setAmountDiscount(15.0f);
        invoice2.setAmountInvoice(150.0f);
        invoice2.setSupplier(supplier);

        invoices.add(invoice1);
        invoices.add(invoice2);

        supplier.setInvoices(invoices);

        // When
        Supplier savedSupplier = supplierRepository.save(supplier);

        // Then
        Assertions.assertThat(savedSupplier).isNotNull();
        Assertions.assertThat(savedSupplier.getIdSupplier()).isGreaterThan(0);
        Assertions.assertThat(savedSupplier.getInvoices()).isNotNull();
        Assertions.assertThat(savedSupplier.getInvoices().size()).isEqualTo(2);
    }
    @Test
    public void findSupplierById() {
        // Given
        Supplier supplier = new Supplier();
        supplier.setCode("S002");
        supplier.setLabel("XYZ Hardware");
        supplier.setSupplierCategory(SupplierCategory.ORDINAIRE);

        Supplier savedSupplier = supplierRepository.save(supplier);

        // When
        Supplier foundSupplier = supplierRepository.findById(savedSupplier.getIdSupplier()).orElse(null);

        // Then
        Assertions.assertThat(foundSupplier).isNotNull();
        Assertions.assertThat(foundSupplier.getIdSupplier()).isEqualTo(savedSupplier.getIdSupplier());
    }
    @Test
    public void updateSupplierLabel() {
        // Given
        Supplier supplier = new Supplier();
        supplier.setCode("S003");
        supplier.setLabel("ABC Electronics");
        supplier.setSupplierCategory(SupplierCategory.CONVENTIONNE);

        Supplier savedSupplier = supplierRepository.save(supplier);

        // When
        savedSupplier.setLabel("Updated Electronics Supplier");
        Supplier updatedSupplier = supplierRepository.save(savedSupplier);

        // Then
        Assertions.assertThat(updatedSupplier).isNotNull();
        Assertions.assertThat(updatedSupplier.getLabel()).isEqualTo("Updated Electronics Supplier");
    }

    @Test
    public void deleteSupplierWithInvoices() {
        // Given
        Supplier supplier = new Supplier();
        supplier.setCode("S004");
        supplier.setLabel("XYZ Hardware");
        supplier.setSupplierCategory(SupplierCategory.ORDINAIRE);

        Set<Invoice> invoices = new HashSet<>();
        Invoice invoice1 = new Invoice();
        invoice1.setAmountDiscount(10.0f);
        invoice1.setAmountInvoice(100.0f);
        invoice1.setSupplier(supplier);

        Invoice invoice2 = new Invoice();
        invoice2.setAmountDiscount(15.0f);
        invoice2.setAmountInvoice(150.0f);
        invoice2.setSupplier(supplier);

        invoices.add(invoice1);
        invoices.add(invoice2);

        supplier.setInvoices(invoices);

        Supplier savedSupplier = supplierRepository.save(supplier);

        // When
        supplierRepository.deleteById(savedSupplier.getIdSupplier());
        Supplier deletedSupplier = supplierRepository.findById(savedSupplier.getIdSupplier()).orElse(null);

        // Then
        Assertions.assertThat(deletedSupplier).isNull();
    }

    @Test
    public void findSuppliersByCategory() {
        // Given
        Supplier supplier1 = new Supplier();
        supplier1.setCode("S005");
        supplier1.setLabel("Tech Gadgets");
        supplier1.setSupplierCategory(SupplierCategory.ORDINAIRE);

        Supplier supplier2 = new Supplier();
        supplier2.setCode("S006");
        supplier2.setLabel("Building Supplies");
        supplier2.setSupplierCategory(SupplierCategory.CONVENTIONNE);

        supplierRepository.save(supplier1);
        supplierRepository.save(supplier2);

        // When
        String categoryToFind = "CONVENTIONNE";
        Iterable<Supplier> foundSuppliers = supplierRepository.findBySupplierCategory(SupplierCategory.valueOf(categoryToFind));

        // Then
        Assertions.assertThat(foundSuppliers).isNotEmpty();
        Assertions.assertThat(foundSuppliers).allMatch(supplier -> supplier.getSupplierCategory().equals(SupplierCategory.valueOf(categoryToFind)));
    }
}
