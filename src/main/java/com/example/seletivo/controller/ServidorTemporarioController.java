package com.example.seletivo.controller;

import com.example.seletivo.model.dto.ServidorTemporarioDTO;
import com.example.seletivo.model.service.ServidorTemporarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/servidores-temporarios")
@RequiredArgsConstructor
@Tag(name = "Servidores Temporários", description = "API para gerenciamento de servidores temporários")
public class ServidorTemporarioController {

    private final ServidorTemporarioService servidorTemporarioService;

    @GetMapping
    @Operation(summary = "Listar todos os servidores temporários")
    public ResponseEntity<Page<ServidorTemporarioDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(servidorTemporarioService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar servidor temporário por ID")
    public ResponseEntity<ServidorTemporarioDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(servidorTemporarioService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo servidor temporário")
    public ResponseEntity<ServidorTemporarioDTO> create(@Valid @RequestBody ServidorTemporarioDTO servidorTemporarioDTO) {
        return new ResponseEntity<>(servidorTemporarioService.save(servidorTemporarioDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar servidor temporário")
    public ResponseEntity<ServidorTemporarioDTO> update(@PathVariable Long id, @Valid @RequestBody ServidorTemporarioDTO servidorTemporarioDTO) {
        servidorTemporarioDTO.setId(id);
        return ResponseEntity.ok(servidorTemporarioService.save(servidorTemporarioDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir servidor temporário")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        servidorTemporarioService.delete(id);
        return ResponseEntity.ok(true);
    }
}
