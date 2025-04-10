package com.example.seletivo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados pessoais")
public class PessoaDTO {
    @Schema(description = "ID da pessoa", example = "null")
    private Long id;
    
    @Schema(description = "Nome completo", example = "João da Silva")
    @NotBlank(message = "{validation.pessoa.nome.notblank}")
    private String nome;
    
    @Schema(description = "Data de nascimento", example = "01/01/1990")
    @NotNull(message = "{validation.pessoa.dataNascimento.notnull}")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;
    
    @Schema(description = "Sexo", example = "M")
    @NotBlank(message = "{validation.pessoa.sexo.notblank}")
    private String sexo;
    
    @Schema(description = "Nome da mãe", example = "Maria da Silva")
    private String mae;
    
    @Schema(description = "Nome do pai", example = "José da Silva")
    private String pai;
    
    @Schema(description = "Lista de endereços")
    private Set<EnderecoDTO> enderecos;

    @Schema(description = "Fotos do servidor")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FotoPessoaDTO fotoPessoa;
}
