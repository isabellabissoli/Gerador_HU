package com.example.dev_web_gerador.Controller;

import com.example.dev_web_gerador.DTO.TipoHistoriaUsuarioInputDTO;
import com.example.dev_web_gerador.Model.TipoEpico;
import com.example.dev_web_gerador.Model.TipoHistoriaUsuario;
import com.example.dev_web_gerador.Repository.TipoEpicoRepository;
import com.example.dev_web_gerador.Repository.TipoHistoriaUsuarioRepository;
import com.example.dev_web_gerador.codes.StatusCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tipoHistoriaUsuario")
public class TipoHistoriaUsuarioController {

    @Autowired
    TipoHistoriaUsuarioRepository tipoHistoriaUsuarioRepository;

    @Autowired
    TipoEpicoRepository tipoEpicoRepository;
    @PostMapping
    public TipoHistoriaUsuario criarTipoHistoriaUsuario(@RequestBody TipoHistoriaUsuarioInputDTO tipoHistoriaUsuarioInputDTO) {
        var tipoHistoriaUsuario = new TipoHistoriaUsuario();
        Long tipoEpicoId = tipoHistoriaUsuarioInputDTO.tipo_epico();


        Optional<TipoEpico> tipoEpicoOptional = tipoEpicoRepository.findById(tipoEpicoId);
        Optional<TipoHistoriaUsuario> tipoHistoriaUsuarioPai = tipoHistoriaUsuarioRepository.findById(tipoHistoriaUsuarioInputDTO.tipoHistoriaUsuarioPai_id());

//        if(!tipoEpicoOptional.isPresent()){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
        if(tipoHistoriaUsuarioPai.isPresent()){
            tipoHistoriaUsuario.setTipoHistoriaUsuarioPai(tipoHistoriaUsuarioPai.get());
        }

        //Mandando valor a chave estrangeira
        tipoHistoriaUsuario.setTipo_epico(tipoEpicoOptional.get());
        BeanUtils.copyProperties(tipoHistoriaUsuarioInputDTO, tipoHistoriaUsuario);

        return tipoHistoriaUsuarioRepository.save(tipoHistoriaUsuario);
    }

    @GetMapping
    public ResponseEntity<List<TipoHistoriaUsuario>> listarTipoHistoriaUsuario() {
        return ResponseEntity.status(HttpStatus.OK).body(tipoHistoriaUsuarioRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> TipoHistoriaUsuarioId(@PathVariable long id) {
        Optional<TipoHistoriaUsuario> tipoHistoriaUsuario = tipoHistoriaUsuarioRepository.findById(id);

        return tipoHistoriaUsuario.<ResponseEntity<Object>>map(tipoHistoriaUsuarios ->
                ResponseEntity.status(HttpStatus.OK).body(tipoHistoriaUsuarios)).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.US_TYPE_NOT_FOUND.getCode()));
    }


    @PutMapping("/{id}")
    public ResponseEntity<TipoHistoriaUsuario> atualizarTipoHistoriaUsuario(
            @PathVariable long id,
            @RequestBody TipoHistoriaUsuarioInputDTO tipoHistoriaUsuarioInputDTO) {

        Optional<TipoHistoriaUsuario> tipoHistoriaUsuarioOptional = tipoHistoriaUsuarioRepository.findById(id);
        Optional<TipoHistoriaUsuario> tipoHistoriaUsuarioPai = tipoHistoriaUsuarioRepository.findById(tipoHistoriaUsuarioInputDTO.tipoHistoriaUsuarioPai_id());

        if (tipoHistoriaUsuarioOptional.isPresent()) {

            TipoHistoriaUsuario tipoHistoriaUsuario = tipoHistoriaUsuarioOptional.get();

            Long tipoEpicoId = tipoHistoriaUsuarioInputDTO.tipo_epico();

            Optional<TipoEpico> tipoEpicoOptional = tipoEpicoRepository.findById(tipoEpicoId);

            if (tipoEpicoOptional.isPresent()) {
                tipoHistoriaUsuario.setTipo_epico(tipoEpicoOptional.get());

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            //Verifica se o id de dependencia existe
            if(tipoHistoriaUsuarioPai.isPresent()){
                tipoHistoriaUsuario.setTipoHistoriaUsuarioPai(tipoHistoriaUsuarioPai.get());
            }

            BeanUtils.copyProperties(tipoHistoriaUsuarioInputDTO, tipoHistoriaUsuario);

            TipoHistoriaUsuario tipoHistoriaUsuarioAtualizado = tipoHistoriaUsuarioRepository.save(tipoHistoriaUsuario);

            return new ResponseEntity<>(tipoHistoriaUsuarioAtualizado, HttpStatus.OK);

        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarTipoHistoriaUsuario(@PathVariable(value = "id") long id) {
        Optional<TipoHistoriaUsuario> tipoHistoriaUsuario = tipoHistoriaUsuarioRepository.findById(id);
        if (tipoHistoriaUsuario.isEmpty()) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.EPIC_TYPE_NOT_FOUND.getCode());}

        tipoHistoriaUsuarioRepository.delete(tipoHistoriaUsuario.get());

        return ResponseEntity.status(HttpStatus.OK).body(StatusCode.US_TYPE_REMOVED.getCode());
    }

}
