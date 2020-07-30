package com.nelioalves.resource;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.dto.EmailDTO;
import com.nelioalves.security.JWTUtil;
import com.nelioalves.security.UsuarioSistema;
import com.nelioalves.service.AuthService;
import com.nelioalves.service.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService authService;
	
	@PostMapping(value = "/refresh_token")
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		
		UsuarioSistema usuarioSistema = UserService.usuarioLogado();
		String token = jwtUtil.gerarToken(usuarioSistema.getUsername());
		
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping(value = "/esqueci")
	public ResponseEntity<Void> esqueci(@Valid @RequestBody EmailDTO emailDTO) {
		authService.enviarNovaSenha(emailDTO.getEmail());
		
		return ResponseEntity.noContent().build();
	}
	
}
