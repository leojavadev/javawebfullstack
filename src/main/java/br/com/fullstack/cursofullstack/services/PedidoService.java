package br.com.fullstack.cursofullstack.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fullstack.cursofullstack.domain.Pedido;
import br.com.fullstack.cursofullstack.repositories.PedidoRepository;
import br.com.fullstack.cursofullstack.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	public Optional<Pedido> buscar(Integer id) {
		Optional<Pedido> pedido = repo.findById(id);
		if(pedido == null) {
			throw new ObjectNotFoundException(
					"Pedido não encontrado. ID: " + id + " Tipo: " + Pedido.class.getName()
			);
		}
		return pedido;
	}
}
