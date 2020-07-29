package com.nelioalves.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.nelioalves.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${remetente.padrao}")
	private String remetente;
	
	@Override
	public void enviarEmailDeConfirmacaoDePedido(Pedido pedido) {
		SimpleMailMessage sm = prepararEmailDoPedido(pedido);
		enviarEmail(sm);
	}

	protected SimpleMailMessage prepararEmailDoPedido(Pedido pedido) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(pedido.getCliente().getEmail());
		sm.setFrom(remetente);
		sm.setSubject("Pedido Confirmado: " + pedido.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(pedido.toString());
		
		return sm;
	}
}
