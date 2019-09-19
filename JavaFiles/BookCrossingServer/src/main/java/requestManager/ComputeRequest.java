package requestManager;

import java.util.ArrayList;

import javax.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;
import book.Book;
import profile.LoginStatus;
import profile.Profile;
import requestManager.communication.Communication;


/**
 * 
 * Classe che si occupa di andare a processare la stringa ricevuta dal client,
 * andando a smistare, a seconda della tipologia della richiesta ricevuta,
 * le azioni da eseguire. 
 * 
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 *
 */
public class ComputeRequest implements ProcessRequest{

	public final static ComputeRequest computeRequestSingleton = new ComputeRequest();

	public final void process(String msg, String username) {

		// First information is the request type, that is encoded in this way: REQUESTTYPE:...;
		int i = msg.indexOf(";", 0);
		int j = msg.indexOf(":", 0);
		// Convert the String information of the request type to an enum
		@Nullable RequestType requestType = RequestType.getEnumReqType(msg.substring(0, i).substring(j+1));

		if(requestType == null) {
			// In this case, in the request, ther's a wrong encoded request type
			Communication.getInstance().send(username, "requestType:" + 10000 + ";result:KO_RequestType");
			return;
		}

		// Check if an user exists in db, else send back a bad response
		if(Profile.existUser(username)) {
			System.out.println("Message under processing: " + msg);
			System.out.println(msg.substring(0, i));
			System.out.println(msg.substring(0, i).substring(j+1));

			switch(requestType) {
			// ====================================== ITERAZIONE 1 ===========================================================================
			case BOOK_REGISTRATION_MANUAL:
				Book b = new Book(msg.substring(i + 1));
				b.setActualOwnerUsername(username);
				//TODO: add check of ISBN (iteration 2)
				b.setISBN("null");

				boolean result = b.insert();
				Communication.getInstance().send(username, "requestType:0;result:" + (result?1:0) + ";BCID:" + b.getBCID());
				break;

			case BOOK_SEARCH:
				// msg = TITLE:xxxxxxx;AUTHOR:xxxxxxxx --> at least one of this two fields is filled
				String request = msg.substring(i + 1);
				System.out.println("request: " + request);

				int y = request.indexOf(":", 0); //Position of :
				int k = request.indexOf(";", 0); //Position of ;
				ArrayList<Book> books = new ArrayList<Book>();;

				// Debug print
				//System.out.println("index: " + (i) + " " + y);
				//System.out.println("titolo: " + request.substring(y + 1)); // stampa type: titolo
				//System.out.println(request.substring(0, y));
				//System.out.println(k);

				String type = request.substring(0, y); 

				if(k == -1) {
					if(type.equals("TITLE")) {
						System.out.println("Search by title");
						String title = request.substring(y + 1);
						books = Book.searchBookByTitle(title);
					} else if (type.equals("AUTHOR")) {
						System.out.println("Search by aithor");
						String author = request.substring(y + 1);
						System.out.println(author);
						books = Book.searchBookByAuthor(author);
					}
				} else {
					System.out.println("Search by title and author");
					String title = request.substring(y+1, k);
					System.out.println(title);
					int z = request.indexOf(":", k);
					String author = request.substring(z+1);
					System.out.println(author + "");
					books = Book.searchBook(title, author);
				}

				if(books.isEmpty()) {
					Communication.getInstance().send(username, "requestType:8;result:" + 0 + ";Books:");
				} else {
					// Response: Books:1;(books[1].toString());2;(books[2].toString());.......;(books[n].toString());
					int count = 0; //Counter of the books

					//Write response as JSON
					JSONObject jsonResponse = new JSONObject();
					JSONArray booksInMsg = new JSONArray();
					for (Book bb : books) 
					{ 
						booksInMsg.put(bb.toString());
						count++;
					}	
					jsonResponse.put("size", count);
					jsonResponse.put("books", booksInMsg);
					Communication.getInstance().send(username, "requestType:8;result:" + 1 + ";Books:" + jsonResponse);
				}
				break;

				// ====================================== ITERAZIONE 2 ==========================================================================
			case BOOK_RESERVATION:
				Book book = new Book(msg.substring(i + 1));
				boolean rs = book.reserve(username);
				Communication.getInstance().send(username, "requestType:1;result:" + (rs?1:0));
				break;
			case LOGIN:
				// i is the index of the first semi colon: now we want to consider all the request, after the request type field
				Profile user = new Profile(msg.substring(i + 1));

				LoginStatus res = user.login();
				//String r = "";
				switch (res) {
				case SUCCESS:
					System.out.println("Login ok for user "+ user.getUsername() + " with pwd " + user.getPassword());
					System.out.println(buildStringForUser(username));
					Communication.getInstance().send(username, "requestType:2;result:" + "Success" + ";" + buildStringForUser(username));
					break;
				case WRONG_USERNAME:
					System.out.println("KO_Username for user "+ user.getUsername());
					Communication.getInstance().send(username, "requestType:2;result:" + "KO_Username" + ";");
					break;
				case WRONG_PWD:
					System.out.println("KO_Password for user "+ user.getUsername() + " with KO pwd " + user.getPassword());
					Communication.getInstance().send(username, "requestType:2;result:" + "KO_Password" + ";");
					break;
				default:
					break;
				}
				break;
			case BOOK_REGISTRATION_AUTOMATIC:
				// i is the index of the first semi colon: now we want to consider all the request, after the request type field
				b = new Book(msg.substring(i + 1));
				b.setActualOwnerUsername(username);

				result = b.insert();
				Communication.getInstance().send(username, "requestType:4;result:" + (result?1:0) + ";BCID:" + b.getBCID());
				break;
				// ======================================= NEXT ITERATION =========================================================
			case PICK_UP:
				break;
			case PROFILE_INFO:
				break;
			case SIGN_IN:
				break;
			case TAKEN_BOOKS:
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
			case BOOK_REGISTRATION_AUTOMATIC:
				reqTypeResponse = "4";
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
			return;
		}
	}

	/**
	 * Metodo per andare a creare la "chain" di utenti per la gestione delle prenotazioni
	 * in cui l'utente username è coinvolto
	 * @param username
	 * @return
	 */
	private static String buildStringForUser(String username) {
		String msg = "";
		String previous = "";
		String next = "";

		for(String s : Profile.pathOfUsers(username)) {
			int pos = Profile.pathOfUsers(username).indexOf(s);
			if(s.indexOf(username) - 2 > 0 && s.indexOf(username) + 2 < s.length()) {
				previous = "" + s.charAt(s.indexOf(username) - 2);
				next = "" + s.charAt(s.indexOf(username) + 2);
			} else if(s.indexOf(username) == 0){
				next = "" + s.charAt(s.indexOf(username) + 2);
			} else if(s.indexOf(username) == s.length() - 2) {
				previous = "" + s.charAt(s.indexOf(username) - 2);
			}

			String pre = "";
			String post = "";

			if(!previous.equals("")) {
				pre = "deve ricevere da " + previous;
			}

			if(!next.equals("")) {
				post = " e passare a " + next;
			}

			msg += username + " " + pre + post + " : " + Book.onRouteBooks(username).get(pos).getTitle() + " di " + Book.onRouteBooks(username).get(pos).getAuthor() + " + ";
		}
		return msg;
	}


}
