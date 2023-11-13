package tn.esprit.devops_project.RepositoryTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.OperatorRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OperatorRepositoryTest {


    @Autowired
    private OperatorRepository operatorRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    public void addOperatorWithInvoices() {
        // Given
        var operator = new Operator();
        operator.setFname("John");
        operator.setLname("Doe");
        operator.setPassword("password");

        Set<Invoice> invoices = new HashSet<>();
        Invoice invoice1 = Invoice.builder()
                .amountDiscount(10.0f)
                .amountInvoice(100.0f)
                .build();
        Invoice invoice2 = Invoice.builder()
                .amountDiscount(15.0f)
                .amountInvoice(150.0f)
                .build();

        invoice1 = invoiceRepository.save(invoice1);
        invoice2 = invoiceRepository.save(invoice2);
        invoices.add(invoice1);
        invoices.add(invoice2);

        operator.setInvoices(invoices);

        // When
        Operator savedOperator = operatorRepository.save(operator);

        // Then
        Assertions.assertThat(savedOperator).isNotNull();
        Assertions.assertThat(savedOperator.getIdOperateur()).isGreaterThan(0);
        Assertions.assertThat(savedOperator.getInvoices()).isNotNull();
        Assertions.assertThat(savedOperator.getInvoices().size()).isEqualTo(2);
    }
    @Test
    public void findOperatorById() {
        // Given
        Operator operator = new Operator();
        operator.setFname("Jane");
        operator.setLname("Doe");
        operator.setPassword("newpassword");

        Operator savedOperator = operatorRepository.save(operator);

        // When
        Operator foundOperator = operatorRepository.findById(savedOperator.getIdOperateur()).orElse(null);

        // Then
        Assertions.assertThat(foundOperator).isNotNull();
        Assertions.assertThat(foundOperator.getIdOperateur()).isEqualTo(savedOperator.getIdOperateur());
    }
}
