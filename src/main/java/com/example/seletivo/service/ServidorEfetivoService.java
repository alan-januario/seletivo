package com.example.seletivo.service;

import com.example.seletivo.exception.ResourceNotFoundException;
import com.example.seletivo.model.dto.EnderecoFuncionalDTO;
import com.example.seletivo.model.dto.ServidorEfetivoDTO;
import com.example.seletivo.model.entity.Endereco;
import com.example.seletivo.model.entity.Lotacao;
import com.example.seletivo.model.entity.ServidorEfetivo;
import com.example.seletivo.model.entity.Unidade;
import com.example.seletivo.model.mapper.ServidorEfetivoMapper;
import com.example.seletivo.repository.LotacaoRepository;
import com.example.seletivo.repository.ServidorEfetivoRepository;
import com.example.seletivo.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServidorEfetivoService {

    private final ServidorEfetivoRepository servidorEfetivoRepository;
    private final LotacaoRepository lotacaoRepository;    
    private final ServidorEfetivoMapper servidorEfetivoMapper;
    private final MessageSource messageSource;
    private final UnidadeRepository unidadeRepository;

    public Page<ServidorEfetivoDTO> findAll(Pageable pageable) {
        return servidorEfetivoRepository.findAll(pageable)
                .map(servidorEfetivoMapper::toDto);
    }

    public ServidorEfetivoDTO findById(Long id) {
        return servidorEfetivoRepository.findById(id)
                .map(servidorEfetivoMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.servidorEfetivo.notfound", 
                                new Object[]{id}, 
                                LocaleContextHolder.getLocale())
                ));
    }

    @Transactional
    public ServidorEfetivoDTO save(ServidorEfetivoDTO servidorEfetivoDTO) {
        ServidorEfetivo servidorEfetivo = servidorEfetivoMapper.toEntity(servidorEfetivoDTO);
        servidorEfetivo = servidorEfetivoRepository.save(servidorEfetivo);
        return servidorEfetivoMapper.toDto(servidorEfetivo);
    }

    @Transactional
    public ServidorEfetivoDTO update(Long id, ServidorEfetivoDTO servidorEfetivoDTO) {
        if (!servidorEfetivoRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("error.servidorEfetivo.notfound", 
                            new Object[]{id}, 
                            LocaleContextHolder.getLocale())
            );
        }
        
        ServidorEfetivo servidorEfetivo = servidorEfetivoMapper.toEntity(servidorEfetivoDTO);
        servidorEfetivo.setId(id);
        servidorEfetivo = servidorEfetivoRepository.save(servidorEfetivo);
        return servidorEfetivoMapper.toDto(servidorEfetivo);
    }

    @Transactional
    public void delete(Long id) {
        if (!servidorEfetivoRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("error.servidorEfetivo.notfound", 
                            new Object[]{id}, 
                            LocaleContextHolder.getLocale())
            );
        }
        servidorEfetivoRepository.deleteById(id);
    }
    
    public Page<ServidorEfetivoDTO> findByNome(String nome, Pageable pageable) {
        return servidorEfetivoRepository.findByNomeContaining(nome, pageable)
                .map(servidorEfetivoMapper::toDto);
    }
    
    /**
     * Busca servidores efetivos por ID da unidade
     * 
     * @param unidadeId ID da unidade
     * @param pageable objeto de paginação
     * @return página de DTOs de resposta de servidores efetivos
     */
    public Page<ServidorEfetivoDTO> findByUnidadeId(Long unidadeId, Pageable pageable) {
        // Verificar se a unidade existe
        unidadeRepository.findById(unidadeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.unidade.notfound", 
                                new Object[]{unidadeId}, 
                                LocaleContextHolder.getLocale())
                ));
        
        // Buscar servidores efetivos pela unidade
        Page<ServidorEfetivo> servidores = servidorEfetivoRepository.findByUnidadeId(unidadeId, pageable);
        
        // Converter para DTOs
        return servidores.map(servidorEfetivoMapper::toDto);
    }
    
    public Page<EnderecoFuncionalDTO> findEnderecoFuncionalByNome(String nome, Pageable pageable) {
        Page<ServidorEfetivo> servidores = servidorEfetivoRepository.findByNomeContaining(nome, pageable);
        
        return servidores.map(servidor -> {
            EnderecoFuncionalDTO dto = new EnderecoFuncionalDTO();
            dto.setNomeServidor(servidor.getPessoa().getNome());
            
            // Buscar lotação atual
            Optional<Lotacao> lotacaoOpt = lotacaoRepository.findLotacaoAtualByPessoaId(servidor.getId());
            
            if (lotacaoOpt.isPresent()) {
                Lotacao lotacao = lotacaoOpt.get();
                Unidade unidade = lotacao.getUnidade();
                
                dto.setNomeUnidade(unidade.getNome());
                dto.setSiglaUnidade(unidade.getSigla());
                
                // Construir endereço completo
                StringBuilder enderecoCompleto = new StringBuilder();
                
                if (!unidade.getEnderecos().isEmpty()) {
                    Endereco endereco = unidade.getEnderecos().iterator().next();
                    
                    if (endereco.getTipoLogradouro() != null) {
                        enderecoCompleto.append(endereco.getTipoLogradouro()).append(" ");
                    }
                    
                    enderecoCompleto.append(endereco.getLogradouro());
                    
                    if (endereco.getNumero() != null) {
                        enderecoCompleto.append(", ").append(endereco.getNumero());
                    }
                    
                    enderecoCompleto.append(" - ").append(endereco.getBairro());
                    
                    if (endereco.getCidade() != null) {
                        enderecoCompleto.append(", ").append(endereco.getCidade().getNome())
                                .append("/").append(endereco.getCidade().getUf());
                    }
                }
                
                dto.setEnderecoCompleto(enderecoCompleto.toString());
            }
            
            return dto;
        });
    }
}