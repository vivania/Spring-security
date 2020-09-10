package org.sid.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor 
public class AppUser {

	@Id
	@GeneratedValue
	private Long id;
	@Column(unique=true)// Indique dans la base de donnée que l'utilisateur sera unique 
	private String username;
	private String password;
	
	// Fetch indique : A cahque fois qu'un utilisateur est chargé, il va charger c'est roles
	@ManyToMany(fetch=FetchType.EAGER) //Cettte anotation indique qu'un utilisateur peur avoir plusieur roles - et un role peut concerné plusieur Utilisateur 
	private Collection<AppRole> roles = new ArrayList<>();
}
