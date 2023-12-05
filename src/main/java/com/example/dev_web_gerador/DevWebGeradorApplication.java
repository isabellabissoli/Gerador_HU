package com.example.dev_web_gerador;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Gerador de Histórias de Usuário",
		version = "1.0",
		description = "Gera Automaticamente Historias Através de Épico. Valores de chaves estrangeiras que não serão " +
				"passadas deverão ser nulos"))
public class DevWebGeradorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevWebGeradorApplication.class, args);
	}

}
