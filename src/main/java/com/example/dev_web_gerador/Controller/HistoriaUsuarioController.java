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

    @PostMapping("/gerar/{epico_id}")
    public ResponseEntity<List<HistoriaUsuario>> gerarHistoriaUsuario(@PathVariable(value = "epico_id") long epico_id) {
        Epico epico = epicoRepository.findById(epico_id).get(); //Prourando o epico pelo id passado
        Long tipoEpicoId = epico.getTipoEpico().getId(); //Armazenando o id

        if(tipoEpicoId == null){ //Verificação se o id existe
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<TipoHistoriaUsuario> tipoHistoriaUsuario = tipoHistoriaUsuarioRepository.findByTipoEpicoId(tipoEpicoId);

        String epicoDescricao = epico.getDescricao(); //Armazenando a desccrição do épico para ser modificada e adicionada no hu
        List<HistoriaUsuario> historias = new ArrayList<HistoriaUsuario>();
        tipoHistoriaUsuario.forEach(tipo -> { //Pegando ccada historia retornada na lista
            String palavra = epicoDescricao.replaceAll("(?<=\\bdesejo\\s)\\w+", tipo.getDescricao()); //Substituindo a palavra desejo pelo verbo

                HistoriaUsuario historiaUsuario = salvarHistoriaUsuario(epico.getId(), epico, palavra, tipo.getId());

                historias.add(historiaUsuario);

        });

        return ResponseEntity.status(HttpStatus.CREATED).body(historias);
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
