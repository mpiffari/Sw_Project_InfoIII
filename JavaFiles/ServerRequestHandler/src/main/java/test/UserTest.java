package test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import user.User;
import user.UserLocalizationInfo;

public class UserTest {

	@Test
	public void toStringTest() {
		String firstName = "Stefano";
		String lastName = "Villa";
		String username = "S";
		String password = "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b";
		Date dateOfBirth = new Date();
		UserLocalizationInfo localization = new UserLocalizationInfo();
		localization.lat = 10;
		localization.longit = 20;
		localization.radius = 30;
		User u = new User(username, firstName, lastName, dateOfBirth, password, localization.lat, localization.longit, localization.radius);
		
		assertTrue(u.toString().equals("USER: " + username + ";"
				+ "PASSWORD: " + password + "\r\n"));
	}
	
	@Test
	public void compareToTest() {
		String user_A = "A";
		String user_B = "B";
		
		User A = new User();
		User B = new User();
		
		A.setUsername(user_A);
		B.setUsername(user_B);
		
		assertTrue(A.compareTo(B) < 0);
		assertTrue(B.compareTo(B) == 0);
		assertTrue(B.compareTo(A) > 0);
	}

}
