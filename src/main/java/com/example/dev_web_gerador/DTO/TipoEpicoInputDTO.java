package com.example.dev_web_gerador.DTO;

import jakarta.validation.constraints.NotBlank;

public record TipoEpicoInputDTO (@NotBlank String descricao){
}
