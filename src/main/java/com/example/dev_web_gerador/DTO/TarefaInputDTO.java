package com.example.dev_web_gerador.DTO;

import jakarta.validation.constraints.NotBlank;

public record TarefaInputDTO(@NotBlank String titulo,@NotBlank String descricao,@NotBlank long tipoTarefa_id ,@NotBlank long historiaUsuario_id, long tarefaPai) { }