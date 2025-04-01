package com.example.seletivo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CidadeDTO {
    private Long id;
    
    @NotBlank
    private String nome;
    
    @NotBlank
    @Size(min = 2, max = 2)
    private String uf;
}
