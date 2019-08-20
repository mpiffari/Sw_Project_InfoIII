package requestManager;

import book.Book;

public class ComputeRequest implements ProcessRequest{
	
	public void process(String msg, String username) {
		int i = msg.indexOf(";", 0);
		String str = msg.substring(0, i);
		
		int j = msg.indexOf(":", 0);
		switch(Integer.parseInt(str.substring(j))) {
			case 0:
				Book b = new Book(str.substring(i + 1));
				Communication.getInstance().send(username, "requestType: 0; result: " + b.insert());
				break;
			case 1:
				break;
			case 2:
				break;
		}
		
		
		
	}
	

}
