package com.example.seletivo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "endereco")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "end_id")
    private Long id;

    @Column(name = "end_tipo_logradouro")
    private String tipoLogradouro;

    @Column(name = "end_logradouro", nullable = false)
    private String logradouro;

    @Column(name = "end_numero")
    private String numero;

    @Column(name = "end_bairro", nullable = false)
    private String bairro;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "pes_id")
    private Pessoa pessoa;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cid_id")
    private Cidade cidade;

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
}
