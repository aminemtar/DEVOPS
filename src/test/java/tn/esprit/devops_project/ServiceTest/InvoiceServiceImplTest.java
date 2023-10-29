package tn.esprit.devops_project.ServiceTest;


import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.OperatorRepository;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.InvoiceServiceImpl;
import tn.esprit.devops_project.services.OperatorServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class InvoiceServiceImplTest {

    @Autowired
    private InvoiceServiceImpl invoiceService;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private OperatorServiceImpl operatorService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveInvoice() {
        Invoice invoice = new Invoice();
        invoice.setSupplier(null);
        invoice.setAmountInvoice(100);
        invoice.setInvoiceDetails(null);
        invoice.setDateCreationInvoice(new Date());
        invoice.setDateLastModificationInvoice(new Date());
        invoice.setArchived(false);
        invoice.setIdInvoice(null);
        invoice.setAmountDiscount(50);
        Invoice invoiceInserted = invoiceRepository.save(invoice);
        final Invoice invoiceRetrieved = this.invoiceService.retrieveInvoice(invoiceInserted.getIdInvoice());
        assertEquals(invoiceRetrieved.getAmountInvoice(), 100f);
    }



    @Test
    @DatabaseSetup({"/data-set/invoice-data.xml", "/data-set/supplier-data.xml"})
    void getInvoicesBySupplierNotFound() {
        assertThrows(NullPointerException.class, () -> {
            this.invoiceService.getInvoicesBySupplier(7L);
        });
    }



    @Test
    @DatabaseSetup({"/data-set/invoice-data.xml", "/data-set/operator-data.xml"})
    void assignOperatorToInvoiceNotFoundOperatorAndInvoice() {
        assertThrows(NullPointerException.class, () -> {
            this.invoiceService.assignOperatorToInvoice(7L, 1L);
        });
        assertThrows(NullPointerException.class, () -> {
            this.invoiceService.assignOperatorToInvoice(1L, 7L);
        });
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void getTotalAmountInvoiceBetweenDates() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Invoice invoice = new Invoice(5L, 20, 100, dateFormat.parse("2020-03-03"), dateFormat.parse("2020-03-03"), false, null, null);
        invoiceRepository.save(invoice);
        float amount = this.invoiceService.getTotalAmountInvoiceBetweenDates(dateFormat.parse("2019-08-26"), dateFormat.parse("2020-12-26"));
        assertEquals(amount, 200);
    }


}


