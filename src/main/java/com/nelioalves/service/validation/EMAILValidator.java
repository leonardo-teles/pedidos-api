package com.nelioalves.service.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.nelioalves.domain.Cliente;
import com.nelioalves.dto.ClienteNovoDTO;
import com.nelioalves.repository.ClienteRepository;
import com.nelioalves.resource.exception.FieldMessage;

public class EMAILValidator implements ConstraintValidator<EMAIL, ClienteNovoDTO> {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(EMAIL constraintAnnotation) {}

	@Override
	public boolean isValid(ClienteNovoDTO clienteNovoDTO, ConstraintValidatorContext context) {

		List<FieldMessage> lista = new ArrayList<>();
		
		Cliente cliente = clienteRepository.findByEmail(clienteNovoDTO.getEmail());
		if (cliente != null) {
			lista.add(new FieldMessage("email", "e-Mail já existente"));
		}
		
		for (FieldMessage e : lista) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		
		return lista.isEmpty();
	}
}
