package com.example.seletivo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados de uma unidade organizacional")
public class UnidadeDTO {
    
    @Schema(description = "ID da unidade", example = "null")
    private Long id;
    
    @NotBlank(message = "{validation.unidade.nome.notblank}")
    @Schema(description = "Nome da unidade", example = "Secretaria de Estado de Planejamento e Gestão")
    private String nome;
    
    @NotBlank(message = "{validation.unidade.sigla.notblank}")
    @Schema(description = "Sigla da unidade", example = "SEPLAG")
    private String sigla;
    
    @Schema(description = "Lista de endereços da unidade")
    private Set<EnderecoDTO> enderecos;
}
