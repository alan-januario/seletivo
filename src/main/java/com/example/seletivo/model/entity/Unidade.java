package com.example.seletivo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "unidade")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unid_id")
    private Long id;

    @Column(name = "unid_nome", nullable = false)
    private String nome;

    @Column(name = "unid_sigla", nullable = false)
    private String sigla;

    @ManyToMany
    @JoinTable(
            name = "unidade_endereco",
            joinColumns = @JoinColumn(name = "unid_id"),
            inverseJoinColumns = @JoinColumn(name = "end_id")
    )
    private Set<Endereco> enderecos = new HashSet<>();

    @OneToMany(mappedBy = "unidade")
    private Set<Lotacao> lotacoes = new HashSet<>();
}
