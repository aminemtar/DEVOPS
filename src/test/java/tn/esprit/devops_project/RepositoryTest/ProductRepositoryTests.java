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
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;
    @Test
    public void addProductWithStock() {
        // Given
        Stock stock = new Stock();


        Stock savedStock = stockRepository.save(stock);

        Product product = new Product();
        product.setTitle("Laptop");
        product.setPrice(999.99f);
        product.setQuantity(10);
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setStock(savedStock);

        // When
        Product savedProduct = productRepository.save(product);

        // Then
        Assertions.assertThat(savedProduct).isNotNull();
        Assertions.assertThat(savedProduct.getIdProduct()).isGreaterThan(0);
        Assertions.assertThat(savedProduct.getStock()).isNotNull();
        Assertions.assertThat(savedProduct.getStock().getIdStock()).isEqualTo(savedStock.getIdStock());
    }
    @Test
    public void findProductById() {
        // Given
        Product product = new Product();
        product.setTitle("Smartphone");
        product.setPrice(499.99f);
        product.setQuantity(20);
        product.setCategory(ProductCategory.ELECTRONICS);

        Product savedProduct = productRepository.save(product);
        Product foundProduct = productRepository.findById(savedProduct.getIdProduct()).orElse(null);
        Assertions.assertThat(foundProduct).isNotNull();
        Assertions.assertThat(foundProduct.getIdProduct()).isEqualTo(savedProduct.getIdProduct());
    }
}
