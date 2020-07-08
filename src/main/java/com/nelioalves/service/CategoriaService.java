package com.nelioalves.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.nelioalves.domain.Categoria;
import com.nelioalves.repository.CategoriaRepository;
import com.nelioalves.service.exception.DataIntegrityException;
import com.nelioalves.service.exception.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Categoria buscarPorId(Integer id) {
		Optional<Categoria> opt = categoriaRepository.findById(id);
		
		return opt.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado. Id: " + id + ", Tipo: "
				+ Categoria.class.getName()));
	}

	public Categoria salvar(Categoria categoria) {
		categoria.setId(null);
		
		return categoriaRepository.save(categoria);
	}

	public Categoria atualizar(Categoria categoria) {
		buscarPorId(categoria.getId());
		
		return categoriaRepository.save(categoria);
	}

	public void apagar(Integer id) {
		buscarPorId(id);
		try {
			categoriaRepository.deleteById(id);
		} catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos.");
		}
	}
}
