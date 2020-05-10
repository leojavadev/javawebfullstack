package br.com.fullstack.cursofullstack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fullstack.cursofullstack.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	@Transactional(readOnly = true)
	Cliente findByEmail(String email);

}
