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
public class TipoHistoriaUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String descricao;


    @ManyToOne
    private TipoEpico tipo_epico;

    @OneToMany(mappedBy = "tipoHistoriaUsuario", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<HistoriaUsuario> historiaUsuario;

    @OneToMany(mappedBy = "tipoHistoriaUsuario", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TipoTarefa> tipoTarefa;

    @ManyToOne
    private TipoHistoriaUsuario tipoHistoriaUsuarioPai;

    @OneToMany(mappedBy = "tipoHistoriaUsuarioPai")
    @JsonIgnore
    private List<TipoHistoriaUsuario> tipoHistoriaUsuarioDependente;


}
