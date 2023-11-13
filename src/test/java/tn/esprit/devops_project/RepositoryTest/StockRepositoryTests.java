package tn.esprit.devops_project.RepositoryTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class StockRepositoryTests {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void addStockWithProducts() {
        // Given
        Stock stock = new Stock();
        stock.setTitle("Warehouse A");

        Set<Product> products = new HashSet<>();
        Product product1 = new Product();
        product1.setTitle("Laptop");
        product1.setPrice(999.99f);
        product1.setQuantity(10);
        product1.setCategory(ProductCategory.ELECTRONICS);
        product1.setStock(stock);

        Product product2 = new Product();
        product2.setTitle("Smartphone");
        product2.setPrice(499.99f);
        product2.setQuantity(20);
        product2.setCategory(ProductCategory.ELECTRONICS);
        product2.setStock(stock);

        products.add(product1);
        products.add(product2);

        stock.setProducts(products);

        // When
        Stock savedStock = stockRepository.save(stock);

        // Then
        Assertions.assertThat(savedStock).isNotNull();
        Assertions.assertThat(savedStock.getIdStock()).isGreaterThan(0);
        Assertions.assertThat(savedStock.getProducts()).isNotNull();
        Assertions.assertThat(savedStock.getProducts().size()).isEqualTo(2);
    }
    @Test
    public void findStockById() {
        // Given
        Stock stock = new Stock();
        stock.setTitle("Warehouse B");

        Stock savedStock = stockRepository.save(stock);

        // When
        Stock foundStock = stockRepository.findById(savedStock.getIdStock()).orElse(null);

        // Then
        Assertions.assertThat(foundStock).isNotNull();
        Assertions.assertThat(foundStock.getIdStock()).isEqualTo(savedStock.getIdStock());
    }
}
