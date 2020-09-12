package br.com.fullstack.cursofullstack.services;

import org.springframework.mail.SimpleMailMessage;

import br.com.fullstack.cursofullstack.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

}
