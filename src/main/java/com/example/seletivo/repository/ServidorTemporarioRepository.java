package com.example.seletivo.repository;

import com.example.seletivo.model.entity.ServidorTemporario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServidorTemporarioRepository extends JpaRepository<ServidorTemporario, Long> {
    
    @Query("SELECT st FROM ServidorTemporario st JOIN st.pessoa p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<ServidorTemporario> findByNomeContaining(@Param("nome") String nome, Pageable pageable);
    
    @Query("SELECT st FROM ServidorTemporario st WHERE st.dataDemissao IS NULL")
    Page<ServidorTemporario> findAtivos(Pageable pageable);
}
