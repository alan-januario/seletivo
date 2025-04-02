package com.example.seletivo.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "servidor_efetivo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServidorEfetivo {

    @Id
    @Column(name = "pes_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "pes_id")
    @JsonIgnoreProperties("servidorEfetivo")
    private Pessoa pessoa;

    @Column(name = "se_matricula", nullable = false)
    private String matricula;
    
    @Override
    public String toString() {
        return "ServidorEfetivo{" +
                "id=" + id +
                ", matricula='" + matricula + '\'' +
                '}';
    }
}