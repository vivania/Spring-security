package org.sid.service;

import javax.transaction.Transactional;

import org.sid.dao.RoleRepository;
import org.sid.dao.UserRepositoty;
import org.sid.entities.AppRole;
import org.sid.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service 
@Transactional
public class AccountServiceImpl implements AccountService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepositoty userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	
	
	@Override //Sauvegarde et et encodage(Cripté) du mots de passe saisie par utilisauetr 
	public AppUser saveUser(AppUser user) {
		String hashPW = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(hashPW);
		return userRepository.save(user);
	}
 
	@Override
	public AppRole saveRole(AppRole role) {
		return roleRepository.save(role);
	}
     
	//Cette methode permet d'ajouter un Role a u utilisateur 
	@Override
	public void addRoleToUser(String username, String roleName) {
		AppUser user =  userRepository.findByUsername(username);
		AppRole role = roleRepository.findByRoleName(roleName);
		user.getRoles().add(role);
	} 

	@Override
	public AppUser findByUserName(String userName) {
		return userRepository.findByUsername(userName);
	}

}
