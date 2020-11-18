package br.com.fullstack.cursofullstack.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fullstack.cursofullstack.domain.Cidade;
import br.com.fullstack.cursofullstack.repositories.CidadeRepository;
import br.com.fullstack.cursofullstack.services.exceptions.ObjectNotFoundException;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository repo;
	
	public Cidade find(Integer id) {
		Optional<Cidade> cidade = repo.findById(id);
		return cidade.orElseThrow(
			() -> new ObjectNotFoundException("Cidade não encontrada com o ID informado. ID " + id
				+ ", Tipo: " + Cidade.class.getName())
		);
	}
	
	public List<Cidade> findByEstado(Integer estadoId){
		return repo.findCidades(estadoId);
	}
}
