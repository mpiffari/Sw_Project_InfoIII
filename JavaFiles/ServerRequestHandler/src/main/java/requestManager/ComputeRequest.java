package requestManager;

import book.Book;

public class ComputeRequest implements ProcessRequest{
	
	/**
	 * @param msg: is a string somthing like this request-Type:<number that specifies the quest>;<other data>
	 */
	public void process(String msg, String username) {
		int i = msg.indexOf(";", 0);
		String str = msg.substring(0, i);
		int j = msg.indexOf(":", 0);
		
		switch(Integer.parseInt(str.substring(j+1))) {
			case 0:
				Book b = new Book(msg.substring(i + 1));
				b.setProprietario(username);
				//TODO: remove the following line
				b.setISBN("esticazzi");
				
				Communication.getInstance().send(username, "requestType: 0; result: " + b.insert());
				break;
			case 1:
				break;
			case 2:
				break;
		}
		
		
		
	}
	

}
