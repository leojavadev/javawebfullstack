package br.com.fullstack.cursofullstack.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.fullstack.cursofullstack.domain.Cliente;
import br.com.fullstack.cursofullstack.repositories.ClienteRepository;
import br.com.fullstack.cursofullstack.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EmailService emailService;
	
	private Random rand = new Random();
	
	public void sendNewPassword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email);
		if(cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado!");
		}
		
		String newPass = newPassword();
		cliente.setSenha(pe.encode(newPass));
		
		clienteRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
		
	}

	private String newPassword() {
		char[] vetor = new char[10];
		for(int i = 0; i < 10; i++) {
			vetor[i] = randomChar();
		}
		return new String(vetor);
	}

	private char randomChar() {
		int opt = rand.nextInt(3);
		if(opt == 0) { //gera um dígito
			return (char) (rand.nextInt(10) + 48);
		}
		else if(opt == 1) { //gera uma letra maiúscula
			return (char) (rand.nextInt(26) + 65);
		}
		else { //gera uma letra minúscula
			return (char) (rand.nextInt(26) + 97);
		}
	}

}
