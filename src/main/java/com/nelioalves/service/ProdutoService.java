package com.nelioalves.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.domain.Categoria;
import com.nelioalves.domain.Produto;
import com.nelioalves.repository.CategoriaRepository;
import com.nelioalves.repository.ProdutoRepository;
import com.nelioalves.service.exception.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto buscarPorId(Integer id) {
		Optional<Produto> opt = produtoRepository.findById(id);

		return opt.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado. Id: " + id + ", Tipo: " + Produto.class.getName()));
	}
	
	public Page<Produto> buscarComPaginacao(String nome, List<Integer> ids, Integer pagina, Integer linhasPorPagina, String ordernarPor, String direcaoOrdenacao) {
		PageRequest pageRequest = PageRequest.of(pagina, linhasPorPagina, Direction.valueOf(direcaoOrdenacao), ordernarPor);
		List<Categoria> categorias = categoriaRepository.findAllById(ids); 
		
		return produtoRepository.buscar(nome, categorias, pageRequest);
	} 
}
