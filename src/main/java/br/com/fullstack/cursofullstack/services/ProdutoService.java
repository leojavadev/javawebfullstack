package br.com.fullstack.cursofullstack.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.fullstack.cursofullstack.domain.Categoria;
import br.com.fullstack.cursofullstack.domain.Produto;
import br.com.fullstack.cursofullstack.repositories.CategoriaRepository;
import br.com.fullstack.cursofullstack.repositories.ProdutoRepository;
import br.com.fullstack.cursofullstack.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
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
	
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String direction, String orderBy){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		//return repo.search(nome, categorias, pageRequest);
		//A linha acima, se descomentada, também funciona e faz a mesma coisa que a de baixo.
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
	}

}
