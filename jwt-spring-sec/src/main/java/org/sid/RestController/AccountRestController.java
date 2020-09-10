package org.sid.RestController;

import org.sid.entities.AppUser;
import org.sid.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController             
public class AccountRestController {
	
	@Autowired
	private AccountService accountService;
	
	
	@PostMapping("/register")
	public AppUser register(@RequestBody RegisterForm userForm) {
		
		//Si le password est different de repassword alors une exception est lévé
		if(!userForm.getPassword().equals(userForm.getRepassword()))
			throw new RuntimeException("You must confirm your password");
			//Verifier si l'utilisateur exist deja 
			AppUser user = accountService.findByUserName(userForm.getUsername());
		if(user != null) throw new RuntimeException("This user already exists");
			AppUser appUSer = new AppUser();
			appUSer.setUsername(userForm.getUsername());
			appUSer.setPassword(userForm.getPassword());
			accountService.saveUser(appUSer); 
			accountService.addRoleToUser(userForm.getUsername(), "USER");
			System.out.println(appUSer);
			return appUSer;
	}
}
 