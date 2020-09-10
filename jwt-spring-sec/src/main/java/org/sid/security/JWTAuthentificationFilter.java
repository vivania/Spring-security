package org.sid.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sid.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAuthentificationFilter extends UsernamePasswordAuthenticationFilter  {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public JWTAuthentificationFilter(AuthenticationManager authenticationManager) {
		super();
		this.authenticationManager = authenticationManager;
	}
	
	//Definie une tentative d'authentification / recuperer les username et password 
	/*ObjectMapper : permet de de-serialisé -> de prendre un object de type Json et stocké dans un object java /
	 *  RedValue : lit et recupere le contenenue de la requet avec method getInputValue(), et stocké et de-serialisé  dans object AppUser !
	 *  Utilisation de Username et password en format comme etant un object Json*/
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		AppUser appUser = null;
		
		try {
			appUser = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		
		System.out.println("*******************************");
		System.out.println("Username " + appUser.getUsername());
		System.out.println("Password " + appUser.getPassword());
		
		//return les l'authentification de l'utilisateur sous form de Token
		return authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword()));
	}
	
	/*Si l'utilisateur est authentifié, appel à cette methode ci-dessous 
	   Qui prend le resultat de l'authentification de l'utitlisateur */ 
	//cette methode est utilisé pour générer les token 
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		User  springUser = (User) authResult.getPrincipal();
		String jwt = Jwts.builder()
			.setSubject(springUser.getUsername())
			.setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME))
			.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
			.claim("roles", springUser.getAuthorities())
			.compact();
	    response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX+ jwt);
	}
	
}
