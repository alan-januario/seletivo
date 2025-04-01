package com.example.seletivo.controller;

import com.example.seletivo.model.dto.UnidadeDTO;
import com.example.seletivo.service.UnidadeService;
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
@RequestMapping("/api/unidades")
@RequiredArgsConstructor
@Tag(name = "Unidades", description = "API para gerenciamento de unidades")
public class UnidadeController {

    private final UnidadeService unidadeService;

    @GetMapping
    @Operation(summary = "Listar todas as unidades")
    public ResponseEntity<Page<UnidadeDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(unidadeService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar unidade por ID")
    public ResponseEntity<UnidadeDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(unidadeService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar nova unidade")
    public ResponseEntity<UnidadeDTO> create(@Valid @RequestBody UnidadeDTO unidadeDTO) {
        return new ResponseEntity<>(unidadeService.save(unidadeDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar unidade")
    public ResponseEntity<UnidadeDTO> update(@PathVariable Long id, @Valid @RequestBody UnidadeDTO unidadeDTO) {
        return ResponseEntity.ok(unidadeService.update(id, unidadeDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir unidade")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        unidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}