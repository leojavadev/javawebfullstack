package br.com.fullstack.cursofullstack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fullstack.cursofullstack.domain.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Integer> {

}
