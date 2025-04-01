package com.example.seletivo.service;

import com.example.seletivo.exception.ResourceNotFoundException;
import com.example.seletivo.model.dto.UnidadeDTO;
import com.example.seletivo.model.entity.Unidade;
import com.example.seletivo.model.mapper.UnidadeMapper;
import com.example.seletivo.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UnidadeService {

    private final UnidadeRepository unidadeRepository;
    private final UnidadeMapper unidadeMapper;
    private final MessageSource messageSource;

    public Page<UnidadeDTO> findAll(Pageable pageable) {
        return unidadeRepository.findAll(pageable)
                .map(unidadeMapper::toDto);
    }

    public UnidadeDTO findById(Long id) {
        return unidadeRepository.findById(id)
                .map(unidadeMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.unidade.notfound", 
                                new Object[]{id}, 
                                LocaleContextHolder.getLocale())
                ));
    }

    @Transactional
    public UnidadeDTO save(UnidadeDTO unidadeDTO) {
        Unidade unidade = unidadeMapper.toEntity(unidadeDTO);
        unidade = unidadeRepository.save(unidade);
        return unidadeMapper.toDto(unidade);
    }

    @Transactional
    public UnidadeDTO update(Long id, UnidadeDTO unidadeDTO) {
        if (!unidadeRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("error.unidade.notfound", 
                            new Object[]{id}, 
                            LocaleContextHolder.getLocale())
            );
        }
        
        Unidade unidade = unidadeMapper.toEntity(unidadeDTO);
        unidade.setId(id);
        unidade = unidadeRepository.save(unidade);
        return unidadeMapper.toDto(unidade);
    }

    @Transactional
    public void delete(Long id) {
        if (!unidadeRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("error.unidade.notfound", 
                            new Object[]{id}, 
                            LocaleContextHolder.getLocale())
            );
        }
        unidadeRepository.deleteById(id);
    }
    
    public Page<UnidadeDTO> findByNome(String nome, Pageable pageable) {
        return unidadeRepository.findByNomeContaining(nome, pageable)
                .map(unidadeMapper::toDto);
    }
    
    public Page<UnidadeDTO> findBySigla(String sigla, Pageable pageable) {
        return unidadeRepository.findBySiglaContaining(sigla, pageable)
                .map(unidadeMapper::toDto);
    }
}