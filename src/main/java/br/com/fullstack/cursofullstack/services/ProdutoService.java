package br.com.fullstack.cursofullstack.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fullstack.cursofullstack.domain.Categoria;
import br.com.fullstack.cursofullstack.domain.Produto;
import br.com.fullstack.cursofullstack.repositories.ProdutoRepository;
import br.com.fullstack.cursofullstack.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repo;
	
	public List<Produto> findAll(){
		List<Produto> obj = repo.findAll();
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Tipo: " + Produto.class.getName());
		}
		return obj;
	}
	
	public Produto findById(Integer id){
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(
				() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id + " Tipo: " + Categoria.class.getName())
		);
	}

}
