package br.com.fullstack.cursofullstack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fullstack.cursofullstack.domain.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

}
