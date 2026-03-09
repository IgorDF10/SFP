package com.sfp.dto.response;

import com.sfp.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDTO {

    private Long id;
    private String description;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDate date;
    private String notes;
    private Boolean isRecurring;
    private Boolean isPaid;
    private Long userId;
    private Long categoryId;
    private String categoryName;
    private String categoryColor;
    private String categoryIcon;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}