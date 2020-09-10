package org.sid.dao;

import org.sid.entities.AppRole;
import org.sid.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoty extends JpaRepository<AppUser, Long> {
	
	//Cette methode permet de retourné le role de l'utilisateur 
	public AppUser findByUsername(String userName);

}
