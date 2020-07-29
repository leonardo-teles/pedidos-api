package com.nelioalves.service;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SmtpMailService extends AbstractEmailService {
	
	private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);

	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Override
	public void enviarEmail(SimpleMailMessage msg) {
		LOG.info("Enviando e-Mail...");
		mailSender.send(msg);
		LOG.info("e-Mail enviado");
	}

	@Override
	public void enviarEmailHtml(MimeMessage msg) {
		LOG.info("Enviando e-Mail HTML...");
		javaMailSender.send(msg);
		LOG.info("e-Mail HTML enviado");
	}
}
