package com.nelioalves.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.domain.Cidade;
import com.nelioalves.repository.CidadeRepository;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	public List<Cidade> listarCidadesPorEstado(Integer estadoId) {
		return cidadeRepository.findCidades(estadoId);
	}
}
