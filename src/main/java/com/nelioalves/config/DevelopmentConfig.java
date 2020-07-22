package com.nelioalves.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nelioalves.service.BDService;

@Configuration
@Profile("dev")
public class DevelopmentConfig {

	@Autowired
	private BDService bdService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String estrategia;
	
	@Bean
	public boolean instanciarBD() throws ParseException {
		if (!"create".equals(estrategia)) {
			return false;
		}
		
		bdService.instanciarBDTeste();
		
		return true;
	}
}
