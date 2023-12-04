package com.example.dev_web_gerador.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table
@Data
public class Epico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descricao;

    private String relevancia;

    private String categoria;

    @OneToMany(mappedBy = "epico", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<HistoriaUsuario> historiaUsuario;

    @ManyToOne
    private TipoEpico tipoEpico;

    @ManyToOne
    private Projeto projeto;

    @ManyToOne
    private Epico epicoPai;

    @OneToMany(mappedBy = "epicoPai")
    @JsonIgnore
    private List<Epico> epicoDependente;

//    @ManyToMany(mappedBy = "epicoDependente")
//    @JsonIgnoreProperties("epicoDependente")
//    private List<Epico> epicoDependente;

}
