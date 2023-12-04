package com.example.dev_web_gerador.DTO;

import jakarta.validation.constraints.NotBlank;

public record HistoriaUsuarioInputDTO (@NotBlank String titulo,@NotBlank String descricao,@NotBlank String relevancia,@NotBlank String categoria,@NotBlank long epico_id,@NotBlank long tipoHistoriaUsuario_id, long historiaUsuarioPai_id){
}
