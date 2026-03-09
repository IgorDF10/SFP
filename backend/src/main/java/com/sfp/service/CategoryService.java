package com.sfp.service;

import com.sfp.dto.request.CategoryRequestDTO;
import com.sfp.dto.response.CategoryResponseDTO;
import com.sfp.exception.ResourceNotFoundException;
import com.sfp.exception.DuplicateResourceException;
import com.sfp.model.Category;
import com.sfp.model.User;
import com.sfp.model.enums.CategoryType;
import com.sfp.repository.CategoryRepository;
import com.sfp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public CategoryResponseDTO createCategory(Long userId, CategoryRequestDTO dto) {
        // Verifica se o usuário existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // Verifica se já existe categoria com mesmo nome e tipo
        if (categoryRepository.existsByUserIdAndNameAndType(userId, dto.getName(), dto.getType())) {
            throw new DuplicateResourceException("Já existe uma categoria com este nome e tipo");
        }

        // Cria a categoria
        Category category = Category.builder()
                .name(dto.getName())
                .type(dto.getType())
                .color(dto.getColor() != null ? dto.getColor() : "#6B7280")
                .icon(dto.getIcon() != null ? dto.getIcon() : "default")
                .description(dto.getDescription())
                .isActive(true)
                .user(user)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return mapToResponseDTO(savedCategory);
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryById(Long userId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        // Verifica se a categoria pertence ao usuário
        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }

        return mapToResponseDTO(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategoriesByUser(Long userId) {
        return categoryRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getActiveCategoriesByUser(Long userId) {
        return categoryRepository.findByUserIdAndIsActiveTrue(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getCategoriesByType(Long userId, CategoryType type) {
        return categoryRepository.findByUserIdAndTypeAndIsActiveTrue(userId, type).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponseDTO updateCategory(Long userId, Long categoryId, CategoryRequestDTO dto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        // Verifica se a categoria pertence ao usuário
        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }

        // Verifica duplicatas se o nome ou tipo mudou
        if (!category.getName().equals(dto.getName()) || !category.getType().equals(dto.getType())) {
            if (categoryRepository.existsByUserIdAndNameAndType(userId, dto.getName(), dto.getType())) {
                throw new DuplicateResourceException("Já existe uma categoria com este nome e tipo");
            }
        }

        // Atualiza os campos
        category.setName(dto.getName());
        category.setType(dto.getType());
        category.setColor(dto.getColor() != null ? dto.getColor() : category.getColor());
        category.setIcon(dto.getIcon() != null ? dto.getIcon() : category.getIcon());
        category.setDescription(dto.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return mapToResponseDTO(updatedCategory);
    }

    @Transactional
    public void deactivateCategory(Long userId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }

        category.setIsActive(false);
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long userId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }

        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public long countCategoriesByUser(Long userId) {
        return categoryRepository.countByUserId(userId);
    }

    // Método auxiliar para converter Entity -> DTO
    private CategoryResponseDTO mapToResponseDTO(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .color(category.getColor())
                .icon(category.getIcon())
                .description(category.getDescription())
                .isActive(category.getIsActive())
                .userId(category.getUser().getId())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}