package com.nelioalves.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.domain.Cliente;
import com.nelioalves.dto.ClienteDTO;
import com.nelioalves.repository.ClienteRepository;
import com.nelioalves.service.exception.DataIntegrityException;
import com.nelioalves.service.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	public Cliente buscarPorId(Integer id) {
		Optional<Cliente> opt = clienteRepository.findById(id);
		
		return opt.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado. Id: " + id + ", Tipo: " 
				+ Cliente.class.getName()));
	}
	
	public Cliente salvar(Cliente cliente) {
		cliente.setId(null);
		
		return clienteRepository.save(cliente);
	}
	
	public Cliente atualizar(Cliente cliente) {
		Cliente clienteAtualizado =  buscarPorId(cliente.getId());
		atualizarCliente(clienteAtualizado, cliente);
		
		return clienteRepository.save(clienteAtualizado);
	}

	public void apagar(Integer id) {
		buscarPorId(id);
		try {
			clienteRepository.deleteById(id);
		} catch (DataIntegrityException e) {
			throw new DataIntegrityException("Não é possível excluir um cliente");
		}
	}
	
	public List<Cliente> listarTodos() {
		return clienteRepository.findAll();
	}
	
	public Page<Cliente> listarComPaginacao(Integer pagina, Integer linhasPorPagina, String ordernarPor, String direcaoOrdenacao) {
		PageRequest pageRequest = PageRequest.of(pagina, linhasPorPagina, Direction.valueOf(direcaoOrdenacao), ordernarPor);
		
		return clienteRepository.findAll(pageRequest);
	}
	
	public Cliente converterDeDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null);
	}
	
	private void atualizarCliente(Cliente clienteAtualizado, Cliente cliente) {
		clienteAtualizado.setNome(cliente.getNome());
		clienteAtualizado.setEmail(cliente.getEmail());
	}

}
