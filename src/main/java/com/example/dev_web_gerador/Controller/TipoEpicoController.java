package com.example.dev_web_gerador.Controller;

import com.example.dev_web_gerador.DTO.TipoEpicoInputDTO;
import com.example.dev_web_gerador.Model.TipoEpico;
import com.example.dev_web_gerador.Repository.TipoEpicoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.dev_web_gerador.codes.StatusCode;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/tipoEpico")
public class TipoEpicoController {

    @Autowired
    TipoEpicoRepository tipoEpicoRepository;

    @PostMapping
    public TipoEpico criarTipoEpico(@RequestBody TipoEpico tipoEpico) {

        return tipoEpicoRepository.save(tipoEpico);
    }


    @GetMapping
    public ResponseEntity<List<TipoEpico>> listarTipoEpico() {
        return ResponseEntity.status(HttpStatus.OK).body(tipoEpicoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> tipoEpicoId(@PathVariable long id) {
        Optional<TipoEpico> tipoEpico = tipoEpicoRepository.findById(id);

        return tipoEpico.<ResponseEntity<Object>>map(tipoEpicos ->
                ResponseEntity.status(HttpStatus.OK).body(tipoEpicos)).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.EPIC_TYPE_NOT_FOUND.getCode()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarTipoEpico(@PathVariable long id,
                                                     @RequestBody @Valid TipoEpicoInputDTO tipoEpicoInputDTO) {
        Optional<TipoEpico> tipoEpico = tipoEpicoRepository.findById(id);
        if (tipoEpico.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.EPIC_TYPE_NOT_FOUND.getCode());
        }

        var tiposEpico = tipoEpico.get();
        BeanUtils.copyProperties(tipoEpicoInputDTO, tiposEpico);

        return ResponseEntity.status(HttpStatus.OK).body(tipoEpicoRepository.save(tiposEpico));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarTipoEpico(@PathVariable(value = "id") long id) {
        Optional<TipoEpico> tipoEpico = tipoEpicoRepository.findById(id);
        if (tipoEpico.isEmpty()) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.EPIC_TYPE_NOT_FOUND.getCode());}

        tipoEpicoRepository.delete(tipoEpico.get());
        return ResponseEntity.status(HttpStatus.OK).body(StatusCode.EPIC_TYPE_REMOVED.getCode());
    }





}
