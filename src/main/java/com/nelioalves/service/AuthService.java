package com.nelioalves.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nelioalves.domain.Cliente;
import com.nelioalves.repository.ClienteRepository;
import com.nelioalves.service.exception.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private EmailService emailService;
	
	private Random random = new Random();
	
	public void enviarNovaSenha(String email) {
		
		Cliente cliente = clienteRepository.findByEmail(email);
		if(cliente == null) {
			throw new ObjectNotFoundException("e-Mail não encontrado");
		}
		
		String novaSenha = novaSenha();
		cliente.setSenha(encoder.encode(novaSenha));
		
		clienteRepository.save(cliente);
		
		emailService.enviarEmailComNovaSenha(cliente, novaSenha);
	}

	// gera uma senha aleatória de 10 caracteres podendo ser letras e números 
	private String novaSenha() {
		char[] vetor = new char[10];
		
		for(int i = 0; i < 10; i++) {
			vetor[i] = randomChar();
		}
		
		return new String(vetor);
	}

	private char randomChar() {
		int opt = random.nextInt(3);
		
		if(opt == 0) { //gera um dígito
			return (char) (random.nextInt(10) + 48);
		} else if(opt == 1) { //gera letra maiúscula
			return (char) (random.nextInt(26) + 65);
		} else { //gera letra minúscula
			return (char) (random.nextInt(26) + 97);
		}
	}
}
