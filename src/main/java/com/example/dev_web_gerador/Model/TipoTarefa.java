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
public class TipoTarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String descricao;

    @ManyToOne
    //@NotBlank
    private TipoHistoriaUsuario tipoHistoriaUsuario;

    @OneToMany(mappedBy = "tipoTarefa", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Tarefa> tarefa;


    @ManyToOne
    private TipoTarefa tipoTarefaPai;

    @OneToMany(mappedBy = "tipoTarefaPai")
    @JsonIgnore
    private List<TipoTarefa> tipoTarefaDependente;

}
