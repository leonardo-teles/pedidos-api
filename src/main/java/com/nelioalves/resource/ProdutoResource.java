package com.nelioalves.resource;

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
	
	@GetMapping
	public ResponseEntity<Page<ProdutoDTO>> listarComPaginacao(@RequestParam(value = "nome", defaultValue = "") String nome,
			@RequestParam(value = "categorias", defaultValue = "") Integer categorias,
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina, 
			@RequestParam(value = "linhasPorPagina", defaultValue = "24") Integer linhasPorPagina, 
			@RequestParam(value = "ordenarPor", defaultValue = "nome") String ordernarPor, 
			@RequestParam(value = "direcaoOrdenacao", defaultValue = "ASC") String direcaoOrdenacao) {
		
		Page<Produto> lista = produtoService.buscarComPaginacao(pagina, linhasPorPagina, ordernarPor, direcaoOrdenacao);
		Page<ProdutoDTO> listaDto = lista.map(categoria -> new CategoriaDTO(categoria));
		
		return ResponseEntity.ok().body(listaDto);
	}

	 
}
