package com.nelioalves.resource;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.domain.Pedido;
import com.nelioalves.service.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

	@Autowired
	private PedidoService pedidoService;
	
	@GetMapping("/{id}")
	public ResponseEntity<Pedido> buscar(@PathVariable Integer id) {
		Pedido pedido = pedidoService.buscarPorId(id);
		
		return ResponseEntity.ok().body(pedido);
	}
	
	@PostMapping
	public ResponseEntity<Void> inserir(@Valid @RequestBody Pedido pedido) {
		
		pedido = pedidoService.salvar(pedido);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pedido.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping
	public ResponseEntity<Page<Pedido>> listarComPaginacao(@RequestParam(value = "pagina", defaultValue = "0") Integer pagina, 
			@RequestParam(value = "linhasPorPagina", defaultValue = "24") Integer linhasPorPagina, 
			@RequestParam(value = "ordenarPor", defaultValue = "instante") String ordernarPor, 
			@RequestParam(value = "direcaoOrdenacao", defaultValue = "DESC") String direcaoOrdenacao) {
		
		Page<Pedido> lista = pedidoService.listarComPaginacao(pagina, linhasPorPagina, ordernarPor, direcaoOrdenacao);
		
		return ResponseEntity.ok().body(lista);
	}
	
	
}
