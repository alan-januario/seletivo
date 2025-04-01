package com.example.seletivo.config;

import com.example.seletivo.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioService usuarioService;

    @Override
    public void run(String... args) {
        // Criar usuário admin padrão
        usuarioService.criarUsuarioAdmin();
    }
}
