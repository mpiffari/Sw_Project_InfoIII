package requestManager;

import book.Book;
import user.LoginStatus;
import user.UserData;
import user.User;

public class ComputeRequest implements ProcessRequest{

	/**
	 * @param msg: is a string something like this 
	 * <request-Type>: number that specifies type of request;
	 * <other data>
	 */
	
	public final static ComputeRequest computeRequestSingleton = new ComputeRequest();
	
	public final void process(String msg, String username) {
		int i = msg.indexOf(";", 0);
		int j = msg.indexOf(":", 0);
		RequestType requestType = RequestType.getEnumReqType(msg.substring(0, i).substring(j+1));
		
		if(UserData.getInstance().exist(username)) {
			System.out.println("Message in process func: " + msg);
			System.out.println(msg.substring(0, i));
			System.out.println(msg.substring(0, i).substring(j+1));
			
			switch(requestType) {
			case BOOK_REGISTRATION_MANUAL:
				Book b = new Book(msg.substring(i + 1));
				b.setProprietario(username);
				//TODO: add check of ISBN (iteration 2)
				b.setISBN("AAAAAAAAA");

				boolean result = b.insert();
				Communication.getInstance().send(username, "requestType:0;result:" + (result?1:0) + ";BCID:" + b.getBCID());
				break;
			case BOOK_RESERVATION:
				Book book = new Book(msg.substring(i + 1));
				Communication.getInstance().send(username, "requestType:1;result:" + book.reserve(username));
				break;
			case LOGIN:
				User user = new User(msg.substring(i + 1));
				
				LoginStatus res = user.login();
				String r = "";
				switch (res) {
				case SUCCESS:
					r = "Success";
					System.out.println("Login ok for user "+ user.getUsername() + " with pwd " + user.getPassword());
					break;
				case WRONG_USERNAME:
					r = "KO_Username";
					System.out.println("KO_Username for user "+ user.getUsername());
					break;
				case WRONG_PWD:
					r = "KO_Password";
					System.out.println("KO_Password for user "+ user.getUsername() + " with KO pwd " + user.getPassword());
					break;
				default:
					break;
				}
				Communication.getInstance().send(username, "requestType:2;result:" + r);
			default:
				break;
			}
		} else {
			System.out.println("No user found: for each type of communication, server will refuse it");
			String reqTypeResponse = "";
			switch(requestType) {
			case BOOK_REGISTRATION_MANUAL:
				reqTypeResponse = "0";
				break;
			case BOOK_RESERVATION:
				reqTypeResponse = "1";
				break;
			case LOGIN:
				reqTypeResponse = "2";
				break;
			default:
				break;
			}
			Communication.getInstance().send(username, "requestType:" + reqTypeResponse + ";result:KO_Username");
		}

	}


}
