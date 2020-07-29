package com.nelioalves.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nelioalves.domain.ItemPedido;
import com.nelioalves.domain.PagamentoComBoleto;
import com.nelioalves.domain.Pedido;
import com.nelioalves.enums.EstadoPagamento;
import com.nelioalves.repository.ItemPedidoRepository;
import com.nelioalves.repository.PagamentoRepository;
import com.nelioalves.repository.PedidoRepository;
import com.nelioalves.service.exception.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido buscarPorId(Integer id) {
		Optional<Pedido> opt = pedidoRepository.findById(id);

		return opt.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado. Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	@Transactional
	public Pedido salvar(Pedido pedido) {
		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.setCliente(clienteService.buscarPorId(pedido.getCliente().getId()));
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENDTE);
		pedido.getPagamento().setPedido(pedido);
		
		//gera a data de vencimento(7 dias após a data atual) com uma classe auxiliar, caso o pagamento seja com boleto
		if(pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pgto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoComBoleto(pgto, pedido.getInstante());
		}
		
		pedido = pedidoRepository.save(pedido);
		pagamentoRepository.save(pedido.getPagamento());
		
		for(ItemPedido ip : pedido.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.buscarPorId(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(pedido);
		}
		
		itemPedidoRepository.saveAll(pedido.getItens());
		
		emailService.enviarEmailDeConfirmacaoDePedido(pedido);
		
		return pedido;
	}
	 
}
