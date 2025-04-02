package com.example.seletivo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados de endereço")
public class EnderecoDTO {
    @Schema(description = "ID do endereço", example = "null")
    private Long id;
    
    @Schema(description = "Tipo de logradouro", example = "Rua")
    private String tipoLogradouro;
    
    @Schema(description = "Logradouro", example = "das Flores")
    private String logradouro;
    
    @Schema(description = "Número", example = "123")
    private String numero;
    
    @Schema(description = "Bairro", example = "Centro")
    private String bairro;
    
    @Schema(description = "Cidade")
    private CidadeDTO cidade;
}
