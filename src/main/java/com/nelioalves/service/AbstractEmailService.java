package com.nelioalves.service;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.nelioalves.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${remetente.padrao}")
	private String remetente;

	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
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
	
	protected String templateHtmlDePedido(Pedido pedido) {
		Context context = new Context();
		context.setVariable("pedido", pedido);
		return templateEngine.process("email/confirmacaoPedido", context);
	}
	
	@Override
	public void enviarEmailHtmlDeConfirmacaoDePedido(Pedido pedido) {
		try {
			MimeMessage mm = prepararMimeMessageDoPedido(pedido);
			enviarEmailHtml(mm);
		} catch (MessagingException e) {
			enviarEmailDeConfirmacaoDePedido(pedido);
		}
	}

	protected MimeMessage prepararMimeMessageDoPedido(Pedido pedido) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
		mmh.setTo(pedido.getCliente().getEmail());
		mmh.setFrom(remetente);
		mmh.setSubject("Pedido Confirmado. Código: " + pedido.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(templateHtmlDePedido(pedido), true);
		
		return mimeMessage;
	}
}
