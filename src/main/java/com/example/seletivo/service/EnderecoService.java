package com.example.seletivo.service;

import com.example.seletivo.model.dto.EnderecoDTO;
import com.example.seletivo.model.entity.Endereco;
import com.example.seletivo.model.mapper.EnderecoMapper;
import com.example.seletivo.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final EnderecoMapper enderecoMapper;

    public Page<EnderecoDTO> findAll(Pageable pageable) {
        return enderecoRepository.findAll(pageable)
                .map(enderecoMapper::toDto);
    }

    public EnderecoDTO save(EnderecoDTO enderecoDTO) {
        Endereco endereco = enderecoMapper.toEntity(enderecoDTO);
        endereco = enderecoRepository.save(endereco);
        return enderecoMapper.toDto(endereco);
    }
}
