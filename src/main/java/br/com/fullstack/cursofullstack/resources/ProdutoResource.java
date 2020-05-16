package br.com.fullstack.cursofullstack.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fullstack.cursofullstack.domain.Produto;
import br.com.fullstack.cursofullstack.dto.ProdutoDTO;
import br.com.fullstack.cursofullstack.resources.utils.URL;
import br.com.fullstack.cursofullstack.services.ProdutoService;

@RestController
@RequestMapping(path = "/produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoService service;
	
	@RequestMapping(value = "/todos", method = RequestMethod.GET)
	public ResponseEntity<?> findAll(){
		List<Produto> obj = service.findAll();
		return ResponseEntity.ok().body(obj);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Produto> find(@PathVariable Integer id) {
		Produto obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(value = "nome", defaultValue = "") String nome,
			@RequestParam(value = "categorias", defaultValue = "") String categorias,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy
		)
	{ 
		String nomeDecoded = URL.decodeParam(nome);
		List<Integer> listaIds = URL.decodeIntList(categorias);
		Page<Produto> lista = service.search(nomeDecoded, listaIds, page, linesPerPage, direction, orderBy);
		Page<ProdutoDTO> listaDto = lista.map(obj -> new ProdutoDTO(obj));
		return ResponseEntity.ok().body(listaDto);
	}
}
