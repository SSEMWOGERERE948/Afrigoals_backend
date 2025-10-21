package com.cosek.edms.products;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Double originalPrice;
    private String sku;
    private Integer stock;
    private String categoryId;
    private String teamId;
    private String teamName; // optional
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

