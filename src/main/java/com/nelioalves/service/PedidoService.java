package com.nelioalves.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.domain.Pedido;
import com.nelioalves.repository.PedidoRepository;
import com.nelioalves.service.exception.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	public Pedido buscarPorId(Integer id) {
		Optional<Pedido> opt = pedidoRepository.findById(id);

		return opt.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado. Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	 
}
