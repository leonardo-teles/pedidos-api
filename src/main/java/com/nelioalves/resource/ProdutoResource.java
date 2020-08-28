package com.nelioalves.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.domain.Categoria;
import com.nelioalves.domain.Produto;
import com.nelioalves.dto.CategoriaDTO;
import com.nelioalves.dto.ProdutoDTO;
import com.nelioalves.resource.util.URL;
import com.nelioalves.service.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoService produtoService;
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> buscar(@PathVariable Integer id) {
		Produto produto = produtoService.buscarPorId(id);
		
		return ResponseEntity.ok().body(produto);
	}
	
	@GetMapping(value = "/categorias")
	public ResponseEntity<List<ProdutoDTO>> listarComCategorias() {
		List<Produto> lista = produtoService.listarTodos();
		List<ProdutoDTO> listaDto = lista.stream().map(produto -> new ProdutoDTO(produto)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listaDto);
	}
	
	@GetMapping(value = "/lista")
	public ResponseEntity<List<Produto>> listar() {
		List<Produto> lista = produtoService.listarTodos();
		
		return ResponseEntity.ok().body(lista);
	}
	
	@GetMapping("/{id}/categorias")
	public ResponseEntity<List<CategoriaDTO>> listarCategoriasDoProduto(@PathVariable Integer id) {
		Produto produto = produtoService.buscarPorId(id);
		
		List<Categoria> lista = produto.getCategorias();
		List<CategoriaDTO> listaDto = lista.stream().map(categoria -> new CategoriaDTO(categoria)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listaDto);
	}
	
	@GetMapping
	public ResponseEntity<Page<ProdutoDTO>> listarComPaginacao(@RequestParam(value = "nome", defaultValue = "") String nome,
			@RequestParam(value = "categorias", defaultValue = "") String categorias,
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina, 
			@RequestParam(value = "linhasPorPagina", defaultValue = "24") Integer linhasPorPagina, 
			@RequestParam(value = "ordenarPor", defaultValue = "nome") String ordernarPor, 
			@RequestParam(value = "direcaoOrdenacao", defaultValue = "ASC") String direcaoOrdenacao) {
		
		String nomeCodificado = URL.decodificarParam(nome);
		List<Integer> ids = URL.decodificarIntList(categorias);
		
		Page<Produto> lista = produtoService.buscarComPaginacao(nomeCodificado, ids, pagina, linhasPorPagina, ordernarPor, direcaoOrdenacao);
		
		Page<ProdutoDTO> listaDto = lista.map(produto -> new ProdutoDTO(produto));
		
		return ResponseEntity.ok().body(listaDto);
	}
}
