package test;

import static org.junit.Assert.*;

import org.junit.Test;

import user.LoginStatus;
import user.User;
import user.UserData;


public class UserDataTest {

	
	@Test
	public void logintTest() {
		
		User u = new User();
		u.setUsername("A");
		u.setPassword("6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b");
		assertTrue(LoginStatus.SUCCESS == u.login());
		
		u.setPassword("1");
		assertFalse(LoginStatus.SUCCESS == UserData.getInstance().login(u));
		assertTrue(LoginStatus.WRONG_PWD == UserData.getInstance().login(u));
		
		u.setUsername("paperino");
		assertTrue(LoginStatus.WRONG_USERNAME == UserData.getInstance().login(u));
	}

	@Test
	public void existLogin() {
		User u = new User();
		u.setUsername("A");
		
		assertTrue(UserData.getInstance().exist(u.getUsername()));
		
		u.setUsername("ZX");
		assertFalse(UserData.getInstance().exist(u.getUsername()));
	}
}
