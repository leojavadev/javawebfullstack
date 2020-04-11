package br.com.fullstack.cursofullstack.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fullstack.cursofullstack.domain.Categoria;
import br.com.fullstack.cursofullstack.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public List<Categoria> listarTodos() {
		List<Categoria> obj = repo.findAll();
		return obj;
	}
	
	public Optional<Categoria> buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj;
	}

}
