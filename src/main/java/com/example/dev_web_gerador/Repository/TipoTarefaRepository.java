package com.example.dev_web_gerador.Repository;

import com.example.dev_web_gerador.Model.Tarefa;
import com.example.dev_web_gerador.Model.TipoTarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoTarefaRepository extends JpaRepository<TipoTarefa, Long> {
    @Query("SELECT tp FROM TipoTarefa tp WHERE tp.tipoHistoriaUsuario.id = :huId")
    List<TipoTarefa> findAllByTipoHistoriaUsuarioId(@Param("huId") Long huId);
}

