package com.example.seletivo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaDTO {
    private Long id;
    
    @NotBlank(message = "{validation.pessoa.nome.notblank}")
    private String nome;
    
    @NotNull(message = "{validation.pessoa.dataNascimento.notnull}")
    private LocalDate dataNascimento;
    
    @NotBlank(message = "{validation.pessoa.sexo.notblank}")
    private String sexo;
    
    private String mae;
    
    private String pai;
    
    private Set<EnderecoDTO> enderecos;
}
