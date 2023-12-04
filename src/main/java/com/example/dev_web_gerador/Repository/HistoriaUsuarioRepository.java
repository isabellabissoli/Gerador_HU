package com.example.dev_web_gerador.Repository;

import com.example.dev_web_gerador.Model.HistoriaUsuario;
import com.example.dev_web_gerador.Model.TipoHistoriaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import java.util.List;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoriaUsuarioRepository extends JpaRepository<HistoriaUsuario, Long> {
//    List<TipoHistoriaUsuario> findAllByTipo_epico(Long)

}
