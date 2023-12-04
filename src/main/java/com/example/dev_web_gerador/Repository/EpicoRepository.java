package com.example.dev_web_gerador.Repository;

import com.example.dev_web_gerador.Model.Epico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpicoRepository extends JpaRepository<Epico, Long> { }