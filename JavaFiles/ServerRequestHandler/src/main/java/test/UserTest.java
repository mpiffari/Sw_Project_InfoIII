package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import book.Book;
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
	
	
	@Test
	public void getChasingBookTest() {
		String username = "A";
		String password = "";
		String msg = "USER: " + username + ";" + "PASSWORD: " + password;
		User u = new User(msg);
		
		ArrayList<Book> chasingBooks = u.getChasingBooks();
		
		assertTrue(chasingBooks.isEmpty()); //da db osservo che A non ha in mano nessun libro
		
		u.setUsername("T");
		//da db so che ci sono 3 libri in mano a T
		assertTrue(u.getChasingBooks().size() == 3);
		
	}
	
	@Test
	public void computeDistanceTest() {
		String usernameA = "A";
		String password = "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b";
		String msg = "USER: " + usernameA + ";" + "PASSWORD: " + password;
		User uA = new User(usernameA, password);
		uA.setLongitude(10);
		uA.setLatitude(10);
		
		String usernameB = "B";
		msg = "USER: " + usernameB + ";" + "PASSWORD: " + password;
		User uB = new User(msg);
		uB.setLatitude(20);
		uB.setLongitude(20);
		
		assertTrue(uA.computeDistance(uA) == 0);
		assertTrue(uA.computeDistance(uB) > 0);
		assertTrue(uA.computeDistance(uB) == Math.sqrt(100 + 100));
	}
	
	@Test
	public void computeDistanceLocTest() {
		String usernameA = "A";
		String password = "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b";
		String msg = "USER: " + usernameA + ";" + "PASSWORD: " + password;
		User uA = new User(msg);
		
		
		String usernameB = "B";
		msg = "USER: " + usernameB + ";" + "PASSWORD: " + password;
		User uB = new User(msg);
		
		
		UserLocalizationInfo localizationInfoA = new UserLocalizationInfo(10, 10);
		UserLocalizationInfo localizationInfoB = new UserLocalizationInfo(20, 20);
		
		uA.setLatitude(localizationInfoA.lat);
		uA.setLongitude(localizationInfoA.longit);
		uB.setLatitude(localizationInfoB.lat);
		uB.setLongitude(localizationInfoB.longit);
		
		assertTrue(uA.computeDistance(localizationInfoA) == 0);
		assertTrue(uA.computeDistance(localizationInfoB) > 0);
		assertTrue(uA.computeDistance(localizationInfoB) == Math.sqrt(100 + 100));
	}
	

}
