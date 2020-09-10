package org.sid.service;

import org.sid.entities.AppRole;
import org.sid.entities.AppUser;

//Cette interface gére les comptes 

public interface AccountService {

	//Cette Inscrire un utilisateur 
	public AppUser saveUser(AppUser user);
	//Cette methode ajoute un role
	public AppRole saveRole(AppRole role);
	// Cette method associé un role a l'utilisateur 
	public void  addRoleToUser(String username, String roleName);
	//Cette methode permet de retourné l'utilisateur 
	public AppUser findByUserName(String userName);
	
}
