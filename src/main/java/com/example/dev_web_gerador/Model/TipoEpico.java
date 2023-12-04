package com.example.dev_web_gerador.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Table
@Data
public class TipoEpico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String descricao;

    @OneToMany(mappedBy = "tipo_epico", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TipoHistoriaUsuario> tipoHistoriaUsuario;

    @OneToMany(mappedBy = "tipoEpico", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Epico> epico;


}
