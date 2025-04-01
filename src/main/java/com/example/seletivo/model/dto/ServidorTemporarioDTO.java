package com.example.seletivo.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServidorTemporarioDTO {
    private Long id;
    
    @NotNull
    private PessoaDTO pessoa;
    
    @NotNull(message = "{validation.servidorTemporario.dataAdmissao.notnull}")
    private LocalDate dataAdmissao;
    
    private LocalDate dataDemissao;
}
