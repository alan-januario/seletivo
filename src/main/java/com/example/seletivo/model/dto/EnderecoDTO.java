package com.example.seletivo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDTO {
    private Long id;
    
    private String tipoLogradouro;
    
    @NotBlank(message = "{validation.endereco.logradouro.notblank}")
    private String logradouro;
    
    private String numero;
    
    @NotBlank(message = "{validation.endereco.bairro.notblank}")
    private String bairro;
    
    @NotNull(message = "{validation.endereco.cidade.notnull}")
    private CidadeDTO cidade;
}
