package com.example.dev_web_gerador.Controller;

import com.example.dev_web_gerador.DTO.TipoTarefaInputDTO;
import com.example.dev_web_gerador.Model.TipoHistoriaUsuario;
import com.example.dev_web_gerador.Model.TipoTarefa;
import com.example.dev_web_gerador.Repository.TipoHistoriaUsuarioRepository;
import com.example.dev_web_gerador.Repository.TipoTarefaRepository;
import com.example.dev_web_gerador.codes.StatusCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tipoTarefa")
public class TipoTarefaController {

    @Autowired
    TipoTarefaRepository tipoTarefaRepository;
    @Autowired
    TipoHistoriaUsuarioRepository tipoHistoriaUsuarioRepository;
    @PostMapping
    public TipoTarefa criarTipoTarefa(@RequestBody TipoTarefaInputDTO tipoTarefaInputDTO) {
        var tipoTarefa = new TipoTarefa();
        Long tipoHistoriaUsuarioId = tipoTarefaInputDTO.tipoHistoriaUsuario_id();

        Optional<TipoHistoriaUsuario> tipoHistoriaUsuarioOptional = tipoHistoriaUsuarioRepository.findById(tipoHistoriaUsuarioId);
        Optional<TipoTarefa> tipoTarefaPai = tipoTarefaRepository.findById(tipoTarefaInputDTO.tipoTarefaPai_id());

        if(tipoTarefaPai.isPresent()){
            tipoTarefa.setTipoTarefaPai(tipoTarefaPai.get());
        }


        tipoTarefa.setTipoHistoriaUsuario(tipoHistoriaUsuarioOptional.get());
        BeanUtils.copyProperties(tipoTarefaInputDTO, tipoTarefa);

        return tipoTarefaRepository.save(tipoTarefa);
    }

    @GetMapping
    public ResponseEntity<List<TipoTarefa>> listarTipoTarefa() {
        return ResponseEntity.status(HttpStatus.OK).body(tipoTarefaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> TipoTarefaId(@PathVariable long id) {
        Optional<TipoTarefa> tipoTarefa = tipoTarefaRepository.findById(id);

        return tipoTarefa.<ResponseEntity<Object>>map(tipoTarefas ->
                ResponseEntity.status(HttpStatus.OK).body(tipoTarefas)).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.TASK_TYPE_NOT_FOUND.getCode()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoTarefa> atualizarTipoTarefa(
            @PathVariable long id,
            @RequestBody TipoTarefaInputDTO tipoTarefaInputDTO) {


        Optional<TipoTarefa> tipoTarefaOptional = tipoTarefaRepository.findById(id);
        Optional<TipoTarefa> tipoTarefaPai = tipoTarefaRepository.findById(tipoTarefaInputDTO.tipoTarefaPai_id());

        if (tipoTarefaOptional.isPresent()) {

            TipoTarefa tipoTarefa = tipoTarefaOptional.get();

            Long tipoHistoriaUsuarioId = tipoTarefaInputDTO.tipoHistoriaUsuario_id();


            Optional<TipoHistoriaUsuario> tipoHistoriaUsuarioOptional = tipoHistoriaUsuarioRepository.findById(tipoHistoriaUsuarioId);

            // Verifica se o tipo épico existe
            if (tipoHistoriaUsuarioOptional.isPresent()) {
                tipoTarefa.setTipoHistoriaUsuario(tipoHistoriaUsuarioOptional.get());

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }



            if(tipoTarefaPai.isPresent()){
                tipoTarefa.setTipoTarefaPai(tipoTarefaPai.get());
            }

            BeanUtils.copyProperties(tipoTarefaInputDTO, tipoTarefa);

            TipoTarefa tipoTarefaAtualizada = tipoTarefaRepository.save(tipoTarefa);

            return new ResponseEntity<>(tipoTarefaAtualizada, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarTipoTarefa(@PathVariable(value = "id") long id) {
        Optional<TipoTarefa> tipoTarefa = tipoTarefaRepository.findById(id);
        if (tipoTarefa.isEmpty()) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.TASK_TYPE_NOT_FOUND.getCode());}

        tipoTarefaRepository.delete(tipoTarefa.get());

        return ResponseEntity.status(HttpStatus.OK).body(StatusCode.TASK_TYPE_REMOVED.getCode());
    }


}
