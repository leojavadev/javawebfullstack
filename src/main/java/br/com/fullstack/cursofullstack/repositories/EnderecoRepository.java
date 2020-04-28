package br.com.fullstack.cursofullstack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fullstack.cursofullstack.domain.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

}
