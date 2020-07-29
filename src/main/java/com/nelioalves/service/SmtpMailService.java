package com.nelioalves.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SmtpMailService extends AbstractEmailService {
	
	private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);

	@Autowired
	private MailSender mailSender;
	
	@Override
	public void enviarEmail(SimpleMailMessage msg) {
		LOG.info("Enviando e-Mail...");
		mailSender.send(msg);
		LOG.info("e-Mail enviado");
	}
}
