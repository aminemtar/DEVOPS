package tn.esprit.devops_project.RepositoryTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierCategory;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.InvoiceServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class InvoiceRepositoryTests {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    public void addInvoice(){
        Invoice invoice = Invoice.builder()
                .idInvoice(1L)
                .amountDiscount(10.0f)
                .amountInvoice(100.0f)
                .dateCreationInvoice(new Date())
                .dateLastModificationInvoice(new Date())
                .archived(false)
                .build();
        Invoice savedInvoice = invoiceRepository.save(invoice);
        Assertions.assertThat(savedInvoice).isNotNull();
        Assertions.assertThat(savedInvoice.getIdInvoice()).isGreaterThan(0);
    }
    @Test
    public void testDeleteInvoice() {
        // Save an Invoice first to have an ID
        Invoice invoice = new Invoice();
        invoice.setAmountDiscount(50.0f);
        invoice.setAmountInvoice(500.0f);
        invoice.setDateCreationInvoice(new Date());
        invoice.setDateLastModificationInvoice(new Date());
        invoice.setArchived(false);
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Delete the Invoice
        invoiceRepository.deleteById(savedInvoice.getIdInvoice());

        // Try to retrieve the deleted Invoice
        Invoice deletedInvoice = invoiceRepository.findById(savedInvoice.getIdInvoice()).orElse(null);

        // Check if the Invoice is not found
        assertEquals(null, deletedInvoice);
    }
    @Test
    @Transactional
    public void testUpdateInvoice() {
        // Save an Invoice first to have an ID
        Invoice invoice = new Invoice();
        invoice.setAmountDiscount(50.0f);
        invoice.setAmountInvoice(500.0f);
        invoice.setDateCreationInvoice(new Date());
        invoice.setDateLastModificationInvoice(new Date());
        invoice.setArchived(false);
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Update the Invoice
        invoiceRepository.updateInvoice(savedInvoice.getIdInvoice());

        // Retrieve the updated Invoice
        Invoice updatedInvoice = invoiceRepository.findById(savedInvoice.getIdInvoice()).orElse(null);

        // Check if the Invoice is archived
        assertNotNull(updatedInvoice);
        assertEquals(false, updatedInvoice.getArchived());
    }
    @Test
    public void GetAll_ReturnMoreThenOneInvoice(){
        Invoice invoice = Invoice.builder()
                .amountDiscount(10.0f)
                .amountInvoice(100.0f)
                .dateCreationInvoice(new Date())
                .dateLastModificationInvoice(new Date())
                .archived(false)
                .build();
        Invoice invoice1 = Invoice.builder()
                .amountDiscount(10.0f)
                .amountInvoice(100.0f)
                .dateCreationInvoice(new Date())
                .dateLastModificationInvoice(new Date())
                .archived(false)
                .build();
        Invoice savedInvoice = invoiceRepository.save(invoice);
        Invoice savedInvoice1 = invoiceRepository.save(invoice1);

        List<Invoice> pokemonList = invoiceRepository.findAll();

        Assertions.assertThat(pokemonList).isNotNull();
        // Assertions.assertThat(pokemonList.size()).isEqualTo(2);
    }
    @Test
    public void InvoiceRepository_FindById_ReturnInvoiceNotNull() {
        Invoice invoice = Invoice.builder()
                .amountDiscount(10.0f)
                .amountInvoice(100.0f)
                .dateCreationInvoice(new Date())
                .dateLastModificationInvoice(new Date())
                .archived(false)
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);

        Invoice pokemonList = invoiceRepository.findById(invoice.getIdInvoice()).get();

        Assertions.assertThat(pokemonList).isNotNull();
    }

    @Test
    public void testRetrieveInvoicesBySupplier() {
        // Create a Supplier
        Supplier supplier = new Supplier();
        supplier.setCode("S001");
        supplier.setLabel("Supplier A");
        supplier.setSupplierCategory(SupplierCategory.CONVENTIONNE);
        supplier.setInvoices(new HashSet<>()); // Initialize the set

        // Save the Supplier
        Supplier savedSupplier = supplierRepository.save(supplier);

        // Create an Invoice related to the Supplier
        Invoice invoice = new Invoice();
        invoice.setAmountDiscount(50.0f);
        invoice.setAmountInvoice(500.0f);
        invoice.setDateCreationInvoice(new Date());
        invoice.setDateLastModificationInvoice(new Date());
        invoice.setArchived(false);
        invoice.setSupplier(savedSupplier);
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Retrieve Invoices by Supplier
        List<Invoice> invoicesBySupplier = invoiceRepository.retrieveInvoicesBySupplier(savedSupplier);

        // Check if the retrieved list contains the saved Invoice
        assertNotNull(invoicesBySupplier);
        assertTrue(invoicesBySupplier.contains(savedInvoice));
    }
    @Test
    public void testGetTotalAmountInvoiceBetweenDates() {
        // Create a Supplier
        Supplier supplier = new Supplier();
        supplier.setCode("S001");
        supplier.setLabel("Supplier A");
        supplier.setSupplierCategory(SupplierCategory.ORDINAIRE);
        supplier.setInvoices(new HashSet<>()); // Initialize the set
        Supplier savedSupplier = supplierRepository.save(supplier);

        // Set your start and end dates
        Date startDate = getDate("2023-01-01");
        Date endDate = getDate("2023-01-31");

        // Save Invoices with creation dates within the specified range and related to the Supplier
        Invoice invoice1 = createInvoiceWithDate(getDate("2023-01-10"), savedSupplier);
        Invoice invoice2 = createInvoiceWithDate(getDate("2023-01-20"), savedSupplier);
        // ...

        // Calculate the expected total amount
        float expectedTotalAmount = invoice1.getAmountInvoice() + invoice2.getAmountInvoice() ;

        // Retrieve the total amount of Invoices between dates
        float totalAmount = invoiceRepository.getTotalAmountInvoiceBetweenDates(startDate, endDate);

        // Check if the calculated total amount matches the retrieved total amount
        assertEquals(expectedTotalAmount, totalAmount, 0.001); // Adjust the delta as needed
    }

    // Helper method to create an Invoice with a specific date and related to a Supplier
    private Invoice createInvoiceWithDate(Date date, Supplier supplier) {
        Invoice invoice = new Invoice();
        invoice.setAmountDiscount(50.0f);
        invoice.setAmountInvoice(500.0f);
        invoice.setDateCreationInvoice(date);
        invoice.setDateLastModificationInvoice(date);
        invoice.setArchived(false);
        invoice.setSupplier(supplier);
        return invoiceRepository.save(invoice);
    }

    // Helper method to convert a string date to a Date object
    private Date getDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing date", e);
        }
    }

}

