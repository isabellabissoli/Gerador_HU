package com.example.dev_web_gerador.Controller;

import com.example.dev_web_gerador.DTO.HistoriaUsuarioInputDTO;
import com.example.dev_web_gerador.DTO.TarefaInputDTO;
import com.example.dev_web_gerador.Model.*;
import com.example.dev_web_gerador.Repository.HistoriaUsuarioRepository;
import com.example.dev_web_gerador.Repository.TarefaRepository;
import com.example.dev_web_gerador.Repository.TipoHistoriaUsuarioRepository;
import com.example.dev_web_gerador.Repository.TipoTarefaRepository;
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
@RequestMapping("/api/tarefa")
public class TarefaController {

    @Autowired
    TipoTarefaRepository tipoTarefaRepository;

    @Autowired
    HistoriaUsuarioRepository historiaUsuarioRepository;

    @Autowired
    TipoHistoriaUsuarioRepository tipoHistoriaUsuarioRepository;
    @Autowired
    TarefaRepository tarefaRepository;


    @GetMapping
    public List<Tarefa> listarTarefa() {
        return tarefaRepository.findAll();
    }
    @PostMapping("/gerar/{historiaUsuario_id}")
    public ResponseEntity<List<Tarefa>> criarGerarTarefa(@PathVariable(value = "historiaUsuario_id") long historiaUsuario_id) {

        HistoriaUsuario historiaUsuario = historiaUsuarioRepository.findById(historiaUsuario_id).get();

        TipoHistoriaUsuario tipoHistoriaUsuario = tipoHistoriaUsuarioRepository.findById(historiaUsuario.getTipoHistoriaUsuario().getId()).get();
        long tipoHistoriaUsuarioId = tipoHistoriaUsuario.getId();

        if(historiaUsuario == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        List<TipoTarefa> tiposTarefa = tipoTarefaRepository.findAllByTipoHistoriaUsuarioId(tipoHistoriaUsuarioId);



        List<Tarefa> tarefas = new ArrayList<Tarefa>();
        tiposTarefa.forEach(tipoTarefa -> {
            String historiaUsuarioDescricao = historiaUsuario.getDescricao();
            String entidade = historiaUsuarioDescricao.substring(historiaUsuarioDescricao.lastIndexOf(" ") + 1);
            Tarefa salvarTarefa = criarTarefa(historiaUsuario, tipoHistoriaUsuario, entidade, tipoTarefa);
            tarefas.add(salvarTarefa);
        });
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefas);
    }

    private Tarefa criarTarefa(HistoriaUsuario historiaUsuario, TipoHistoriaUsuario tipoHistoriaUsuario, String entidade, TipoTarefa tipoTarefa) {
        Tarefa tarefa = new Tarefa();
        //HistoriaUsuario historiausuario = historiaUsuarioRepository.findById(tarefaInputDTO.HistoriaUsuarioId()).get();
        //TipoTarefa tipoTarefa = tipoHistoriaUsuario.getTipoTarefa();

        tarefa.setTitulo(tipoTarefa.getDescricao());
        tarefa.setDescricao(tipoHistoriaUsuario.getDescricao().concat(" ").concat(tipoTarefa.getDescricao()).concat(" de ").concat(entidade));
        tarefa.setHistoriaUsuario(historiaUsuario);
        tarefa.setTipoTarefa(tipoTarefa);

        return tarefaRepository.save(tarefa);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizarTarefa(
            @PathVariable long id,
            @RequestBody TarefaInputDTO tarefaInputDTO) {

        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);

        if (tarefaOptional.isPresent()) {

            Tarefa tarefa = tarefaOptional.get();

            Long tipoTarefa_id = tarefaInputDTO.tipoTarefa_id();
            Long historiaUsuarioId = tarefaInputDTO.historiaUsuario_id();

            Optional<TipoTarefa> tipoTarefaOptional = tipoTarefaRepository.findById(tipoTarefa_id);
            Optional<HistoriaUsuario> historiaUsuarioOptional = historiaUsuarioRepository.findById(historiaUsuarioId);

            if (tipoTarefaOptional.isPresent()) {
                if (historiaUsuarioOptional.isPresent()) {
                    tarefa.setTipoTarefa(tipoTarefaOptional.get());
                    tarefa.setHistoriaUsuario(historiaUsuarioOptional.get());
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            BeanUtils.copyProperties(tarefaInputDTO, tarefa);

            return ResponseEntity.status(HttpStatus.OK).body(tarefaRepository.save(tarefa));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarTarefa(@PathVariable(value = "id") long id) {
        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        if (tarefa.isEmpty()) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.TASK_NOT_FOUND.getCode());}

        tarefaRepository.delete(tarefa.get());
        return ResponseEntity.status(HttpStatus.OK).body(StatusCode.TASK_REMOVED.getCode());
    }
}
