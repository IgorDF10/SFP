package com.sfp.dto.request;

import com.sfp.model.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {

    @NotBlank(message = "Nome da categoria é obrigatório")
    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    private String name;

    @NotNull(message = "Tipo da categoria é obrigatório")
    private CategoryType type;

    @Size(max = 7, message = "Cor deve ser um código hexadecimal válido")
    private String color;

    @Size(max = 50, message = "Ícone deve ter no máximo 50 caracteres")
    private String icon;

    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    private String description;
}