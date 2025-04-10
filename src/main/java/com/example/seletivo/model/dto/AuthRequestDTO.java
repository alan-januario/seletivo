package com.example.seletivo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Objeto de autenticação")
public class AuthRequestDTO {
    
    @NotBlank
    @Schema(description = "Nome de usuário", example = "admin")
    private String username;
    
    @NotBlank
    @Schema(description = "Senha do usuário", example = "seletivo")
    private String password;
}
