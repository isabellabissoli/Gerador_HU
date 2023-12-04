package com.example.dev_web_gerador.Repository;

import com.example.dev_web_gerador.Model.Tarefa;
import com.example.dev_web_gerador.Model.TipoHistoriaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

}
