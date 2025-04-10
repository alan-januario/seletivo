package com.example.seletivo.model.repository;

import com.example.seletivo.model.entity.ServidorTemporario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ServidorTemporarioRepository extends JpaRepository<ServidorTemporario, Long> {
    
    // Corrigindo o método para usar JPQL com join para acessar o nome da pessoa
    @Query("SELECT st FROM ServidorTemporario st JOIN st.pessoa p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<ServidorTemporario> findByNomeContaining(@Param("nome") String nome, Pageable pageable);
    
    @Query("SELECT st FROM ServidorTemporario st WHERE st.dataDemissao IS NULL OR st.dataDemissao > CURRENT_DATE")
    Page<ServidorTemporario> findAtivos(Pageable pageable);
    
    @Modifying
    @Query(value = "INSERT INTO servidor_temporario (pes_id, st_data_admissao, st_data_demissao) VALUES (:pessoaId, :dataAdmissao, :dataDemissao)", nativeQuery = true)
    void inserirServidorTemporario(
        @Param("pessoaId") Long pessoaId, 
        @Param("dataAdmissao") LocalDate dataAdmissao, 
        @Param("dataDemissao") LocalDate dataDemissao
    );

    @Modifying
    @Query(value = "DELETE FROM servidor_temporario WHERE pes_id = :id", nativeQuery = true)
    void deleteServidorTemporarioById(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE servidor_temporario SET data_admissao = :dataAdmissao, data_demissao = :dataDemissao WHERE pes_id = :id", nativeQuery = true)
    void atualizarServidorTemporario(@Param("id") Long id, @Param("dataAdmissao") LocalDate dataAdmissao, @Param("dataDemissao") LocalDate dataDemissao);
}
