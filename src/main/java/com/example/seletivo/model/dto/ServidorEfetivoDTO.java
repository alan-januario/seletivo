package com.example.seletivo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServidorEfetivoDTO {
    private Long id;
    
    @NotNull
    private PessoaDTO pessoa;
    
    @NotBlank(message = "{validation.servidorEfetivo.matricula.notblank}")
    private String matricula;
}
