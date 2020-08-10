package com.nelioalves.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.domain.Cidade;
import com.nelioalves.domain.Estado;
import com.nelioalves.dto.CidadeDTO;
import com.nelioalves.dto.EstadoDTO;
import com.nelioalves.service.CidadeService;
import com.nelioalves.service.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {

	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private CidadeService cidadeService;
	
	@GetMapping
	public ResponseEntity<List<EstadoDTO>> listarTodosOrdenadosPorNome() {
		List<Estado> lista = estadoService.listarOrdenadosPorNome();
		List<EstadoDTO> listaDto = lista.stream().map(estado -> new EstadoDTO(estado)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listaDto);
	}
	
	@GetMapping("/{estadoId}/cidades")
	public ResponseEntity<List<CidadeDTO>> listarCidades(@PathVariable Integer estadoId) {
		List<Cidade> lista = cidadeService.listarCidadesPorEstado(estadoId);
		List<CidadeDTO> listaDto = lista.stream().map(cidade -> new CidadeDTO(cidade)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listaDto);
	}
}
