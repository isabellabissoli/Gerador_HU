package com.example.dev_web_gerador.Repository;

import com.example.dev_web_gerador.Model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {


}