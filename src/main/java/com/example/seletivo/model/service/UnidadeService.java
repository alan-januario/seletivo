package com.example.seletivo.model.service;

import com.example.seletivo.exception.ResourceNotFoundException;
import com.example.seletivo.model.dto.EnderecoDTO;
import com.example.seletivo.model.dto.UnidadeDTO;
import com.example.seletivo.model.entity.Cidade;
import com.example.seletivo.model.entity.Endereco;
import com.example.seletivo.model.entity.Unidade;
import com.example.seletivo.model.mapper.UnidadeMapper;
import com.example.seletivo.model.repository.CidadeRepository;
import com.example.seletivo.model.repository.EnderecoRepository;
import com.example.seletivo.model.repository.UnidadeRepository;

import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class UnidadeService {

    private final UnidadeRepository unidadeRepository;
    private final UnidadeMapper unidadeMapper;
    private final MessageSource messageSource;
    private final EnderecoRepository enderecoRepository;
    private final CidadeRepository cidadeRepository;

    public Page<UnidadeDTO> findAll(Pageable pageable) {
        Pageable pageableWithSort = pageable;
        if (!pageable.getSort().isSorted()) {
            pageableWithSort = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id")
            );
        }
        
        return unidadeRepository.findAll(pageableWithSort)
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
        // Verificar se é uma atualização (ID presente) ou uma criação
        boolean isUpdate = unidadeDTO.getId() != null && unidadeDTO.getId() > 0;
        
        if (isUpdate) {
            // Verificar se a unidade existe antes de atualizar
            Unidade unidadeExistente = unidadeRepository.findById(unidadeDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada com o ID: " + unidadeDTO.getId()));
            
            // Atualizar os dados básicos da unidade
            unidadeExistente.setNome(unidadeDTO.getNome());
            unidadeExistente.setSigla(unidadeDTO.getSigla());
            // Atualizar outros campos conforme necessário
            
            // Salvar a unidade com os dados atualizados
            Unidade unidadeSalva = unidadeRepository.save(unidadeExistente);
            
            // Processar os endereços
            if (unidadeDTO.getEnderecos() != null && !unidadeDTO.getEnderecos().isEmpty()) {
                // Opção 1: Remover todos os endereços existentes e adicionar os novos
                // unidadeExistente.getEnderecos().clear();
                
                // Opção 2: Atualizar endereços existentes e adicionar novos
                Set<Endereco> enderecosAtualizados = new HashSet<>();
                
                for (EnderecoDTO enderecoDTO : unidadeDTO.getEnderecos()) {
                    Endereco endereco;
                    
                    // Se o endereço já tem ID, atualizar
                    if (enderecoDTO.getId() != null && enderecoDTO.getId() > 0) {
                        
                        endereco = enderecoRepository.findById(enderecoDTO.getId())
                            .orElseGet(() -> new Endereco());
                        
                        // Atualizar dados do endereço
                        endereco.setTipoLogradouro(enderecoDTO.getTipoLogradouro());
                        endereco.setLogradouro(enderecoDTO.getLogradouro());
                        endereco.setNumero(enderecoDTO.getNumero());
                        endereco.setBairro(enderecoDTO.getBairro());
                    } else {
                        // Criar novo endereço
                        endereco = new Endereco();
                        endereco.setTipoLogradouro(enderecoDTO.getTipoLogradouro());
                        endereco.setLogradouro(enderecoDTO.getLogradouro());
                        endereco.setNumero(enderecoDTO.getNumero());
                        endereco.setBairro(enderecoDTO.getBairro());
                    }
                    
                    // Processar a cidade
                    if (enderecoDTO.getCidade() != null) {
                        Cidade cidade;
                        
                        // Se a cidade tem ID, buscar do banco
                        if (enderecoDTO.getCidade().getId() != null && enderecoDTO.getCidade().getId() > 0) {
                            cidade = cidadeRepository.findById(enderecoDTO.getCidade().getId())
                                .orElseGet(() -> {
                                    // Se não encontrar, criar nova cidade
                                    Cidade novaCidade = new Cidade();
                                    novaCidade.setNome(enderecoDTO.getCidade().getNome());
                                    novaCidade.setUf(enderecoDTO.getCidade().getUf());
                                    return cidadeRepository.save(novaCidade);
                                });
                        } else {
                            // Buscar cidade pelo nome
                            List<Cidade> cidades = cidadeRepository.findByNomeContainingIgnoreCase(
                                enderecoDTO.getCidade().getNome());
                            
                            if (!cidades.isEmpty()) {
                                cidade = cidades.get(0);
                            } else {
                                // Criar nova cidade
                                cidade = new Cidade();
                                cidade.setNome(enderecoDTO.getCidade().getNome());
                                cidade.setUf(enderecoDTO.getCidade().getUf());
                                cidade = cidadeRepository.save(cidade);
                            }
                        }
                        
                        endereco.setCidade(cidade);
                    }
                    
                    // Salvar o endereço
                    endereco = enderecoRepository.save(endereco);
                    enderecosAtualizados.add(endereco);
                }
                
                // Atualizar a coleção de endereços da unidade
                unidadeSalva.setEnderecos(enderecosAtualizados);
                unidadeSalva = unidadeRepository.save(unidadeSalva);
            }
            
            return unidadeMapper.toDto(unidadeSalva);
        } else {
            // Caso de criação - código existente
            // 1. Primeiro, salva a unidade sem endereços
            Unidade unidade = unidadeMapper.toEntity(unidadeDTO);
            Set<Endereco> enderecos = new HashSet<>(unidade.getEnderecos());
            unidade.setEnderecos(new HashSet<>());
            unidade = unidadeRepository.save(unidade);
            
            // 2. Para cada endereço, processa a cidade e salva o endereço
            for (Endereco endereco : enderecos) {
                // 2.1 Processa a cidade
                if (endereco.getCidade() != null) {
                    Cidade cidade;
                    
                    // Se a cidade não tem ID, verifica se já existe ou cria uma nova
                    if (endereco.getCidade().getId() == null) {
                        cidade = cidadeRepository.save(endereco.getCidade());
                    } else {
                        // Se tem ID, busca do banco
                        cidade = cidadeRepository.findById(endereco.getCidade().getId()).orElse(null);
                    }
                    
                    // 2.2 Associa a cidade ao endereço e salva
                    if (cidade != null) {
                        endereco.setCidade(cidade);
                        endereco = enderecoRepository.save(endereco);
                        
                        // 2.3 Adiciona o endereço salvo à unidade
                        unidade.getEnderecos().add(endereco);
                    }
                }
            }
            
            // 3. Salva a unidade com os endereços
            if (!unidade.getEnderecos().isEmpty()) {
                unidade = unidadeRepository.save(unidade);
            }
            
            return unidadeMapper.toDto(unidade);
        }
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
        Pageable pageableWithSort = pageable;
        if (!pageable.getSort().isSorted()) {
            pageableWithSort = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id")
            );
        }
        
        return unidadeRepository.findByNomeContaining(nome, pageableWithSort)
                .map(unidadeMapper::toDto);
    }
    
    public Page<UnidadeDTO> findBySigla(String sigla, Pageable pageable) {
        Pageable pageableWithSort = pageable;
        if (!pageable.getSort().isSorted()) {
            pageableWithSort = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id")
            );
        }
        
        return unidadeRepository.findBySiglaContaining(sigla, pageableWithSort)
                .map(unidadeMapper::toDto);
    }
}