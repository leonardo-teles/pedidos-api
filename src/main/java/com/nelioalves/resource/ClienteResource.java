package com.nelioalves.resource;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.domain.Cliente;
import com.nelioalves.dto.ClienteDTO;
import com.nelioalves.dto.ClienteNovoDTO;
import com.nelioalves.service.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

	@Autowired
	private ClienteService clienteService;
	
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> buscar(@PathVariable Integer id) {
		Cliente cliente = clienteService.buscarPorId(id);
		
		return ResponseEntity.ok().body(cliente);
	}
	
	@GetMapping("/email")
	public ResponseEntity<Cliente> buscarPorEmail(@RequestParam(value = "valor") String email) {
		Cliente cliente = clienteService.buscarPorEmail(email);
		
		return ResponseEntity.ok().body(cliente);
	}

  	@PostMapping
	public ResponseEntity<Void> inserir(@Valid @RequestBody ClienteNovoDTO clienteNovoDTO) {
		Cliente cliente = clienteService.converterDeDTO(clienteNovoDTO);
		
		cliente = clienteService.salvar(cliente);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> atualizar(@Valid @RequestBody ClienteDTO clienteDTO, @PathVariable Integer id) {
		Cliente cliente = clienteService.converterDeDTO(clienteDTO);
		
		cliente.setId(id);
		cliente = clienteService.atualizar(cliente);
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> apagar(@PathVariable Integer id) {
		clienteService.apagar(id);
		
		return ResponseEntity.noContent().build();
	}	
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<List<ClienteDTO>> listar() {
		List<Cliente> lista = clienteService.listarTodos();
		List<ClienteDTO> listaDto = lista.stream().map(cliente -> new ClienteDTO(cliente)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listaDto);
	}
	
	@GetMapping(value = "/page")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Page<ClienteDTO>> listarComPaginacao(@RequestParam(value = "pagina", defaultValue = "0") Integer pagina, 
			@RequestParam(value = "linhasPorPagina", defaultValue = "24") Integer linhasPorPagina, 
			@RequestParam(value = "ordenarPor", defaultValue = "nome") String ordernarPor, 
			@RequestParam(value = "direcaoOrdenacao", defaultValue = "ASC") String direcaoOrdenacao) {
		
		Page<Cliente> lista = clienteService.listarComPaginacao(pagina, linhasPorPagina, ordernarPor, direcaoOrdenacao);
		Page<ClienteDTO> listaDto = lista.map(cliente -> new ClienteDTO(cliente));
		
		return ResponseEntity.ok().body(listaDto);
	}
	
  	@PostMapping(value = "/foto")
	public ResponseEntity<Void> uploadFoto(@RequestParam(name = "arquivo") MultipartFile arquivo) {
  		URI uri = clienteService.uploadFoto(arquivo);
		
		return ResponseEntity.created(uri).build();
	}
	
}
