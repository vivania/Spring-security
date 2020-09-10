package org.sid.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JWTAutorizationFilter  extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		//autorisation recupere des requette venant de l'exterieur( Code du filtre de la parti front-end )
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Headers", 
				"Origin, Accept, X-Requested-With, Content-Type,"
				+ "Access-Control-Request-Method, "
				+ "Access-Control-Request- Headers,"
				+ "Authorization");
		
		response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Authorization");
		     
		//Verifier si le token exist dans la requette header et le prefix 
		String jwt = request.getHeader(SecurityConstants.HEADER_STRING);
		System.out.println(jwt);
		if(request.getMethod().equals("OPTIONS")) {
			response.setStatus(HttpServletResponse.SC_OK);
		} 
		else {
			if(jwt == null || !jwt.startsWith(SecurityConstants.TOKEN_PREFIX)) {
				filterChain.doFilter(request, response);
				return;
			}
			Claims  claims = Jwts.parser()
				.setSigningKey(SecurityConstants.SECRET)
				.parseClaimsJws(jwt.replace(SecurityConstants.TOKEN_PREFIX, ""))
				.getBody();
				
				// Subject contient le username a recuperer
				String username = claims.getSubject();
				ArrayList<Map<String, String>> roles = (ArrayList<Map<String, String>>) claims.get("roles");
				//auhorities define le role de l'utilisateur 
				Collection<GrantedAuthority> authorities = new ArrayList<>();
				roles.forEach(r -> {
				authorities.add(new SimpleGrantedAuthority(r.get("authority")));		
				});
				//Recuperer l'identité de l'utilisateur qui a envoyer la requette  
				UsernamePasswordAuthenticationToken authenticateUser = 
						new UsernamePasswordAuthenticationToken(username , null, authorities);
						SecurityContextHolder.getContext().setAuthentication(authenticateUser);
						filterChain.doFilter(request, response);					
		}
		
	}

}
