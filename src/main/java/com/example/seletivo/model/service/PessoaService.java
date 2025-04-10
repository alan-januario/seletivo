package com.example.seletivo.model.service;

import com.example.seletivo.exception.ResourceNotFoundException;
import com.example.seletivo.model.dto.PessoaDTO;
import com.example.seletivo.model.entity.Endereco;
import com.example.seletivo.model.entity.Pessoa;
import com.example.seletivo.model.mapper.PessoaMapper;
import com.example.seletivo.model.repository.EnderecoRepository;
import com.example.seletivo.model.repository.FotoPessoaRepository;
import com.example.seletivo.model.repository.PessoaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final PessoaMapper pessoaMapper;
    private final MessageSource messageSource;
    private final EnderecoRepository enderecoRepository;

    public Page<PessoaDTO> findAll(Pageable pageable) {
        return pessoaRepository.findAll(pageable)
                .map(pessoaMapper::toDto);
    }

    public PessoaDTO findById(Long id) {
        return pessoaRepository.findById(id)
                .map(pessoaMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.pessoa.notfound", 
                                new Object[]{id}, 
                                LocaleContextHolder.getLocale())
                ));
    }

    @Transactional
    public PessoaDTO save(PessoaDTO pessoaDTO) {
        Pessoa pessoa = pessoaMapper.toEntity(pessoaDTO);
        pessoa = pessoaRepository.save(pessoa);
        return pessoaMapper.toDto(pessoa);
    }

    @Transactional
    public PessoaDTO update(PessoaDTO pessoaDTO) {
        // Verificar se a pessoa existe
        if (pessoaDTO.getId() == null || !pessoaRepository.existsById(pessoaDTO.getId())) {
            throw new IllegalArgumentException("Pessoa não encontrada com o ID: " + pessoaDTO.getId());
        }
        
        // Mapear DTO para entidade
        Pessoa pessoa = pessoaMapper.toEntity(pessoaDTO);
        
        // Salvar a entidade atualizada
        pessoa = pessoaRepository.save(pessoa);
        
        // Retornar o DTO atualizado
        return pessoaMapper.toDto(pessoa);
    }

    @Transactional
    public void delete(Long id) {
        if (!pessoaRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("error.pessoa.notfound", 
                            new Object[]{id}, 
                            LocaleContextHolder.getLocale())
            );
        }
        pessoaRepository.deleteById(id);
    }
    
    public Page<PessoaDTO> findByNome(String nome, Pageable pageable) {
        return pessoaRepository.findByNomeContaining(nome, pageable)
                .map(pessoaMapper::toDto);
    }

    @Transactional
    public void adicionarEndereco(Long pessoaId, Long enderecoId) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
            .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada"));
        
        Endereco endereco = enderecoRepository.findById(enderecoId)
            .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));
        
        pessoa.getEnderecos().add(endereco);
        pessoaRepository.save(pessoa);
    }
}
