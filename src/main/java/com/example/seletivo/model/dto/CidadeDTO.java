package com.example.seletivo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados da cidade")
public class CidadeDTO {
    @Schema(description = "ID da cidade", example = "null")
    private Long id;
    
    @Schema(description = "Nome da cidade", example = "Cuiabá")
    @NotBlank
    private String nome;
    
    @Schema(description = "UF", example = "MT")
    @NotBlank
    @Size(min = 2, max = 2)
    private String uf;
}
