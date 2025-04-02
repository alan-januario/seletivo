package com.example.seletivo.model.service;

import com.example.seletivo.exception.ResourceNotFoundException;
import com.example.seletivo.model.dto.LotacaoDTO;
import com.example.seletivo.model.entity.Lotacao;
import com.example.seletivo.model.entity.Pessoa;
import com.example.seletivo.model.entity.Unidade;
import com.example.seletivo.model.mapper.LotacaoMapper;
import com.example.seletivo.model.repository.LotacaoRepository;
import com.example.seletivo.model.repository.PessoaRepository;
import com.example.seletivo.model.repository.UnidadeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class LotacaoService {

                private final LotacaoRepository lotacaoRepository;
                private final PessoaRepository pessoaRepository;
                private final UnidadeRepository unidadeRepository;
                private final LotacaoMapper lotacaoMapper;
                private final MessageSource messageSource;

                public Page<LotacaoDTO> findAll(Pageable pageable) {
                    Pageable pageableWithSort = pageable;
                    if (!pageable.getSort().isSorted()) {
                        pageableWithSort = PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            Sort.by(Sort.Direction.DESC, "id")
                        );
                    }
                
                    return lotacaoRepository.findAll(pageableWithSort)
                            .map(lotacaoMapper::toDto);
                }

                public LotacaoDTO findById(Long id) {
                    return lotacaoRepository.findById(id)
                            .map(lotacaoMapper::toDto)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    messageSource.getMessage("error.lotacao.notfound", 
                                            new Object[]{id}, 
                                            LocaleContextHolder.getLocale())
                            ));
                }

                public Page<LotacaoDTO> findByPessoaId(Long pessoaId, Pageable pageable) {
                    Pageable pageableWithSort = pageable;
                    if (!pageable.getSort().isSorted()) {
                        pageableWithSort = PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            Sort.by(Sort.Direction.DESC, "id")
                        );
                    }
                
                    return lotacaoRepository.findByPessoaId(pessoaId, pageableWithSort)
                            .map(lotacaoMapper::toDto);
                }

                public Page<LotacaoDTO> findByUnidadeId(Long unidadeId, Pageable pageable) {
                    Pageable pageableWithSort = pageable;
                    if (!pageable.getSort().isSorted()) {
                        pageableWithSort = PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            Sort.by(Sort.Direction.DESC, "id")
                        );
                    }
                
                    return lotacaoRepository.findByUnidadeId(unidadeId, pageableWithSort)
                            .map(lotacaoMapper::toDto);
                }

                public Optional<LotacaoDTO> findLotacaoAtualByPessoaId(Long pessoaId) {
                    return lotacaoRepository.findLotacaoAtualByPessoaId(pessoaId)
                            .map(lotacaoMapper::toDto);
                }

                @Transactional
                public LotacaoDTO save(LotacaoDTO lotacaoDTO) {
                    Lotacao lotacao = lotacaoMapper.toEntity(lotacaoDTO);
                    lotacao = lotacaoRepository.save(lotacao);
                    return lotacaoMapper.toDto(lotacao);
                }

                @Transactional
                public LotacaoDTO update(Long id, LotacaoDTO lotacaoDTO) {
                    if (!lotacaoRepository.existsById(id)) {
                        throw new ResourceNotFoundException(
                                messageSource.getMessage("error.lotacao.notfound", 
                                        new Object[]{id}, 
                                        LocaleContextHolder.getLocale())
                        );
                    }
        
                    Lotacao lotacao = lotacaoMapper.toEntity(lotacaoDTO);
                    lotacao.setId(id);
                    lotacao = lotacaoRepository.save(lotacao);
                    return lotacaoMapper.toDto(lotacao);
                }

                @Transactional
                public void delete(Long id) {
                    if (!lotacaoRepository.existsById(id)) {
                        throw new ResourceNotFoundException(
                                messageSource.getMessage("error.lotacao.notfound", 
                                        new Object[]{id}, 
                                        LocaleContextHolder.getLocale())
                        );
                    }
                    lotacaoRepository.deleteById(id);
                }

                @Transactional
                public LotacaoDTO removerServidor(Long id, LocalDate dataRemocao) {
                    Lotacao lotacao = lotacaoRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    messageSource.getMessage("error.lotacao.notfound", 
                                            new Object[]{id}, 
                                            LocaleContextHolder.getLocale())
                            ));
        
                    lotacao.setDataRemocao(dataRemocao);
                    lotacao = lotacaoRepository.save(lotacao);
        
                    return lotacaoMapper.toDto(lotacao);
                }

                @Transactional
                public LotacaoDTO lotarServidor(Long pessoaId, Long unidadeId, LocalDate dataLotacao, String portaria) {
                    // Verificar se a pessoa existe
                    Pessoa pessoa = pessoaRepository.findById(pessoaId)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    messageSource.getMessage("error.pessoa.notfound", 
                                            new Object[]{pessoaId}, 
                                            LocaleContextHolder.getLocale())
                            ));
        
                    // Verificar se a unidade existe
                    Unidade unidade = unidadeRepository.findById(unidadeId)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    messageSource.getMessage("error.unidade.notfound", 
                                            new Object[]{unidadeId}, 
                                            LocaleContextHolder.getLocale())
                            ));
        
                    // Verificar se existe lotação atual e encerrar
                    Optional<Lotacao> lotacaoAtual = lotacaoRepository.findLotacaoAtualByPessoaId(pessoaId);
                    if (lotacaoAtual.isPresent()) {
                        Lotacao lotacao = lotacaoAtual.get();
                        lotacao.setDataRemocao(dataLotacao.minusDays(1));
                        lotacaoRepository.save(lotacao);
                    }
        
                    // Criar nova lotação
                    Lotacao novaLotacao = new Lotacao();
                    novaLotacao.setPessoa(pessoa);
                    novaLotacao.setUnidade(unidade);
                    novaLotacao.setDataLotacao(dataLotacao);
                    novaLotacao.setPortaria(portaria);
        
                    novaLotacao = lotacaoRepository.save(novaLotacao);
        
                    return lotacaoMapper.toDto(novaLotacao);
                }
}
