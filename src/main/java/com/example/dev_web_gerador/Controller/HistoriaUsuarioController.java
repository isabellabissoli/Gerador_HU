package com.example.dev_web_gerador.Controller;


import com.example.dev_web_gerador.DTO.HistoriaUsuarioInputDTO;
import com.example.dev_web_gerador.Model.Epico;
import com.example.dev_web_gerador.Model.HistoriaUsuario;
import com.example.dev_web_gerador.Model.TipoHistoriaUsuario;
import com.example.dev_web_gerador.Repository.EpicoRepository;
import com.example.dev_web_gerador.Repository.HistoriaUsuarioRepository;
import com.example.dev_web_gerador.Repository.TipoHistoriaUsuarioRepository;
import com.example.dev_web_gerador.codes.StatusCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/historiaUsuario")
public class HistoriaUsuarioController {

    @Autowired
    EpicoRepository epicoRepository;
    @Autowired
    TipoHistoriaUsuarioRepository tipoHistoriaUsuarioRepository;
    @Autowired
    HistoriaUsuarioRepository historiaUsuarioRepository;

    @GetMapping
    public List<HistoriaUsuario> listarHistoriaUsuario() {
        return historiaUsuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> historiaUsuarioId(@PathVariable long id) {
        Optional<HistoriaUsuario> historiaUsuario = historiaUsuarioRepository.findById(id);

        return historiaUsuario.<ResponseEntity<Object>>map(historias ->
                ResponseEntity.status(HttpStatus.OK).body(historias)).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.US_NOT_FOUND.getCode()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoriaUsuario> atualizarHistoriaUsuario(
            @PathVariable(value = "id") long id,
            @RequestBody HistoriaUsuarioInputDTO historiaUsuarioInputDTO) {

        Optional<HistoriaUsuario> historiaUsuarioOptional = historiaUsuarioRepository.findById(id);

        if (historiaUsuarioOptional.isPresent()) {

            HistoriaUsuario historiaUsuario = historiaUsuarioOptional.get();

            Long epicoId = historiaUsuarioInputDTO.epico_id();
            Long tipoHistoriaUsuarioId = historiaUsuarioInputDTO.tipoHistoriaUsuario_id();

            Optional<Epico> epicoOptional = epicoRepository.findById(epicoId);
            Optional<TipoHistoriaUsuario> tipoHistoriaUsuarioOptional = tipoHistoriaUsuarioRepository.findById(tipoHistoriaUsuarioId);

            if (epicoOptional.isPresent()) {
                historiaUsuario.setEpico(epicoOptional.get());
                if(tipoHistoriaUsuarioOptional.isPresent()){
                    historiaUsuario.setTipoHistoriaUsuario(tipoHistoriaUsuarioOptional.get());
                } else{
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            BeanUtils.copyProperties(historiaUsuarioInputDTO, historiaUsuario);

            return ResponseEntity.status(HttpStatus.OK).body(historiaUsuarioRepository.save(historiaUsuario));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarHistoriaUsuario(@PathVariable(value = "id") long id) {
        Optional<HistoriaUsuario> historiaUsuario = historiaUsuarioRepository.findById(id);
        if (historiaUsuario.isEmpty()) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.US_NOT_FOUND.getCode());}

        historiaUsuarioRepository.delete(historiaUsuario.get());
        return ResponseEntity.status(HttpStatus.OK).body(StatusCode.US_REMOVED.getCode());
    }
}
