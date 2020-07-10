package com.nelioalves.service.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.nelioalves.domain.Cliente;
import com.nelioalves.dto.ClienteDTO;
import com.nelioalves.repository.ClienteRepository;
import com.nelioalves.resource.exception.FieldMessage;

public class EmailAtualizadoValidator implements ConstraintValidator<EmailAtualizado, ClienteDTO> {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(EmailAtualizado constraintAnnotation) {}

	@Override
	public boolean isValid(ClienteDTO clienteDTO, ConstraintValidatorContext context) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));

		List<FieldMessage> lista = new ArrayList<>();
		
		Cliente cliente = clienteRepository.findByEmail(clienteDTO.getEmail());
		
		if (cliente != null && !cliente.getId().equals(uriId)) {
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
