package com.example.seletivo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FotoPessoaDTO {
    private Long id;
    private Long pessoaId;
    private String pessoaNome;
    private LocalDate data;
    private String bucket;
    private String hash;
    private String url; // Campo para URL pré-assinada
}
