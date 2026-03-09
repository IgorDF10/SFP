package com.sfp.dto.response;

import com.sfp.model.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private CategoryType type;
    private String color;
    private String icon;
    private String description;
    private Boolean isActive;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}