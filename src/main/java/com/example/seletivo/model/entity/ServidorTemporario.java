package com.example.seletivo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "servidor_temporario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServidorTemporario {

    @Id
    @Column(name = "pes_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "pes_id")
    private Pessoa pessoa;

    @Column(name = "st_data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    @Column(name = "st_data_demissao")
    private LocalDate dataDemissao;
}
