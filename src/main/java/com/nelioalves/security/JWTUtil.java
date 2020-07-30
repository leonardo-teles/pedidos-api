package com.nelioalves.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
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
	
	public boolean tokenValido(String token) {
		Claims claims = getClaims(token);
		
		if(claims != null) {
			String username = claims.getSubject();
			Date dataExpiracao = claims.getExpiration();
			Date dataAtual = new Date(System.currentTimeMillis());
			
			if(username != null && dataExpiracao != null && dataAtual.before(dataExpiracao)) {
				return true;
			}
		}
		
		return false;
	}
	
	public String getUsername(String token) {
		Claims claims = getClaims(token);
		
		if(claims != null) {
			return claims.getSubject();
		}
	
		return null;
	}	

	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(palavraPasse.getBytes()).parseClaimsJws(token).getBody();
		} catch(Exception e) {
			return null;
		}
	}
	
}
