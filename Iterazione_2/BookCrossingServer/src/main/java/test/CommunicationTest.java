package test;

import static org.junit.Assert.*;

import org.junit.Test;

import book.Book;
import profile.Profile;
import requestManager.ComputeRequest;
import requestManager.RequestType;
import requestManager.communication.Communication;

public class CommunicationTest {

	@Test
	public void registrationBookTest() {
		
		String usernameA = "A";
		String password = "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b";
		String msg = "USER: " + usernameA + ";" + "PASSWORD: " + password;
		Profile uA = new Profile(msg);
		
		String bookEncode = "TITLE:" + "CasoDiTest" + ";" +
							"AUTHOR:" + "JUNIT" + ";" +
							"YEAR:" + 2019 + ";" +
							"EDITION:" + 1 + ";" +
							"TYPE:" + "Test" + ";" +
							"USER:" + usernameA + ";" +
							"ISBN:" + "" + ";" +
							"STATE:" + true + ";" +
							"BCID:" + "";
		
		Book b = new Book(bookEncode);
		String separator = ";";
		String request = usernameA + separator + "requestType:" + RequestType.BOOK_REGISTRATION_MANUAL.toString() + separator + b.toString();
		
		int j = request.indexOf(";");
		String username = request.substring(0, j);
		System.err.println(request.substring(j + 1));
		
		ComputeRequest.COMPUTE_REQUEST_SINGLETON.process(request.substring(j + 1), usernameA);
		//assertTrue(ComputeRequest.result);
	}

}
