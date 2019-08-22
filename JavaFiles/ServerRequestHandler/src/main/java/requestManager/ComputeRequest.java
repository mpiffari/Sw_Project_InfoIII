package requestManager;


import book.Book;
import dataManager.UserData;
import user.User;

public class ComputeRequest implements ProcessRequest{

	/**
	 * @param msg: is a string something like this 
	 * <request-Type>: number that specifies type of request;
	 * <other data>
	 */

	private final static String request = "requestType: 0; result: ";

	public void process(String msg, String username) {
		if(UserData.getInstance().exist(username)) {
			System.out.println("Message in process func: " + msg);
			int i = msg.indexOf(";", 0);
			int j = msg.indexOf(":", 0);
			RequestType requestType = RequestType.getEnumReqType(msg.substring(0, i).substring(j+1));
			System.out.println(msg.substring(0, i));
			System.out.println(msg.substring(0, i).substring(j+1));
			boolean result;
			
			switch(requestType) {
			case BOOK_REGISTRATION_MANUAL:
				Book b = new Book(msg.substring(i + 1));
				b.setProprietario(username);
				//TODO: add check of ISBN (iteration 2)
				b.setISBN("AAAAAAAAA");

				result = b.insert();
				Communication.getInstance().send(username, "requestType:0;result:" + (result?1:0) + ";BCID:" + b.getBCID());
				break;
			case BOOK_RESERVATION:
				Book book = new Book(msg.substring(i + 1));
				Communication.getInstance().send(username, "requestType:1;result:" + book.reserve(username));
				break;
			case LOGIN:
				User user = new User(msg.substring(i + 1));
				
				result = user.login();
				Communication.getInstance().send(username, "requestType:2;result:" + (result?1:0));
			default:
				break;
			}

		}

	}


}
