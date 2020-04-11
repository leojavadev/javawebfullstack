package br.com.fullstack.cursofullstack;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.fullstack.cursofullstack.domain.Categoria;
import br.com.fullstack.cursofullstack.repositories.CategoriaRepository;

@SpringBootApplication
public class CursoFullstackApplication implements CommandLineRunner {
	
	@Autowired
	private CategoriaRepository repo;
	
	public static void main(String[] args) {
		SpringApplication.run(CursoFullstackApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		repo.saveAll(Arrays.asList(cat1, cat2));
	}

}
