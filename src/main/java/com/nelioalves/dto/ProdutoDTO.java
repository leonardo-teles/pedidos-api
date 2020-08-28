package com.nelioalves.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.nelioalves.domain.Produto;

public class ProdutoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String nome;
	
	private Double preco;
	
	private List<CategoriaDTO> categorias;
	
	public ProdutoDTO() {}
	
	public ProdutoDTO(Produto produto) {
		id = produto.getId();
		nome = produto.getNome();
		preco = produto.getPreco();
		setCategorias(produto.getCategorias().stream().map(categorias -> new CategoriaDTO(categorias)).collect(Collectors.toList()));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public List<CategoriaDTO> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<CategoriaDTO> categorias) {
		this.categorias = categorias;
	}
}
