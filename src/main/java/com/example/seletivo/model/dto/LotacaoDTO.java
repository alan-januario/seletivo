package com.example.seletivo.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotacaoDTO {
    private Long id;
    
    @NotNull
    private PessoaDTO pessoa;
    
    @NotNull
    private UnidadeDTO unidade;
    
    @NotNull(message = "{validation.lotacao.dataLotacao.notnull}")
    private LocalDate dataLotacao;
    
    private LocalDate dataRemocao;
    
    private String portaria;
}
