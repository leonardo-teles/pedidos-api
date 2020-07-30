package com.nelioalves.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nelioalves.security.UsuarioSistema;

@Service
public class UserService {

	//retorna o usuário logado no sistema
	public static UsuarioSistema usuarioLogado() {
		try {
			return (UsuarioSistema) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch(Exception e) {
			return null;
		}
	}
}
