package com.nelioalves.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nelioalves.service.BDService;
import com.nelioalves.service.EmailService;
import com.nelioalves.service.MockEmailService;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private BDService bdService;
	
	@Bean
	public boolean instanciarBD() throws ParseException {
		bdService.instanciarBDTeste();
		
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
}
