package com.example.seletivo.model.service;

import com.example.seletivo.exception.ResourceNotFoundException;
import com.example.seletivo.model.dto.EnderecoDTO;
import com.example.seletivo.model.dto.EnderecoFuncionalDTO;
import com.example.seletivo.model.dto.FotoPessoaDTO;
import com.example.seletivo.model.dto.ServidorEfetivoDTO;
import com.example.seletivo.model.entity.Cidade;
import com.example.seletivo.model.entity.Endereco;
import com.example.seletivo.model.entity.Lotacao;
import com.example.seletivo.model.entity.Pessoa;
import com.example.seletivo.model.entity.ServidorEfetivo;
import com.example.seletivo.model.entity.Unidade;
import com.example.seletivo.model.mapper.ServidorEfetivoMapper;
import com.example.seletivo.model.repository.CidadeRepository;
import com.example.seletivo.model.repository.EnderecoRepository;
import com.example.seletivo.model.repository.FotoPessoaRepository;
import com.example.seletivo.model.repository.LotacaoRepository;
import com.example.seletivo.model.repository.ServidorEfetivoRepository;
import com.example.seletivo.model.repository.UnidadeRepository;
import com.example.seletivo.model.repository.PessoaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServidorEfetivoService {

    private final ServidorEfetivoRepository servidorEfetivoRepository;
    private final LotacaoRepository lotacaoRepository;    
    private final ServidorEfetivoMapper servidorEfetivoMapper;
    private final MessageSource messageSource;
    private final UnidadeRepository unidadeRepository;
    private final EnderecoRepository enderecoRepository;
    private final CidadeRepository cidadeRepository;    
    private final PessoaRepository pessoaRepository;
    private final FotoPessoaRepository fotoPessoaRepository;
    private final FotoPessoaService fotoPessoaService;

    public Page<ServidorEfetivoDTO> findAll(Pageable pageable) {
        // Criar um novo Pageable com ordenação por ID decrescente se não houver ordenação definida
        Pageable pageableWithSort = pageable;
        if (!pageable.getSort().isSorted()) {
            pageableWithSort = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id")
            );
        }
        
        return servidorEfetivoRepository.findAll(pageableWithSort)
                .map(servidorEfetivoMapper::toDto);
    }

    public ServidorEfetivoDTO findById(Long id) {
        ServidorEfetivoDTO servidorDTO = servidorEfetivoRepository.findById(id)
                .map(servidorEfetivoMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.servidorEfetivo.notfound", 
                                new Object[]{id}, 
                                LocaleContextHolder.getLocale())
                ));
        
        // Adicionar foto da pessoa
        adicionarFotoPessoa(servidorDTO);
        
        return servidorDTO;
    }

    /**
     * Adiciona a foto mais recente da pessoa ao DTO do servidor
     * 
     * @param servidorDTO DTO do servidor a ser enriquecido com a foto
     */
    private void adicionarFotoPessoa(ServidorEfetivoDTO servidorDTO) {
        if (servidorDTO.getPessoa() != null && servidorDTO.getPessoa().getId() != null) {
            fotoPessoaRepository.findTopByPessoaIdOrderByDataDesc(servidorDTO.getPessoa().getId())
                .ifPresent(fotoPessoa -> {
                    try {
                        FotoPessoaDTO fotoDTO = fotoPessoaService.findById(fotoPessoa.getId());
                        servidorDTO.getPessoa().setFotoPessoa(fotoDTO);
                    } catch (Exception e) {
                        // Log do erro, mas continua o processamento
                        System.err.println("Erro ao recuperar foto da pessoa: " + e.getMessage());
                    }
                });
        }
    }

    @Transactional
    public ServidorEfetivoDTO save(ServidorEfetivoDTO servidorEfetivoDTO) {
        try {
            // Verificar se é uma atualização (ID presente) ou uma criação
            boolean isUpdate = servidorEfetivoDTO.getId() != null && servidorEfetivoDTO.getId() > 0;
            
            if (isUpdate) {
                // Caso de atualização - buscar o servidor existente
                ServidorEfetivo servidorExistente = servidorEfetivoRepository.findById(servidorEfetivoDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Servidor efetivo não encontrado com o ID: " + servidorEfetivoDTO.getId()));
                
                Pessoa pessoaExistente = servidorExistente.getPessoa();
                
                // Atualizar dados da pessoa
                if (servidorEfetivoDTO.getPessoa() != null) {
                    pessoaExistente.setNome(servidorEfetivoDTO.getPessoa().getNome());
                    pessoaExistente.setDataNascimento(servidorEfetivoDTO.getPessoa().getDataNascimento());
                    pessoaExistente.setSexo(servidorEfetivoDTO.getPessoa().getSexo());
                    pessoaExistente.setMae(servidorEfetivoDTO.getPessoa().getMae());
                    pessoaExistente.setPai(servidorEfetivoDTO.getPessoa().getPai());
                }
                
                // Atualizar dados do servidor efetivo
                servidorExistente.setMatricula(servidorEfetivoDTO.getMatricula());
                
                // Salvar as alterações
                pessoaRepository.save(pessoaExistente);
                
                // Processar endereços se existirem
                if (servidorEfetivoDTO.getPessoa() != null && 
                    servidorEfetivoDTO.getPessoa().getEnderecos() != null) {
                    
                    for (EnderecoDTO enderecoDTO : servidorEfetivoDTO.getPessoa().getEnderecos()) {
                        // Verificar se o endereço já existe (tem ID) e não é zero
                        if (enderecoDTO.getId() != null && enderecoDTO.getId() > 0) {
                            // Verificar se o endereço já está associado à pessoa
                            // Se não estiver, associar
                            servidorEfetivoRepository.adicionarEnderecoPessoa(pessoaExistente.getId(), enderecoDTO.getId());
                            continue;
                        }
                        
                        // Criar e configurar o endereço
                        Endereco endereco = new Endereco();
                        endereco.setTipoLogradouro(enderecoDTO.getTipoLogradouro());
                        endereco.setLogradouro(enderecoDTO.getLogradouro());
                        endereco.setNumero(enderecoDTO.getNumero());
                        endereco.setBairro(enderecoDTO.getBairro());
                        
                        // Processar a cidade
                        if (enderecoDTO.getCidade() != null) {
                            Cidade cidade;
                            List<Cidade> cidades = cidadeRepository.findByNomeContainingIgnoreCase(
                                enderecoDTO.getCidade().getNome());
                            
                            if (!cidades.isEmpty()) {
                                cidade = cidades.get(0);
                            } else {
                                cidade = new Cidade();
                                cidade.setNome(enderecoDTO.getCidade().getNome());
                                cidade.setUf(enderecoDTO.getCidade().getUf());
                                cidade = cidadeRepository.save(cidade);
                            }
                            
                            endereco.setCidade(cidade);
                        }
                        
                        // Salvar o endereço
                        endereco = enderecoRepository.save(endereco);
                        
                        // Inserir na tabela de junção usando JDBC
                        servidorEfetivoRepository.adicionarEnderecoPessoa(pessoaExistente.getId(), endereco.getId());
                    }
                }
                
                return servidorEfetivoMapper.toDto(servidorExistente);
            } else {
                // Caso de criação - código existente
                // 1. Criar a pessoa
                Pessoa pessoa = new Pessoa();
                if (servidorEfetivoDTO.getPessoa() != null) {
                    pessoa.setNome(servidorEfetivoDTO.getPessoa().getNome());
                    pessoa.setDataNascimento(servidorEfetivoDTO.getPessoa().getDataNascimento());
                    pessoa.setSexo(servidorEfetivoDTO.getPessoa().getSexo());
                    pessoa.setMae(servidorEfetivoDTO.getPessoa().getMae());
                    pessoa.setPai(servidorEfetivoDTO.getPessoa().getPai());
                    pessoa.setEnderecos(new HashSet<>());
                    pessoa.setFotos(new HashSet<>());
                    pessoa.setLotacoes(new HashSet<>());
                }
                
                // 2. Criar o servidor efetivo e associá-lo à pessoa
                ServidorEfetivo servidorEfetivo = new ServidorEfetivo();
                servidorEfetivo.setMatricula(servidorEfetivoDTO.getMatricula());
                servidorEfetivo.setPessoa(pessoa);
                pessoa.setServidorEfetivo(servidorEfetivo);
                
                // 3. Salvar a pessoa (isso salvará o servidor efetivo em cascata)
                pessoa = pessoaRepository.save(pessoa);
                
                // 4. Processar endereços se existirem
                if (servidorEfetivoDTO.getPessoa() != null && 
                    servidorEfetivoDTO.getPessoa().getEnderecos() != null) {
                    
                    for (EnderecoDTO enderecoDTO : servidorEfetivoDTO.getPessoa().getEnderecos()) {
                        // Criar e configurar o endereço
                        Endereco endereco = new Endereco();
                        endereco.setTipoLogradouro(enderecoDTO.getTipoLogradouro());
                        endereco.setLogradouro(enderecoDTO.getLogradouro());
                        endereco.setNumero(enderecoDTO.getNumero());
                        endereco.setBairro(enderecoDTO.getBairro());
                        
                        // Processar a cidade
                        if (enderecoDTO.getCidade() != null) {
                            Cidade cidade;
                            List<Cidade> cidades = cidadeRepository.findByNomeContainingIgnoreCase(
                                enderecoDTO.getCidade().getNome());
                            
                            if (!cidades.isEmpty()) {
                                cidade = cidades.get(0);
                            } else {
                                cidade = new Cidade();
                                cidade.setNome(enderecoDTO.getCidade().getNome());
                                cidade.setUf(enderecoDTO.getCidade().getUf());
                                cidade = cidadeRepository.save(cidade);
                            }
                            
                            endereco.setCidade(cidade);
                        }
                        
                        // Salvar o endereço
                        endereco = enderecoRepository.save(endereco);
                        
                        // Inserir na tabela de junção usando JDBC
                        servidorEfetivoRepository.adicionarEnderecoPessoa(pessoa.getId(), endereco.getId());
                    }
                }
          
                ServidorEfetivo servidorAtualizado = servidorEfetivoRepository.findById(pessoa.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Servidor não encontrado após salvar"));
                
                return servidorEfetivoMapper.toDto(servidorAtualizado);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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
        // Verificar se o servidor efetivo existe
        if (!servidorEfetivoRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("error.servidorEfetivo.notfound", 
                            new Object[]{id}, 
                            LocaleContextHolder.getLocale())
            );
        }
        
        try {
            // Usar o método personalizado do repository para excluir
            servidorEfetivoRepository.deleteServidorEfetivoById(id);
            
            // Log para confirmar a exclusão
            System.out.println("Servidor efetivo com ID " + id + " excluído com sucesso.");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir servidor efetivo: " + e.getMessage(), e);
        }
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
        Page<ServidorEfetivo> servidores = servidorEfetivoRepository.findByUnidadeAtual(unidadeId, pageable);
        
        // Converter para DTOs
        Page<ServidorEfetivoDTO> servidoresDTO = servidores.map(servidorEfetivoMapper::toDto);
        
        // Adicionar foto para cada servidor
        servidoresDTO.forEach(this::adicionarFotoPessoa);
        
        return servidoresDTO;
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
