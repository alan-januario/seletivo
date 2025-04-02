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
@Schema(description = "Dados de um servidor temporário")
public class ServidorTemporarioDTO {
    
    @Schema(description = "ID do servidor temporário", example = "1")
    private Long id;
    
    @NotNull
    @Schema(description = "Dados pessoais do servidor")
    private PessoaDTO pessoa;
    
    @NotNull(message = "{validation.servidorTemporario.dataAdmissao.notnull}")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(description = "Data de admissão do servidor", example = "01/01/2023")
    private LocalDate dataAdmissao;
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(description = "Data de demissão do servidor, se houver", example = "31/12/2023")
    private LocalDate dataDemissao;
}
