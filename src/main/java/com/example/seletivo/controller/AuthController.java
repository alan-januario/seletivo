package com.example.seletivo.controller;

import com.example.seletivo.model.dto.AuthRequestDTO;
import com.example.seletivo.model.dto.AuthResponseDTO;
import com.example.seletivo.model.dto.RefreshTokenRequestDTO;
import com.example.seletivo.model.security.JwtTokenProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "API para autenticação de usuários")
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final JwtTokenProvider jwtTokenProvider;

        @PostMapping("/login")
        @Operation(
            summary = "Autenticar usuário", 
            description = "Autentica o usuário e retorna tokens de acesso e refresh",
            responses = {
                @ApiResponse(
                    responseCode = "200", 
                    description = "Autenticação bem-sucedida",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = AuthResponseDTO.class)
                    )
                )
            },
            tags = {"Autenticação"}
        )
        public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO authRequest) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.generateToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

            return ResponseEntity.ok(new AuthResponseDTO(accessToken, refreshToken));
        }

        @PostMapping("/refresh")
        @Operation(summary = "Renovar token de acesso")
        public ResponseEntity<AuthResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequest) {
            // Validar o refresh token
            if (!jwtTokenProvider.validateToken(refreshTokenRequest.getRefreshToken())) {
                return ResponseEntity.badRequest().build();
            }

            // Obter o usuário a partir do refresh token
            //String username = jwtTokenProvider.getUsernameFromToken(refreshTokenRequest.getRefreshToken());
            Authentication authentication = jwtTokenProvider.getAuthentication(refreshTokenRequest.getRefreshToken());
        
            // Gerar novos tokens
            String newAccessToken = jwtTokenProvider.generateToken(authentication);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);

            return ResponseEntity.ok(new AuthResponseDTO(newAccessToken, newRefreshToken));
        }
}
