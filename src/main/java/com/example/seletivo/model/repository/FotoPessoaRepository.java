package com.example.seletivo.model.repository;

import com.example.seletivo.model.entity.FotoPessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FotoPessoaRepository extends JpaRepository<FotoPessoa, Long> {
    
    Page<FotoPessoa> findByPessoaId(Long pessoaId, Pageable pageable);
    
    Optional<FotoPessoa> findTopByPessoaIdOrderByDataDesc(Long pessoaId);
}
