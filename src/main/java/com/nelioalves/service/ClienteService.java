package com.nelioalves.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.domain.Cliente;
import com.nelioalves.repository.ClienteRepository;
import com.nelioalves.service.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	public Cliente buscarPorId(Integer id) {
		Optional<Cliente> opt = clienteRepository.findById(id);
		
		return opt.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado. Id: " + id + ", Tipo: " 
				+ Cliente.class.getName()));
	}
}
