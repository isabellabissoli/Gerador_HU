package com.example.dev_web_gerador.Controller;

import com.example.dev_web_gerador.DTO.EpicoInputDTO;
import com.example.dev_web_gerador.Model.*;
import com.example.dev_web_gerador.Repository.*;
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
@RequestMapping("/api/epico")
public class EpicoController {

    @Autowired
    TipoEpicoRepository tipoEpicoRepository;

    @Autowired
    EpicoRepository epicoRepository;

    @Autowired
    ProjetoRepository projetoRepository;

    @Autowired
    HistoriaUsuarioRepository historiaUsuarioRepository;

    @Autowired
    TipoHistoriaUsuarioRepository tipoHistoriaUsuarioRepository;


    @PostMapping
    public ResponseEntity<Epico> criarEpico(@RequestBody EpicoInputDTO epicoInputDTO) {
        var epico = new Epico();
        Long tipoEpicoId = epicoInputDTO.tipoEpico_id();

        Optional<TipoEpico> tipoEpicoOptional = tipoEpicoRepository.findById(tipoEpicoId);
        Optional<Epico> epicoOptional = epicoRepository.findById(epicoInputDTO.epicoPai_id());
        Optional<Projeto> projetoId = projetoRepository.findById(epicoInputDTO.projeto_id());

        if(epicoOptional.isPresent()){
            epico.setEpicoPai(epicoOptional.get());
        }

        if(projetoId.isPresent()){
            epico.setProjeto(projetoId.get());
        }


        epico.setTipoEpico(tipoEpicoOptional.get());

        BeanUtils.copyProperties(epicoInputDTO, epico);

        Epico epicoSalvo = epicoRepository.save(epico);
        gerarHistoriaUsuario(epicoSalvo.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(epicoSalvo);
    }


    public ResponseEntity<List<HistoriaUsuario>> gerarHistoriaUsuario(long epico_id) {
        Epico epico = epicoRepository.findById(epico_id).get(); //Prourando o epico pelo id passado
        Long tipoEpicoId = epico.getTipoEpico().getId(); //Armazenando o id


        List<TipoHistoriaUsuario> tipoHistoriaUsuario = tipoHistoriaUsuarioRepository.findByTipoEpicoId(tipoEpicoId);

        String epicoDescricao = epico.getDescricao(); //Armazenando a desccrição do épico para ser modificada e adicionada no hu
        List<HistoriaUsuario> historias = new ArrayList<HistoriaUsuario>();
        tipoHistoriaUsuario.forEach(tipo -> { //Pegando ccada historia retornada na lista
            String palavra = epicoDescricao.replaceAll("(?<=\\bdesejo\\s)\\w+", tipo.getDescricao()); //Substituindo a palavra desejo pelo verbo

            HistoriaUsuario historiaUsuario = salvarHistoriaUsuario(epico.getId(), epico, palavra, tipo.getId());

            historias.add(historiaUsuario);

        });

        return ResponseEntity.ok(historias);
    }


    private HistoriaUsuario salvarHistoriaUsuario(long epico_id,Epico epico, String descricao, long tipoHistoriaUsuario) {
        HistoriaUsuario historiaUsuario = new HistoriaUsuario();

        Optional<Epico> epicoOptional = epicoRepository.findById(epico.getId());
        Optional<TipoHistoriaUsuario> tipoHistoriaUsuarioOptional = tipoHistoriaUsuarioRepository.findById(tipoHistoriaUsuario);
        historiaUsuario.setCategoria(epico.getCategoria());
        historiaUsuario.setDescricao(descricao);
        historiaUsuario.setRelevancia(epico.getRelevancia());
        historiaUsuario.setTitulo(epico.getTitulo());
        historiaUsuario.setEpico(epicoOptional.get());
        historiaUsuario.setTipoHistoriaUsuario(tipoHistoriaUsuarioOptional.get());

        return historiaUsuarioRepository.save(historiaUsuario);
    }

    @GetMapping
    public ResponseEntity<List<Epico>> listarEpico() {
        return ResponseEntity.status(HttpStatus.OK).body(epicoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> epicoId(@PathVariable long id) {
        Optional<Epico> epico = epicoRepository.findById(id);

        return epico.<ResponseEntity<Object>>map(epicos ->
                ResponseEntity.status(HttpStatus.OK).body(epicos)).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.EPIC_NOT_FOUND.getCode()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Epico> atualizarEpico(
            @PathVariable long id,
            @RequestBody EpicoInputDTO epicoInputDTO) {

        Optional<Epico> epicoOptional = epicoRepository.findById(id);

        if (epicoOptional.isPresent()) {

            Epico epico = epicoOptional.get();

            Long tipoEpicoId = epicoInputDTO.tipoEpico_id();

            Optional<TipoEpico> tipoEpicoOptional = tipoEpicoRepository.findById(tipoEpicoId);
            Optional<Epico> epicoPai = epicoRepository.findById(epicoInputDTO.epicoPai_id());
            Optional<Projeto> projetoId = projetoRepository.findById(epicoInputDTO.projeto_id());

            if (tipoEpicoOptional.isPresent()) {
                epico.setTipoEpico(tipoEpicoOptional.get());

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if(epicoPai.isPresent()){
                epico.setEpicoPai(epicoPai.get());
            }

            if(projetoId.isPresent()){
                epico.setProjeto(projetoId.get());
            }

            BeanUtils.copyProperties(epicoInputDTO, epico);

            // Retorna a resposta com o tipo de história de usuário atualizado
            return ResponseEntity.status(HttpStatus.OK).body(epicoRepository.save(epico));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarEpico(@PathVariable(value = "id") long id) {
        Optional<Epico> epico = epicoRepository.findById(id);
        if (epico.isEmpty()) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.EPIC_NOT_FOUND.getCode());}

        epicoRepository.delete(epico.get());
        return ResponseEntity.status(HttpStatus.OK).body(StatusCode.EPIC_REMOVED.getCode());
    }
}