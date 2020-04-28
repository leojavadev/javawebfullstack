package br.com.fullstack.cursofullstack.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fullstack.cursofullstack.domain.Cliente;
import br.com.fullstack.cursofullstack.repositories.ClienteRepository;
import br.com.fullstack.cursofullstack.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public List<Cliente> listarTodos(){
		List<Cliente> lista = repo.findAll();
		if(lista == null) {
			throw new ObjectNotFoundException("Nenhum cliente cadastrado! Tipo: " + Cliente.class.getName());
		}
		return lista;
	}
	
	public Cliente buscar(Integer id){
		Optional<Cliente> cliente = repo.findById(id);
		return cliente.orElseThrow(
				() -> new ObjectNotFoundException("Cliente não encontrado com o ID informado: ID = " + id + ", Tipo: " + Cliente.class.getName())
		);
	}

}
