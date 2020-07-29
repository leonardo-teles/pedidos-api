package com.nelioalves.service;

import org.springframework.mail.SimpleMailMessage;

import com.nelioalves.domain.Pedido;

public interface EmailService {

	void enviarEmailDeConfirmacaoDePedido(Pedido pedido);
	
	void enviarEmail(SimpleMailMessage msg);
}
