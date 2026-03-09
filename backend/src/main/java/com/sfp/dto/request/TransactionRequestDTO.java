package com.sfp.dto.request;

import com.sfp.model.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 3, max = 200, message = "Descrição deve ter entre 3 e 200 caracteres")
    private String description;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Valor deve ter no máximo 10 dígitos inteiros e 2 decimais")
    private BigDecimal amount;

    @NotNull(message = "Tipo da transação é obrigatório")
    private TransactionType type;

    @NotNull(message = "Data é obrigatória")
    private LocalDate date;

    @NotNull(message = "Categoria é obrigatória")
    private Long categoryId;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String notes;

    private Boolean isRecurring = false;

    private Boolean isPaid = true;
}