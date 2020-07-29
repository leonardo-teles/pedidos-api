package com.nelioalves.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstractEmailService {

	private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);
	
	@Override
	public void enviarEmail(SimpleMailMessage msg) {
		LOG.info("Simulando envio de e-Mail...");
		LOG.info(msg.toString());
		LOG.info("e-Mail enviado");
		
	}

}
