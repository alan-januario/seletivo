package com.example.seletivo.service;

import com.example.seletivo.exception.ResourceNotFoundException;
import com.example.seletivo.model.dto.PessoaDTO;
import com.example.seletivo.model.entity.Pessoa;
import com.example.seletivo.model.mapper.PessoaMapper;
import com.example.seletivo.repository.PessoaRepository;
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
    public PessoaDTO update(Long id, PessoaDTO pessoaDTO) {
        if (!pessoaRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("error.pessoa.notfound", 
                            new Object[]{id}, 
                            LocaleContextHolder.getLocale())
            );
        }
        
        Pessoa pessoa = pessoaMapper.toEntity(pessoaDTO);
        pessoa.setId(id);
        pessoa = pessoaRepository.save(pessoa);
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
}
