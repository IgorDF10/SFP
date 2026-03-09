package com.sfp.model;

import com.sfp.model.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_user_date", columnList = "user_id, date"),
    @Index(name = "idx_user_type", columnList = "user_id, type"),
    @Index(name = "idx_category", columnList = "category_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 3, max = 200, message = "Descrição deve ter entre 3 e 200 caracteres")
    @Column(nullable = false, length = 200)
    private String description;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Valor deve ter no máximo 10 dígitos inteiros e 2 decimais")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "Tipo da transação é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;

    @NotNull(message = "Data é obrigatória")
    @Column(nullable = false)
    private LocalDate date;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    @Column(length = 500)
    private String notes;

    @Column(name = "is_recurring")
    @Builder.Default
    private Boolean isRecurring = false;

    @Column(name = "is_paid")
    @Builder.Default
    private Boolean isPaid = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public BigDecimal getSignedAmount() {
        return type == TransactionType.EXPENSE ? amount.negate() : amount;
    }

    @PrePersist
    @PreUpdate
    private void validateCategoryType() {
        if (category != null && type != null) {
            boolean typesMatch = (type == TransactionType.INCOME && category.getType() == com.sfp.model.enums.CategoryType.INCOME) ||
                                (type == TransactionType.EXPENSE && category.getType() == com.sfp.model.enums.CategoryType.EXPENSE);
            
            if (!typesMatch) {
                throw new IllegalStateException(
                    "Tipo da transação deve ser compatível com o tipo da categoria"
                );
            }
        }
    }
}