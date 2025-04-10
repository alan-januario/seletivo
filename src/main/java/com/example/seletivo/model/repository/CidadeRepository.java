package com.example.seletivo.model.repository;

import com.example.seletivo.model.entity.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {
    List<Cidade> findByUf(String uf);
    List<Cidade> findByNomeContainingIgnoreCase(String nome);
}
