package org.example.separateschema.controller;

import lombok.RequiredArgsConstructor;
import org.example.separateschema.domain.Category;
import org.example.separateschema.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping("all")
    public Collection<Category> getAll() {
        return categoryRepository.findAll();
    }

    @PostMapping
    public void create(@RequestBody Category category) {
        categoryRepository.save(category);
    }

    @GetMapping("{id}")
    public Category getById(@PathVariable(value = "id") Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable(value = "id") Long id) {
        categoryRepository.deleteById(id);
    }
}
