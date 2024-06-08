package com.example.firstprogramInspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Library Management system",description = "spring boot and server side project",contact = @Contact(email = "akshaya.k@nilaapps.co.in",name = "Akshaya K")))
public class FirstprojectzInSpringbootApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(FirstprojectzInSpringbootApplication.class, args);
		
	}
}
