package com.cosek.edms.products;

import com.cosek.edms.teams.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TeamRepository teamRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Optional<Product> updateProduct(String id, Product newProductData) {
        return productRepository.findById(id).map(existingProduct -> {
            newProductData.setId(id);
            newProductData.setCreatedAt(existingProduct.getCreatedAt());
            newProductData.setUpdatedAt(LocalDateTime.now());
            return productRepository.save(newProductData);
        });
    }

    public boolean deleteProduct(String id) {
        return productRepository.findById(id).map(product -> {
            productRepository.delete(product);
            return true;
        }).orElse(false);
    }

    public Product createProduct(Product productData, String teamId) {
        productData.setId(UUID.randomUUID().toString()); // Generate ID
        productData.setCreatedAt(LocalDateTime.now());
        productData.setUpdatedAt(LocalDateTime.now());

        // 👉 Attach team if teamId is provided
        if (teamId != null && !teamId.equalsIgnoreCase("none")) {
            try {
                Long teamIdLong = Long.parseLong(teamId);
                teamRepository.findById(teamIdLong).ifPresent(productData::setTeam);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid teamId format: " + teamId);
            }
        }

        return productRepository.save(productData);
    }

}

