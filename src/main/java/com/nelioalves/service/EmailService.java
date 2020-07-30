package com.nelioalves.service;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.nelioalves.domain.Cliente;
import com.nelioalves.domain.Pedido;

public interface EmailService {

	// versão em texto plano de envio de e-Mail
	void enviarEmailDeConfirmacaoDePedido(Pedido pedido);
	void enviarEmail(SimpleMailMessage msg);
	
	//versão HTML de envio de e-Mail
	void enviarEmailHtmlDeConfirmacaoDePedido(Pedido pedido);
	void enviarEmailHtml(MimeMessage msg);
	void enviarEmailComNovaSenha(Cliente cliente, String novaSenha);
 
}
