package SpringSecurityApp.app.test;

import org.springframework.beans.factory.annotation.Autowired;

import SpringSecurityApp.app.service.UserAccountService;


public class Test {

	@Autowired
	private UserAccountService sv;


	public void registerAdmin() {
		sv.registerAdmin("dbadmin","dbadpass");
		sv.registerUser("dbuser","dbusrp");

	}




}
