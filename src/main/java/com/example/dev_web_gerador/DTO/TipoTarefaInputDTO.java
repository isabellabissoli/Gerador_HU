package com.example.dev_web_gerador.DTO;

import jakarta.validation.constraints.NotBlank;

public record TipoTarefaInputDTO(@NotBlank String descricao, @NotBlank long tipoHistoriaUsuario_id, long tipoTarefaPai_id) {
}
