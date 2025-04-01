package com.example.seletivo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServidorEfetivoResponseDTO {
    
    private String nome;
    private Integer idade;
    private String unidadeLotacao;
    private String fotoUrl;
}
