package com.example.dev_web_gerador.DTO;

import jakarta.validation.constraints.NotBlank;

public record EpicoInputDTO(@NotBlank String titulo, @NotBlank String descricao, @NotBlank String relevancia, @NotBlank String categoria, @NotBlank long tipoEpico_id, long projeto_id, long epicoPai_id) {
}
