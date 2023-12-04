package com.example.dev_web_gerador.DTO;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record TipoHistoriaUsuarioInputDTO(@NotBlank String descricao, @NotBlank long tipo_epico, long tipoHistoriaUsuarioPai_id) {

}
