package com.example.dev_web_gerador.Controller;

import com.example.dev_web_gerador.DTO.ProjetoInputDTO;
import com.example.dev_web_gerador.Model.Projeto;
import com.example.dev_web_gerador.Repository.ProjetoRepository;
import com.example.dev_web_gerador.codes.StatusCode;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {

    @Autowired
    ProjetoRepository projetoRepository;

    @GetMapping
    public List<Projeto> listarProjeto() {
        return projetoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Object> criarProject(@RequestBody @Valid ProjetoInputDTO projetoInputDTO) {
        var projeto = new Projeto();
        BeanUtils.copyProperties(projetoInputDTO, projeto);

        return ResponseEntity.status(HttpStatus.CREATED).body(projetoRepository.save(projeto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> projetoId(@PathVariable long id) {
        Optional<Projeto> projeto = projetoRepository.findById(id);

        return projeto.<ResponseEntity<Object>>map(tipoEpicos ->
                ResponseEntity.status(HttpStatus.OK).body(tipoEpicos)).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.PROJECT_NOT_FOUND.getCode()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarProjeto(@PathVariable long id,
                                                     @RequestBody @Valid ProjetoInputDTO projetoInputDTO) {
        Optional<Projeto> projeto = projetoRepository.findById(id);
        if (projeto.isEmpty()) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.PROJECT_NOT_FOUND.getCode());}

        var projetos = projeto.get();
        BeanUtils.copyProperties(projetoInputDTO, projetos);

        return ResponseEntity.status(HttpStatus.OK).body(projetoRepository.save(projetos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarProjeto(@PathVariable(value = "id") long id) {
        Optional<Projeto> projeto = projetoRepository.findById(id);
        if (projeto.isEmpty()) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.PROJECT_NOT_FOUND.getCode());}

        projetoRepository.delete(projeto.get());
        return ResponseEntity.status(HttpStatus.OK).body(StatusCode.PROJECT_REMOVED.getCode());
    }


}
