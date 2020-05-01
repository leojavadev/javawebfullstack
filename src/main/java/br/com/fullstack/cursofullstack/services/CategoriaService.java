package br.com.fullstack.cursofullstack.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.fullstack.cursofullstack.domain.Categoria;
import br.com.fullstack.cursofullstack.dto.CategoriaDTO;
import br.com.fullstack.cursofullstack.repositories.CategoriaRepository;
import br.com.fullstack.cursofullstack.services.exceptions.DataIntegrityException;
import br.com.fullstack.cursofullstack.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public List<Categoria> findAll() {
		List<Categoria> obj = repo.findAll();
		if(obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Tipo: " + Categoria.class.getName()
			);
		}
		return obj;
	}
	
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
	 	return obj.orElseThrow(
				() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id + " Tipo: " + Categoria.class.getName()) 
		);
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		find(obj.getId());
		return repo.save(obj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException(
				"Não é permitido excluir uma categoria que contenha produtos cadastrados. "
				+ "Exclua os produtos associados antes de deletar esta categoria!"
			);
		}
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String direction, String orderBy){
		PageRequest pageRequest = PageRequest.of(
			page, linesPerPage, Direction.valueOf(direction), orderBy
		);
		return repo.findAll(pageRequest);
	}
	
	public Categoria fromDto(CategoriaDTO obj) {
		return new Categoria(obj.getId(), obj.getNome());
	}
}
