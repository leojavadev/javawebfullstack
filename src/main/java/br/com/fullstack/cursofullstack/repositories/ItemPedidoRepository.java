package br.com.fullstack.cursofullstack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fullstack.cursofullstack.domain.ItemPedido;
import br.com.fullstack.cursofullstack.domain.ItemPedidoPK;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, ItemPedidoPK> {

}
