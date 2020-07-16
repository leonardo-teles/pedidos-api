package com.nelioalves.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nelioalves.domain.Categoria;
import com.nelioalves.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT p FROM Produto p INNER JOIN p.categorias c WHERE lower(p.nome) LIKE %:nome% AND c IN :categorias")
	Page<Produto> buscar(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable pageRequest);

	//executando a mesma busca sem uso da anotação @Query
	//@Transactional(readOnly = true)
	//Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
}
