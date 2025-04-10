package com.example.seletivo.model.repository;

import com.example.seletivo.model.entity.Lotacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LotacaoRepository extends JpaRepository<Lotacao, Long> {
    
    Page<Lotacao> findByPessoaId(Long pessoaId, Pageable pageable);
    
    Page<Lotacao> findByUnidadeId(Long unidadeId, Pageable pageable);
    
    @Query("SELECT l FROM Lotacao l WHERE l.pessoa.id = :pessoaId AND (l.dataRemocao IS NULL OR l.dataRemocao > CURRENT_DATE)")
    Optional<Lotacao> findLotacaoAtualByPessoaId(@Param("pessoaId") Long pessoaId);
}
