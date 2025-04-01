package com.example.seletivo.service;

import com.example.seletivo.model.dto.CidadeDTO;
import com.example.seletivo.model.mapper.CidadeMapper;
import com.example.seletivo.repository.CidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CidadeService {

    private final CidadeRepository cidadeRepository;
    private final CidadeMapper cidadeMapper;

    public Page<CidadeDTO> findAll(Pageable pageable) {
        return cidadeRepository.findAll(pageable)
                .map(cidadeMapper::toDto);
    }

    public List<CidadeDTO> findByUf(String uf) {
        return cidadeRepository.findByUf(uf).stream()
                .map(cidadeMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CidadeDTO> findByNome(String nome) {
        return cidadeRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(cidadeMapper::toDto)
                .collect(Collectors.toList());
    }
}
