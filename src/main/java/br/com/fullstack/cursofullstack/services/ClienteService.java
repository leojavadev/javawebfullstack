package br.com.fullstack.cursofullstack.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.fullstack.cursofullstack.domain.Cliente;
import br.com.fullstack.cursofullstack.dto.ClienteDTO;
import br.com.fullstack.cursofullstack.repositories.ClienteRepository;
import br.com.fullstack.cursofullstack.services.exceptions.DataIntegrityException;
import br.com.fullstack.cursofullstack.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public List<Cliente> findAll(){
		List<Cliente> lista = repo.findAll();
		if(lista == null) {
			throw new ObjectNotFoundException("Nenhum cliente cadastrado! Tipo: " + Cliente.class.getName());
		}
		return lista;
	}
	
	public Cliente find(Integer id){
		Optional<Cliente> cliente = repo.findById(id);
		return cliente.orElseThrow(
				() -> new ObjectNotFoundException("Cliente não encontrado com o ID informado: ID = " + id + ", Tipo: " + Cliente.class.getName())
		);
	}
	
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Cliente fromDto(ClienteDTO obj) {
		return new Cliente(obj.getId(), obj.getNome(), obj.getEmail(), null, null);
	}
	
	public Cliente update(Cliente obj) {
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
				"Para apagar um cliente, é necessário excluir, primeiro,"
				+ " os seus pedidos, telefone e endereço"
			);
		}
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String direction, String orderBy) {
		PageRequest pageRequest = PageRequest.of(
			page, linesPerPage, Direction.valueOf(direction), orderBy
		);
		return repo.findAll(pageRequest);
	}
}
