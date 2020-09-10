package org.sid.service;

import java.util.ArrayList;
import java.util.Collection;

import org.sid.entities.AppRole;
import org.sid.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private AccountService accountService;
	

	@Override // Cette methood est utiliser pour verifier l'authentification de l'utilisateur(Role)  
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = accountService.findByUserName(username);
		
		if(user == null) throw new UsernameNotFoundException(username);
		
		//Donne l'autorisation au Role
		Collection<GrantedAuthority> authoriries = new ArrayList<>();
			user.getRoles().forEach(r -> {
				authoriries.add(new SimpleGrantedAuthority(r.getRoleName()));
			});
		return new User(user.getUsername(), user.getPassword(), authoriries);
	} 

}
