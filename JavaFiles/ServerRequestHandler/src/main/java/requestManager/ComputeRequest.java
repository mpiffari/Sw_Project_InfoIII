package requestManager;

import book.Book;

public class ComputeRequest implements ProcessRequest{
	
	/**
	 * @param msg: is a string something like this 
	 * <request-Type>: number that specifies type of request;
	 * <other data>
	 */
	
	private final static String request = "requestType: 0; result: ";
	
	public void process(String msg, String username) {
		int i = msg.indexOf(";", 0);
		int j = msg.indexOf(":", 0);
		RequestType requestType = RequestType.valueOf(Integer.parseInt(msg.substring(0, i).substring(j+1)));
		
		
		switch(requestType) {
			case BOOK_REGISTRATION:
				Book b = new Book(msg.substring(i + 1));
				b.setProprietario(username);
				//TODO: remove the following line
				b.setISBN("esticazzi");
				
				boolean result = b.insert();
				Communication.getInstance().send(username, "Request of type 0 had this result: " + result);
				break;
			case BOOK_RESERVATION:
				Book book = new Book(msg.substring(i + 1));
				Communication.getInstance().send(username, "requestType: 1; result: " + book.reserve(username));
				break;
			default:
				break;
		}
		
		
		
	}
	

}
