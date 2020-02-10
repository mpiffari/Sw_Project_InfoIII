package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import book.Book;
import dataManager.Localization;
import profile.Profile;

public class ProfileTest {

	@Test
	public void toStringTest() {
		String firstName = "Stefano";
		String lastName = "Villa";
		String username = "S";
		String password = "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b";
		Date dateOfBirth = new Date();
		Localization localization = new Localization();
		localization.lat = 10;
		localization.longit = 20;
		localization.radius = 30;
		
		
		Profile u = new Profile(username, firstName, lastName, dateOfBirth, password, localization.lat, localization.longit, localization.radius);
		
		assertTrue(u.toString().equals("USER: " + username + ";"
				+ "PASSWORD: " + password + "\r\n"));
	}
	
	@Test
	public void compareToTest() {
		String user_A = "A";
		String user_B = "B";
		
		Profile A = new Profile();
		Profile B = new Profile();
		
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
		Profile u = new Profile(msg);
		
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
		Profile uA = new Profile(usernameA, password);
		uA.setLongitude(10);
		uA.setLatitude(10);
		
		String usernameB = "B";
		msg = "USER: " + usernameB + ";" + "PASSWORD: " + password;
		Profile uB = new Profile(msg);
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
		Profile uA = new Profile(msg);
		
		
		String usernameB = "B";
		msg = "USER: " + usernameB + ";" + "PASSWORD: " + password;
		Profile uB = new Profile(msg);
		
		
		Localization localizationInfoA = new Localization(10, 10);
		Localization localizationInfoB = new Localization(20, 20);
		
		uA.setLatitude(localizationInfoA.lat);
		uA.setLongitude(localizationInfoA.longit);
		uB.setLatitude(localizationInfoB.lat);
		uB.setLongitude(localizationInfoB.longit);
		
		assertTrue(uA.computeDistance(localizationInfoA) == 0);
		assertTrue(uA.computeDistance(localizationInfoB) > 0);
		assertTrue(uA.computeDistance(localizationInfoB) == Math.sqrt(100 + 100));
	}
	

}
