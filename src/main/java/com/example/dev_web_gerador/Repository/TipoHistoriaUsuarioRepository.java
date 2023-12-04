package com.example.dev_web_gerador.Repository;

import com.example.dev_web_gerador.Model.TipoHistoriaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TipoHistoriaUsuarioRepository extends JpaRepository<TipoHistoriaUsuario, Long> {
    @Query("SELECT th FROM TipoHistoriaUsuario th WHERE th.tipo_epico.id = :epicoId")
    List<TipoHistoriaUsuario> findByTipoEpicoId(@Param("epicoId") Long epicoId);


}
