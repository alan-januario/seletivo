package com.example.seletivo.repository;

import com.example.seletivo.model.entity.Unidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {
    
    @Query("SELECT u FROM Unidade u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Unidade> findByNomeContaining(@Param("nome") String nome, Pageable pageable);
    
    @Query("SELECT u FROM Unidade u WHERE LOWER(u.sigla) LIKE LOWER(CONCAT('%', :sigla, '%'))")
    Page<Unidade> findBySiglaContaining(@Param("sigla") String sigla, Pageable pageable);
}
