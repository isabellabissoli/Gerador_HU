package com.example.dev_web_gerador.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Table
@Data
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String titulo;

    private String descricao;

    @ManyToOne
    private TipoTarefa tipoTarefa;

    @ManyToOne
    private HistoriaUsuario historiaUsuario;


    @ManyToOne
    private Tarefa tarefaPai;

    @OneToMany(mappedBy = "tarefaPai")
    @JsonIgnore
    private List<Tarefa> tarefaDependente;


}
