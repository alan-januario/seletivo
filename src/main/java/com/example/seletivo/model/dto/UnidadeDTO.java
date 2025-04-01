package com.example.seletivo.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeDTO {
    private Long id;
    
    @NotBlank(message = "{validation.unidade.nome.notblank}")
    private String nome;
    
    @NotBlank(message = "{validation.unidade.sigla.notblank}")
    private String sigla;
    
    private Set<EnderecoDTO> enderecos;
}
