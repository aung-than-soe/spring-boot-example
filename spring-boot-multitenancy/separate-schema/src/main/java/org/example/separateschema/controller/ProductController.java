package org.example.separateschema.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.separateschema.domain.Category;
import org.example.separateschema.domain.Product;
import org.example.separateschema.dto.ProductDTO;
import org.example.separateschema.repository.CategoryRepository;
import org.example.separateschema.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping("all")
    public Collection<Product> getAll() {
        return productRepository.findAll();
    }

    @PostMapping
    public void create(@RequestBody ProductDTO product) {
        Category category = categoryRepository.findById(product.categoryId()).orElseThrow(() -> new EntityNotFoundException("Category not found"));
        Product entity = Product.builder()
                .name(product.name())
                .code(product.code())
                .price(product.price())
                .description(product.description())
                .category(category)
                .build();
        productRepository.save(entity);
    }

    @GetMapping("{id}")
    public Product getById(@PathVariable(value = "id") Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable(value = "id") Long id) {
        productRepository.deleteById(id);
    }
}
