package com.example.seletivo.service;

import com.example.seletivo.exception.MinioException;
import com.example.seletivo.exception.ResourceNotFoundException;
import com.example.seletivo.model.dto.FotoPessoaDTO;
import com.example.seletivo.model.entity.FotoPessoa;
import com.example.seletivo.model.entity.Pessoa;
import com.example.seletivo.model.mapper.FotoPessoaMapper;
import com.example.seletivo.repository.FotoPessoaRepository;
import com.example.seletivo.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FotoPessoaService {

    private final FotoPessoaRepository fotoPessoaRepository;
    private final PessoaRepository pessoaRepository;
    private final FotoPessoaMapper fotoPessoaMapper;
    private final MinioService minioService;
    private final MessageSource messageSource;

    public Page<FotoPessoaDTO> findAll(Pageable pageable) {
        return fotoPessoaRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    public FotoPessoaDTO findById(Long id) {
        return fotoPessoaRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.fotoPessoa.notfound", 
                                new Object[]{id}, 
                                LocaleContextHolder.getLocale())
                ));
    }

    public Page<FotoPessoaDTO> findByPessoaId(Long pessoaId, Pageable pageable) {
        return fotoPessoaRepository.findByPessoaId(pessoaId, pageable)
                .map(this::mapToDTO);
    }

    @Transactional
    public FotoPessoaDTO uploadFoto(Long pessoaId, MultipartFile file) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.pessoa.notfound", 
                                new Object[]{pessoaId}, 
                                LocaleContextHolder.getLocale())
                ));
        
        try {
            // Gerar hash único para o arquivo
            String hash = UUID.randomUUID().toString();
            
            // Upload do arquivo para o MinIO
            minioService.uploadFile(hash, file);
            
            // Salvar metadados no banco de dados
            FotoPessoa fotoPessoa = new FotoPessoa();
            fotoPessoa.setPessoa(pessoa);
            fotoPessoa.setData(LocalDate.now());
            fotoPessoa.setBucket(minioService.getBucketName());
            fotoPessoa.setHash(hash);
            
            fotoPessoa = fotoPessoaRepository.save(fotoPessoa);
            
            return mapToDTO(fotoPessoa);
        } catch (Exception e) {
            throw new MinioException(
                    messageSource.getMessage("error.minio.upload", 
                            new Object[]{e.getMessage()}, 
                            LocaleContextHolder.getLocale()),
                    e);
        }
    }

    @Transactional
    public void delete(Long id) {
        FotoPessoa fotoPessoa = fotoPessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.fotoPessoa.notfound", 
                                new Object[]{id}, 
                                LocaleContextHolder.getLocale())
                ));
        
        try {
            // Excluir arquivo do MinIO
            minioService.deleteFile(fotoPessoa.getHash());
            
            // Excluir registro do banco de dados
            fotoPessoaRepository.delete(fotoPessoa);
        } catch (Exception e) {
            throw new MinioException(
                    messageSource.getMessage("error.minio.delete", 
                            new Object[]{e.getMessage()}, 
                            LocaleContextHolder.getLocale()),
                    e);
        }
    }
    
    private FotoPessoaDTO mapToDTO(FotoPessoa fotoPessoa) {
        FotoPessoaDTO dto = fotoPessoaMapper.toDto(fotoPessoa);
        
        try {
            // Gerar URL pré-assinada para acesso à foto
            String url = minioService.getPresignedUrl(fotoPessoa.getHash());
            dto.setUrl(url);
        } catch (Exception e) {
            throw new MinioException(
                    messageSource.getMessage("error.minio.download", 
                            new Object[]{e.getMessage()}, 
                            LocaleContextHolder.getLocale()),
                    e);
        }
        
        return dto;
    }
}
