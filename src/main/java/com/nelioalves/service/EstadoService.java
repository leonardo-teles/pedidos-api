package com.nelioalves.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.domain.Estado;
import com.nelioalves.repository.EstadoRepository;

@Service
public class EstadoService {

	@Autowired
	private EstadoRepository estadoRepository;
	
	public List<Estado> listarOrdenadosPorNome() {
		return estadoRepository.findAllByOrderByNome();
	}
}
