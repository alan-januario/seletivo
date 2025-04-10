package com.example.seletivo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoFuncionalDTO {
    private String nomeServidor;
    private String nomeUnidade;
    private String siglaUnidade;
    private String enderecoCompleto;
}
