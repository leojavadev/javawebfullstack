package br.com.fullstack.cursofullstack.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fullstack.cursofullstack.domain.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {
	
	@Transactional(readOnly = true)
	public List<Estado> findAllByOrderByNome();

}
