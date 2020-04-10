package br.com.fullstack.cursofullstack.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/teste")
public class CategoriaResource {
	
	@RequestMapping(method = RequestMethod.GET)
	public String teste() {
		return "O teste funcionou!";
	}

}
