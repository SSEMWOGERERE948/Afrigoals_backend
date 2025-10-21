package com.cosek.edms.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // GET /api/products
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        Map<String, Object> response = new HashMap<>();
        response.put("products", productService.getAllProducts());
        return ResponseEntity.ok(response);
    }

    // GET /api/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable String id) {
        return productService.getProductById(id)
                .map(product -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("product", product);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", "Product not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    // PUT /api/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable String id, @RequestBody Product productData) {
        return productService.updateProduct(id, productData)
                .map(updated -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("product", updated);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", "Product not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    // DELETE /api/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable String id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Product deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Product not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping("/createProduct")
    public ResponseEntity<?> createProduct(@RequestBody Map<String, Object> payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.convertValue(payload, Product.class);

            String teamId = payload.get("teamId") != null ? payload.get("teamId").toString() : null;

            Product createdProduct = productService.createProduct(product, teamId);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("product", createdProduct));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create product", "details", e.getMessage()));
        }
    }


}
