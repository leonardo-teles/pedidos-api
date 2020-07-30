package com.nelioalves.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	@Value("${jwt.passe}")
	private String palavraPasse;
	
	@Value("${jwt.expiracao}")
	private Long tempoExpiracao;
	
	public String gerarToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + tempoExpiracao))
				.signWith(SignatureAlgorithm.HS512, palavraPasse.getBytes())
				.compact();
	}
	
}
