package requestManager;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import book.Book;
import book.BookData;
import user.LoginStatus;
import user.UserData;
import user.User;

public class ComputeRequest implements ProcessRequest{

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
				b.setActualOwnerUsername(username);
				//TODO: add check of ISBN (iteration 2)
				b.setISBN("null");
				

				boolean result = b.insert();
				Communication.getInstance().send(username, "requestType:0;result:" + (result?1:0) + ";BCID:" + b.getBCID());
				break;
			case BOOK_RESERVATION:
				// msg = book.toString
				Book book = new Book(msg.substring(i + 1));
				boolean rs = book.reserve(username);
				//TODO: valuate which kind of response send back to user
				Communication.getInstance().send(username, "requestType:1;result:" + (rs?1:0));
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
					System.out.println(buildStringForUser(username));
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
				Communication.getInstance().send(username, "requestType:2;result:" + r + ";" + buildStringForUser(username));
				break;
			case BOOK_REGISTRATION_AUTOMATIC:
				//TODO: add structure of message for a better understanding of indexes
				b = new Book(msg.substring(i + 1));
				b.setActualOwnerUsername(username);
				result = b.insert();
				Communication.getInstance().send(username, "requestType:4;result:" + (result?1:0) + ";BCID:" + b.getBCID());
				break;
			case BOOK_SEARCH:
				// msg = TITLE:xxxxxxx;AUTHOR:xxxxxxxx --> at least one of this two fields
				String request = msg.substring(i + 1);
				System.out.println("request: " + request);
				//request: TITLE: titolo; AUTHOR: autore
				
				int y = request.indexOf(":", 0); //posizione :
				int k = request.indexOf(";", 0); //posizione ;
				ArrayList<Book> books = new ArrayList<Book>();;
				
				//System.out.println("index: " + (i) + " " + y);
				
				
				//System.out.println("titolo: " + request.substring(y + 1)); // stampa type: titolo
				//System.out.println(request.substring(0, y));
				//System.out.println(k);
				
				
				String type = request.substring(0, y); 
				
				if(k == -1) {
					//Search only by TITLE OR AUTHOR
					if(type.equals("TITLE")) {
						System.out.println("RICERCA PER TITOLO");
						String title = request.substring(y + 1);
						books = BookData.searchBookByTitle(title);
						
					} else if (type.equals("AUTHOR")) {
						System.out.println("RICERCA PER AUTORE");
						String author = request.substring(y + 1);
						System.out.println(author);
						books = BookData.searchBookByAuthor(author);
					}
				} else {
					//Search by TITLE AND AUTHOR
					System.out.println("RICERCA PER TUTTO");
					String title = request.substring(y+1, k);
					System.out.println(title);
					int z = request.indexOf(":", k);
					String author = request.substring(z+1);
					System.out.println(author + "");
					books = BookData.searchBook(title, author);
				}
				
				if(books.isEmpty()) {
					Communication.getInstance().send(username, "requestType:8;result:" + 0 + ";Books:");
				} else {
					// Response: Books:1;(books[1].toString());2;(books[2].toString());.......;(books[n].toString());
					int count = 0; //conta libri trovati
					//write as JSON
					JSONObject sendMsg = new JSONObject();
					JSONArray booksInMsg = new JSONArray();
					for (Book bb : books) 
					{ 
						booksInMsg.put(bb.toString());
					    count++;
					}
					
					sendMsg.put("size", count);
					sendMsg.put("books", booksInMsg);
					Communication.getInstance().send(username, "requestType:8;result:" + 1 + ";Books:" + sendMsg);
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
			case BOOK_SEARCH:
				reqTypeResponse = "8";
				break;
			default:
				break;
			}
			Communication.getInstance().send(username, "requestType:" + reqTypeResponse + ";result:KO_Username");
		}

	}

	
	private static String buildStringForUser(String username) {
		String msg = "";
		String previous = "";
		String next = "";
		for(String s : UserData.getInstance().pathOfUsers(username)) {
			int pos = UserData.getInstance().pathOfUsers(username).indexOf(s);
			if(s.indexOf(username) - 2 > 0 && s.indexOf(username) + 2 < s.length()) {
				previous = "" + s.charAt(s.indexOf(username) - 2);
				next = "" + s.charAt(s.indexOf(username) + 2);
			}else if(s.indexOf(username) == 0){
				next = "" + s.charAt(s.indexOf(username) + 2);
			}else if(s.indexOf(username) == s.length() - 2) {
				previous = "" + s.charAt(s.indexOf(username) - 2);
			}
			
			String pre = "";
			String post = "";
			
			if(!previous.equals("")) {
				pre = "deve ricevere da " + previous;
			}
			
			if(!next.equals("")) {
				post = " passare a " + next;
			}
			
			msg += username + " " + pre + post + " : " + BookData.getInstance().onRouteBooks(username).get(pos).getTitle() + " di " + BookData.getInstance().onRouteBooks(username).get(pos).getAuthor() + " + ";
		}
		return msg;
	}
	

}
