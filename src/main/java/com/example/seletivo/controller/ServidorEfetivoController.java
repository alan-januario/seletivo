package com.example.seletivo.controller;

import com.example.seletivo.model.dto.EnderecoFuncionalDTO;
import com.example.seletivo.model.dto.ServidorEfetivoDTO;
import com.example.seletivo.service.ServidorEfetivoService;
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
@RequestMapping("/api/servidores-efetivos")
@RequiredArgsConstructor
@Tag(name = "Servidores Efetivos", description = "API para gerenciamento de servidores efetivos")
public class ServidorEfetivoController {

    private final ServidorEfetivoService servidorEfetivoService;

    @GetMapping
    @Operation(summary = "Listar todos os servidores efetivos")
    public ResponseEntity<Page<ServidorEfetivoDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(servidorEfetivoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar servidor efetivo por ID")
    public ResponseEntity<ServidorEfetivoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(servidorEfetivoService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo servidor efetivo")
    public ResponseEntity<ServidorEfetivoDTO> create(@Valid @RequestBody ServidorEfetivoDTO servidorEfetivoDTO) {
        return new ResponseEntity<>(servidorEfetivoService.save(servidorEfetivoDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar servidor efetivo")
    public ResponseEntity<ServidorEfetivoDTO> update(@PathVariable Long id, @Valid @RequestBody ServidorEfetivoDTO servidorEfetivoDTO) {
        return ResponseEntity.ok(servidorEfetivoService.update(id, servidorEfetivoDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir servidor efetivo")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servidorEfetivoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unidade/{unidadeId}")
    @Operation(summary = "Listar servidores efetivos por unidade")
    public ResponseEntity<Page<ServidorEfetivoDTO>> findByUnidadeId(
            @PathVariable Long unidadeId, Pageable pageable) {
        return ResponseEntity.ok(servidorEfetivoService.findByUnidadeId(unidadeId, pageable));
    }

    @GetMapping("/endereco-funcional")
    @Operation(summary = "Buscar endereço funcional por nome do servidor")
    public ResponseEntity<Page<EnderecoFuncionalDTO>> findEnderecoFuncionalByNome(
            @RequestParam String nome, Pageable pageable) {
        return ResponseEntity.ok(servidorEfetivoService.findEnderecoFuncionalByNome(nome, pageable));
    }
}
