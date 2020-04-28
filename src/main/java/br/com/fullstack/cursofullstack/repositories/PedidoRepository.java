package br.com.fullstack.cursofullstack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fullstack.cursofullstack.domain.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

}
