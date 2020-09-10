package org.sid.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Definition de la class de security
@Configuration
@EnableWebSecurity // active la securité web
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
	
	@Autowired // Utilisation d'un systeme d'authentification basé sur une couche service
	private UserDetailsService userDetailsService;
	
	@Autowired // Sert a la verification(et encriptage du mode de passe) ansi que le Hash /  
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override //Cette Authentification et definition des roles 
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/*	auth.inMemoryAuthentication()
		.withUser("admin").password("{noop}1234").roles("ADMIN", "USER")
		.and()
		.withUser("user").password("{noop}123").roles("USER");*/
		
		//Definir quel type d'encodage
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(bCryptPasswordEncoder); // Verification du mots de passe avec BCRYP	
	}
	
	//Pour Utilisé JWt il faut desactiver l'authentifaiction basé sur les Sessions
	
	@Override //Cette methode definie les droits d'accées / et filtrage
	protected void configure(HttpSecurity http) throws Exception {
		
		//Desactiver le csrf pour etre disponible pour les autres application externe
		http.csrf().disable();
		//Desactivation de la Session
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		/* formulaire d'authentification utilisé par spring 
		 loginPage est utilisé pour la personalisation*/
		//http.formLogin();
		http.authorizeRequests().antMatchers("/login/**",  "/register/**").permitAll();
		//Si Utilisation de post n'autorisé que si l'utilisateur est Admin 
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/save/**").hasAuthority("ADMIN");
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(new JWTAuthentificationFilter(authenticationManager()));
		//ne pas oublier d'importer  la class JWTAutorizationFilter
		http.addFilterBefore(new JWTAutorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}
