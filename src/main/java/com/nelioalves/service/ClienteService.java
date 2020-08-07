package com.nelioalves.service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nelioalves.domain.Cidade;
import com.nelioalves.domain.Cliente;
import com.nelioalves.domain.Endereco;
import com.nelioalves.dto.ClienteDTO;
import com.nelioalves.dto.ClienteNovoDTO;
import com.nelioalves.enums.Perfil;
import com.nelioalves.enums.TipoCliente;
import com.nelioalves.repository.ClienteRepository;
import com.nelioalves.repository.EnderecoRepository;
import com.nelioalves.security.UsuarioSistema;
import com.nelioalves.service.exception.AuthorizationException;
import com.nelioalves.service.exception.DataIntegrityException;
import com.nelioalves.service.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private S3Service s3Service;
	
	public Cliente buscarPorId(Integer id) {
		UsuarioSistema usuarioSistema = UserService.usuarioLogado();
		
		if (usuarioSistema == null || !usuarioSistema.hasRole(Perfil.ADMINISTRADOR) && !id.equals(usuarioSistema.getId())) {
			throw new AuthorizationException("Acesso Negado");
		}
		
		Optional<Cliente> opt = clienteRepository.findById(id);
		
		return opt.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado. Id: " + id + ", Tipo: " 
				+ Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente salvar(Cliente cliente) {
		cliente.setId(null);
		cliente = clienteRepository.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		
		return cliente;
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
		} catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir clientes que possuem pedidos.");
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
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null, null);
	}
	
	public Cliente converterDeDTO(ClienteNovoDTO clienteNovoDTO) {
		Cliente cliente = new Cliente(null, clienteNovoDTO.getNome(), clienteNovoDTO.getEmail(), 
				clienteNovoDTO.getCpfOuCnpj(), TipoCliente.toEnum(clienteNovoDTO.getTipo()), encoder.encode(clienteNovoDTO.getSenha()));
		
		Cidade cidade = new Cidade(clienteNovoDTO.getCidadeId(), null, null);
		
		Endereco endereco = new Endereco(null, clienteNovoDTO.getLogradouro(), clienteNovoDTO.getNumero(), 
				 clienteNovoDTO.getComplemento(), clienteNovoDTO.getBairro(), clienteNovoDTO.getCep(), cliente, cidade);
		
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(clienteNovoDTO.getTelefone1());
		
		if (clienteNovoDTO.getTelefone2() != null) {
			cliente.getTelefones().add(clienteNovoDTO.getTelefone2());
		}

		if (clienteNovoDTO.getTelefone3() != null) {
			cliente.getTelefones().add(clienteNovoDTO.getTelefone3());
		}

		return cliente;
	}
	
	private void atualizarCliente(Cliente clienteAtualizado, Cliente cliente) {
		clienteAtualizado.setNome(cliente.getNome());
		clienteAtualizado.setEmail(cliente.getEmail());
	}
	
	public URI uploadFoto(MultipartFile multipartFile) {
		UsuarioSistema usuarioSistema = UserService.usuarioLogado();
		
		if(usuarioSistema == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		URI uri = s3Service.upload(multipartFile);
		
		Optional<Cliente> cliente = clienteRepository.findById(usuarioSistema.getId());
		cliente.orElse(null).setUrlImagem(uri.toString());
		clienteRepository.save(cliente.orElse(null));
		
		return uri;
	}

}
