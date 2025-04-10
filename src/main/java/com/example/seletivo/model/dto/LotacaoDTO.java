package com.example.seletivo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados de lotação de um servidor")
public class LotacaoDTO {
    
    @Schema(description = "ID da lotação", example = "1")
    private Long id;
    
    @NotNull
    @Schema(description = "Dados da pessoa associada à lotação")
    private PessoaDTO pessoa;
    
    @NotNull
    @Schema(description = "Dados da unidade onde a pessoa está lotada")
    private UnidadeDTO unidade;
    
    @NotNull(message = "{validation.lotacao.dataLotacao.notnull}")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(description = "Data em que a lotação foi realizada", example = "01/01/2023")
    private LocalDate dataLotacao;
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(description = "Data em que a pessoa foi removida da lotação, se aplicável", example = "31/12/2023")
    private LocalDate dataRemocao;
    
    @Schema(description = "Número da portaria que oficializou a lotação", example = "Portaria nº 123/2023")
    private String portaria;
}
