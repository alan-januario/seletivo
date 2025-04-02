package com.example.seletivo.controller;

import com.example.seletivo.model.dto.LotacaoDTO;
import com.example.seletivo.model.service.LotacaoService;

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
@RequestMapping("/api/lotacoes")
@RequiredArgsConstructor
@Tag(name = "Lotações", description = "API para gerenciamento de lotações de servidores")
public class LotacaoController {

    private final LotacaoService lotacaoService;

    @GetMapping
    @Operation(summary = "Listar todas as lotações")
    public ResponseEntity<Page<LotacaoDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(lotacaoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar lotação por ID")
    public ResponseEntity<LotacaoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(lotacaoService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar nova lotação")
    public ResponseEntity<LotacaoDTO> create(@Valid @RequestBody LotacaoDTO lotacaoDTO) {
        return new ResponseEntity<>(lotacaoService.save(lotacaoDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar lotação")
    public ResponseEntity<LotacaoDTO> update(@PathVariable Long id, @Valid @RequestBody LotacaoDTO lotacaoDTO) {
        lotacaoDTO.setId(id);
        return ResponseEntity.ok(lotacaoService.save(lotacaoDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir lotação")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        lotacaoService.delete(id);
        return ResponseEntity.ok(true);
    }
}
