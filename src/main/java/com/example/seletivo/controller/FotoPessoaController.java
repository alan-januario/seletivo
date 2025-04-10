package com.example.seletivo.controller;

import com.example.seletivo.model.dto.FotoPessoaDTO;
import com.example.seletivo.model.service.FotoPessoaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/fotos")
@RequiredArgsConstructor
@Tag(name = "Fotos", description = "API para gerenciamento de fotos de pessoas")
public class FotoPessoaController {

    private final FotoPessoaService fotoPessoaService;

    @GetMapping
    @Operation(summary = "Listar todas as fotos")
    public ResponseEntity<Page<FotoPessoaDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(fotoPessoaService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar foto por ID")
    public ResponseEntity<FotoPessoaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(fotoPessoaService.findById(id));
    }

    @PostMapping(value = "/upload/{pessoaId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Fazer upload de foto para uma pessoa")
    public ResponseEntity<FotoPessoaDTO> upload(
            @PathVariable Long pessoaId,
            @RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(fotoPessoaService.uploadFoto(pessoaId, file), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir foto")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        fotoPessoaService.delete(id);
        return ResponseEntity.ok(true);
    }
}
