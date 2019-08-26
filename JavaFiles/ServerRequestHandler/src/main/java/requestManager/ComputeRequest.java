package requestManager;

import java.util.ArrayList;

import book.Book;
import book.BookData;
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
				b.setISBN("null");

				boolean result = b.insert();
				Communication.getInstance().send(username, "requestType:0;result:" + (result?1:0) + ";BCID:" + b.getBCID());
				break;
			case BOOK_RESERVATION:
				//TODO: add structure of message for a better understanding of indexes
				Book book = new Book(msg.substring(i + 1));
				Communication.getInstance().send(username, "requestType:1;result:" + book.reserve(username));
				break;
			case LOGIN:
				//TODO: add structure of message for a better understanding of indexes
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
				break;
			case BOOK_REGISTRATION_AUTOMATIC:
				//TODO: add structure of message for a better understanding of indexes
				b = new Book(msg.substring(i + 1));
				b.setProprietario(username);
				int pos_ISBN = msg.indexOf(",", i + 1);
				b.setISBN(msg.substring(pos_ISBN + 1));
				result = b.insert();
				Communication.getInstance().send(username, "requestType:4;result:" + (result?1:0) + ";BCID:" + b.getBCID());
				break;
			case BOOK_SEARCH:
				// msg = TITLE:xxxxxxx;AUTHOR:xxxxxxxx --> at least one of this two fields
				String request = msg.substring(i + 1);
				int y = request.indexOf(":", 0);
				int k = request.indexOf(";", 0);
				ArrayList<Book> books = new ArrayList<Book>();;
				
				if(k == -1) {
					//Search only by TITLE OR AUTHOR
					String type = msg.substring(i + 1, y);
					if(type == "TITLE") {
						String title = msg.substring(y+1);
						books = BookData.searchBookByTitle(title);
					} else if (type =="AUTHOR") {
						String author = msg.substring(y+1);
						books = BookData.searchBookByTitle(author);
					}
				} else {
					//Search by TITLE AND AUTHOR
					String title = msg.substring(y+1, k);
					int z = request.indexOf(":", k);
					String author = msg.substring(z+1);
					books = BookData.searchBook(title, author);
				}
				
				if(books.isEmpty()) {
					Communication.getInstance().send(username, "requestType:8;result:" + 0 + ";Books:");
				} else {
					// Response: Books:1;(books[1].toString());2;(books[2].toString());.......;(books[n].toString());
					int index = 1;
					String resres = "";
					for (Book bb : books) 
					{ 
					    resres = resres + index + ";" + bb.toString() + ";";
					}
					Communication.getInstance().send(username, "requestType:8;result:" + 1 + ";Books:" + resres);
				}
				
				break;
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
