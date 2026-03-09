package com.sfp.controller;

import com.sfp.dto.request.CategoryRequestDTO;
import com.sfp.dto.response.CategoryResponseDTO;
import com.sfp.model.enums.CategoryType;
import com.sfp.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @RequestParam Long userId,
            @Valid @RequestBody CategoryRequestDTO dto) {
        CategoryResponseDTO category = categoryService.createCategory(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @RequestParam Long userId,
            @PathVariable Long id) {
        CategoryResponseDTO category = categoryService.getCategoryById(userId, id);
        return ResponseEntity.ok(category);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(@RequestParam Long userId) {
        List<CategoryResponseDTO> categories = categoryService.getAllCategoriesByUser(userId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponseDTO>> getActiveCategories(@RequestParam Long userId) {
        List<CategoryResponseDTO> categories = categoryService.getActiveCategoriesByUser(userId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesByType(
            @RequestParam Long userId,
            @PathVariable CategoryType type) {
        List<CategoryResponseDTO> categories = categoryService.getCategoriesByType(userId, type);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @RequestParam Long userId,
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO dto) {
        CategoryResponseDTO category = categoryService.updateCategory(userId, id, dto);
        return ResponseEntity.ok(category);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateCategory(
            @RequestParam Long userId,
            @PathVariable Long id) {
        categoryService.deactivateCategory(userId, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @RequestParam Long userId,
            @PathVariable Long id) {
        categoryService.deleteCategory(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countCategories(@RequestParam Long userId) {
        long count = categoryService.countCategoriesByUser(userId);
        return ResponseEntity.ok(count);
    }
}