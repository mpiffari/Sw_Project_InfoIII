package test;

import static org.junit.Assert.*;

import org.junit.Test;

import dataManager.ProfileData;
import profile.LoginStatus;
import profile.Profile;


public class UserDataTest {

	
	@Test
	public void logintTest() {
		
		Profile u = new Profile();
		u.setUsername("A");
		u.setPassword("6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b");
		assertTrue(LoginStatus.SUCCESS == u.login());
		
		u.setPassword("1");
		assertFalse(LoginStatus.SUCCESS == ProfileData.getInstance().login(u));
		assertTrue(LoginStatus.WRONG_PWD == ProfileData.getInstance().login(u));
		
		u.setUsername("paperino");
		assertTrue(LoginStatus.WRONG_USERNAME == ProfileData.getInstance().login(u));
	}

	@Test
	public void existLogin() {
		Profile u = new Profile();
		u.setUsername("A");
		
		assertTrue(ProfileData.getInstance().exist(u.getUsername()));
		
		u.setUsername("ZX");
		assertFalse(ProfileData.getInstance().exist(u.getUsername()));
	}
}
