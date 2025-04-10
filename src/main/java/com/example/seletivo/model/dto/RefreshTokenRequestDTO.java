package com.example.seletivo.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequestDTO {
    
    @NotBlank(message = "O token de atualização é obrigatório")
    private String refreshToken;
}
