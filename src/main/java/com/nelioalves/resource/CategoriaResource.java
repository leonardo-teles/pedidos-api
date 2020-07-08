package com.nelioalves.resource;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.domain.Categoria;
import com.nelioalves.dto.CategoriaDTO;
import com.nelioalves.service.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService categoriaService;
	
	@GetMapping("/{id}")
	public ResponseEntity<Categoria> buscar(@PathVariable Integer id) {
		Categoria categoria = categoriaService.buscarPorId(id);
		
		return ResponseEntity.ok().body(categoria);
	}
	
	@PostMapping
	public ResponseEntity<Void> inserir(@Valid @RequestBody CategoriaDTO categoriaDto) {
		Categoria categoria = categoriaService.converterDeDTO(categoriaDto);
		
		categoria = categoriaService.salvar(categoria);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoria.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> atualizar(@Valid @RequestBody CategoriaDTO categoriaDto, @PathVariable Integer id) {
		Categoria categoria = categoriaService.converterDeDTO(categoriaDto);
		
		categoria.setId(id);
		categoria = categoriaService.atualizar(categoria);
		
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> apagar(@PathVariable Integer id) {
		categoriaService.apagar(id);
		
		return ResponseEntity.noContent().build();
	}	
		
	@GetMapping
	public ResponseEntity<List<CategoriaDTO>> listar() {
		List<Categoria> lista = categoriaService.listarTodas();
		List<CategoriaDTO> listaDto = lista.stream().map(categoria -> new CategoriaDTO(categoria)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listaDto);
	}
	
	@GetMapping(value = "/page")
	public ResponseEntity<Page<CategoriaDTO>> listarComPaginacao(@RequestParam(value = "pagina", defaultValue = "0") Integer pagina, 
			@RequestParam(value = "linhasPorPagina", defaultValue = "24") Integer linhasPorPagina, 
			@RequestParam(value = "ordenarPor", defaultValue = "nome") String ordernarPor, 
			@RequestParam(value = "direcaoOrdenacao", defaultValue = "ASC") String direcaoOrdenacao) {
		
		Page<Categoria> lista = categoriaService.listarComPaginacao(pagina, linhasPorPagina, ordernarPor, direcaoOrdenacao);
		Page<CategoriaDTO> listaDto = lista.map(categoria -> new CategoriaDTO(categoria));
		
		return ResponseEntity.ok().body(listaDto);
	}
	
}
