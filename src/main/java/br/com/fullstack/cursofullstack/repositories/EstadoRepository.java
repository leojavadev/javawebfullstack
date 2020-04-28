package br.com.fullstack.cursofullstack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fullstack.cursofullstack.domain.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {

}
