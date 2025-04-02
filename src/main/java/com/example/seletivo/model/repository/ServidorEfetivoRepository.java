package com.example.seletivo.model.repository;

import com.example.seletivo.model.entity.ServidorEfetivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServidorEfetivoRepository extends JpaRepository<ServidorEfetivo, Long> {
    
    @Query("SELECT se FROM ServidorEfetivo se JOIN se.pessoa p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<ServidorEfetivo> findByNomeContaining(@Param("nome") String nome, Pageable pageable);
    
    @Modifying
    @Query(value = "INSERT INTO pessoa_endereco (pes_id, end_id) VALUES (:pessoaId, :enderecoId)", nativeQuery = true)
    void adicionarEnderecoPessoa(@Param("pessoaId") Long pessoaId, @Param("enderecoId") Long enderecoId);
    
    @Modifying
    @Query(value = "DELETE FROM servidor_efetivo WHERE pes_id = :id", nativeQuery = true)
    void deleteServidorEfetivoById(@Param("id") Long id);
    
   @Query("SELECT se FROM ServidorEfetivo se JOIN se.pessoa p JOIN p.lotacoes l " +
       "WHERE l.unidade.id = :unidadeId AND (l.dataRemocao IS NULL OR l.dataRemocao > CURRENT_DATE)")
    Page<ServidorEfetivo> findByUnidadeAtual(@Param("unidadeId") Long unidadeId, Pageable pageable);

}
