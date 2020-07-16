package com.nelioalves.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.nelioalves.domain.PagamentoComBoleto;

@Service
public class BoletoService {

	public void preencherPagamentoComBoleto(PagamentoComBoleto pgto, Date instanteDoPedido) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(instanteDoPedido);
		calendar.add(Calendar.DAY_OF_MONTH, 7);
		pgto.setDataVencimento(calendar.getTime());
	}
}
