package com.example.seletivo.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados do servidor efetivo")
public class ServidorEfetivoDTO {
    @Schema(description = "ID do servidor", example = "null")
    private Long id;
    
    @Schema(description = "Dados pessoais do servidor")
    private PessoaDTO pessoa;
    
    @Schema(description = "Matrícula do servidor", example = "123456")
    @NotBlank(message = "{validation.servidorEfetivo.matricula.notblank}")
    private String matricula;
}
