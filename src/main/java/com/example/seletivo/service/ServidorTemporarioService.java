package com.example.seletivo.service;

import com.example.seletivo.exception.ResourceNotFoundException;
import com.example.seletivo.model.dto.ServidorTemporarioDTO;
import com.example.seletivo.model.entity.ServidorTemporario;
import com.example.seletivo.model.mapper.ServidorTemporarioMapper;
import com.example.seletivo.repository.ServidorTemporarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServidorTemporarioService {

    private final ServidorTemporarioRepository servidorTemporarioRepository;
    private final ServidorTemporarioMapper servidorTemporarioMapper;
    private final MessageSource messageSource;

    public Page<ServidorTemporarioDTO> findAll(Pageable pageable) {
        return servidorTemporarioRepository.findAll(pageable)
                .map(servidorTemporarioMapper::toDto);
    }

    public ServidorTemporarioDTO findById(Long id) {
        return servidorTemporarioRepository.findById(id)
                .map(servidorTemporarioMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.servidorTemporario.notfound", 
                                new Object[]{id}, 
                                LocaleContextHolder.getLocale())
                ));
    }

    @Transactional
    public ServidorTemporarioDTO save(ServidorTemporarioDTO servidorTemporarioDTO) {
        ServidorTemporario servidorTemporario = servidorTemporarioMapper.toEntity(servidorTemporarioDTO);
        servidorTemporario = servidorTemporarioRepository.save(servidorTemporario);
        return servidorTemporarioMapper.toDto(servidorTemporario);
    }

    @Transactional
    public ServidorTemporarioDTO update(Long id, ServidorTemporarioDTO servidorTemporarioDTO) {
        if (!servidorTemporarioRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("error.servidorTemporario.notfound", 
                            new Object[]{id}, 
                            LocaleContextHolder.getLocale())
            );
        }
        
        ServidorTemporario servidorTemporario = servidorTemporarioMapper.toEntity(servidorTemporarioDTO);
        servidorTemporario.setId(id);
        servidorTemporario = servidorTemporarioRepository.save(servidorTemporario);
        return servidorTemporarioMapper.toDto(servidorTemporario);
    }

    @Transactional
    public void delete(Long id) {
        if (!servidorTemporarioRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("error.servidorTemporario.notfound", 
                            new Object[]{id}, 
                            LocaleContextHolder.getLocale())
            );
        }
        servidorTemporarioRepository.deleteById(id);
    }
    
    public Page<ServidorTemporarioDTO> findByNome(String nome, Pageable pageable) {
        return servidorTemporarioRepository.findByNomeContaining(nome, pageable)
                .map(servidorTemporarioMapper::toDto);
    }
    
    public Page<ServidorTemporarioDTO> findAtivos(Pageable pageable) {
        return servidorTemporarioRepository.findAtivos(pageable)
                .map(servidorTemporarioMapper::toDto);
    }
}
