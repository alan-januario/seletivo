package com.example.seletivo.model.service;

import com.example.seletivo.exception.ResourceNotFoundException;
import com.example.seletivo.model.dto.CidadeDTO;
import com.example.seletivo.model.dto.EnderecoDTO;
import com.example.seletivo.model.dto.PessoaDTO;
import com.example.seletivo.model.dto.ServidorTemporarioDTO;
import com.example.seletivo.model.entity.ServidorTemporario;
import com.example.seletivo.model.mapper.ServidorTemporarioMapper;
import com.example.seletivo.model.repository.ServidorTemporarioRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

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
public class ServidorTemporarioService {

    private final ServidorTemporarioRepository servidorTemporarioRepository;
    private final ServidorTemporarioMapper servidorTemporarioMapper;
    private final MessageSource messageSource;
    private final PessoaService pessoaService;
    private final EnderecoService enderecoService;
    private final CidadeService cidadeService;
    
    public Page<ServidorTemporarioDTO> findAll(Pageable pageable) {
        Pageable pageableWithSort = pageable;
        if (!pageable.getSort().isSorted()) {
            pageableWithSort = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id")
            );
        }
        
        return servidorTemporarioRepository.findAll(pageableWithSort)
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
        try {
            // 1. Verificar se os dados necessários estão presentes
            if (servidorTemporarioDTO.getPessoa() == null) {
                throw new IllegalArgumentException("Dados da pessoa são obrigatórios");
            }
            
            // 2. Criar e salvar a pessoa usando PessoaService
            PessoaDTO pessoaDTO = new PessoaDTO();
            pessoaDTO.setNome(servidorTemporarioDTO.getPessoa().getNome());
            pessoaDTO.setDataNascimento(servidorTemporarioDTO.getPessoa().getDataNascimento());
            pessoaDTO.setSexo(servidorTemporarioDTO.getPessoa().getSexo());
            pessoaDTO.setMae(servidorTemporarioDTO.getPessoa().getMae());
            pessoaDTO.setPai(servidorTemporarioDTO.getPessoa().getPai());
            
            PessoaDTO pessoaSalva = pessoaService.save(pessoaDTO);
            
            // 3. Inserir o servidor temporário usando o repository em vez de JDBC direto
            servidorTemporarioRepository.inserirServidorTemporario(
                pessoaSalva.getId(),
                servidorTemporarioDTO.getDataAdmissao(),
                servidorTemporarioDTO.getDataDemissao()
            );
            
            // 4. Processar endereços se existirem
            if (servidorTemporarioDTO.getPessoa().getEnderecos() != null && 
                !servidorTemporarioDTO.getPessoa().getEnderecos().isEmpty()) {
                
                for (EnderecoDTO enderecoDTO : servidorTemporarioDTO.getPessoa().getEnderecos()) {
                    // Verificar se o endereço já existe (tem ID) e não é zero
                    if (enderecoDTO.getId() != null && enderecoDTO.getId() != 0) {
                        // Se já existe, apenas associa à pessoa
                        pessoaService.adicionarEndereco(pessoaSalva.getId(), enderecoDTO.getId());
                        continue;
                    }
                    
                    // Verificar se os dados obrigatórios do endereço estão presentes
                    if (enderecoDTO.getLogradouro() == null || enderecoDTO.getBairro() == null) {
                        throw new IllegalArgumentException("Logradouro e bairro são obrigatórios para o endereço");
                    }
                    
                    // Verificar se a cidade está presente
                    if (enderecoDTO.getCidade() == null || enderecoDTO.getCidade().getNome() == null) {
                        throw new IllegalArgumentException("Cidade é obrigatória para o endereço");
                    }
                    
                    CidadeDTO cidadeDTO;
                    // Verificar se a cidade já existe (tem ID) e não é zero
                    if (enderecoDTO.getCidade().getId() != null && enderecoDTO.getCidade().getId() != 0) {
                        cidadeDTO = enderecoDTO.getCidade();
                    } else {
                        // Buscar a cidade pelo nome usando o CidadeService
                        List<CidadeDTO> cidadesEncontradas = cidadeService.findByNome(enderecoDTO.getCidade().getNome());
                        
                        if (cidadesEncontradas.isEmpty()) {
                            // Se não encontrou a cidade, verificar se tem UF para criar uma nova
                            if (enderecoDTO.getCidade().getUf() == null) {
                                throw new IllegalArgumentException("UF é obrigatória para criar uma nova cidade");
                            }
                            
                            // Criar uma nova cidade
                            CidadeDTO novaCidadeDTO = new CidadeDTO();
                            novaCidadeDTO.setNome(enderecoDTO.getCidade().getNome());
                            novaCidadeDTO.setUf(enderecoDTO.getCidade().getUf());
                            
                            // Salvar a cidade
                            cidadeDTO = cidadeService.save(novaCidadeDTO);
                        } else {
                            cidadeDTO = cidadesEncontradas.get(0);
                        }
                    }
                    
                    // Associar a cidade ao endereço
                    enderecoDTO.setCidade(cidadeDTO);
                    
                    // Salvar o endereço usando EnderecoService
                    EnderecoDTO enderecoSalvo = enderecoService.save(enderecoDTO);
                    
                    // Usar o método adicionarEndereco do PessoaService para associar pessoa e endereço
                    pessoaService.adicionarEndereco(pessoaSalva.getId(), enderecoSalvo.getId());
                }
            }
            
            // 5. Construir o DTO de retorno manualmente
            ServidorTemporarioDTO result = new ServidorTemporarioDTO();
            result.setId(pessoaSalva.getId());
            result.setDataAdmissao(servidorTemporarioDTO.getDataAdmissao());
            result.setDataDemissao(servidorTemporarioDTO.getDataDemissao());
            result.setPessoa(pessoaSalva);
            
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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
        // Verificar se o servidor temporário existe
        if (!servidorTemporarioRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("error.servidorTemporario.notfound", 
                            new Object[]{id}, 
                            LocaleContextHolder.getLocale())
            );
        }
        
        try {
            // Usar o método personalizado do repository para excluir
            servidorTemporarioRepository.deleteServidorTemporarioById(id);
            
            // Log para confirmar a exclusão
            System.out.println("Servidor temporário com ID " + id + " excluído com sucesso.");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir servidor temporário: " + e.getMessage(), e);
        }
    }
    
    public Page<ServidorTemporarioDTO> findByNome(String nome, Pageable pageable) {
        Pageable pageableWithSort = pageable;
        if (!pageable.getSort().isSorted()) {
            pageableWithSort = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id")
            );
        }
        
        return servidorTemporarioRepository.findByNomeContaining(nome, pageableWithSort)
                .map(servidorTemporarioMapper::toDto);
    }
    
    public Page<ServidorTemporarioDTO> findAtivos(Pageable pageable) {
        Pageable pageableWithSort = pageable;
        if (!pageable.getSort().isSorted()) {
            pageableWithSort = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id")
            );
        }
        
        return servidorTemporarioRepository.findAtivos(pageableWithSort)
                .map(servidorTemporarioMapper::toDto);
    }
}
