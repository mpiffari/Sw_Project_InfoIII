package book;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import algorithmReservationHandler.Algorithm;
import algorithmReservationHandler.AlgorithmResult;
import dataManager.DBConnector;
import dataManager.Localization;
import dataManager.Queries;
import user.User;

/**
 * 
 * BookData implementata come oggetto singleton, richiedendo l'interfaccia BookQuery
 * 
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public final class BookData implements BookQuery {

	private static BookData instance = null;

	private BookData() {}


	/**
	 * 
	 * @return BookData singleton instance
	 */
	public static BookData getInstance() {
		if(instance == null)
			instance = new BookData();
		return instance;
	}

	/**
	 * @param book Libro che si desidera registrare su databse
	 * @return true se la query di insert è andata a buon fine
	 */
	public boolean insertBook(Book book) {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.insertBookQuery);

		int result = 0;
		try {
			stmt.setString(1, book.getBCID().toLowerCase());
			stmt.setString(2, book.getTitle().toLowerCase());
			stmt.setString(3, book.getAuthor().toLowerCase());
			stmt.setString(4, String.valueOf(book.getYearOfPubblication()).toLowerCase());
			stmt.setString(5, book.getISBN().toLowerCase());
			stmt.setString(6, book.getActualOwnerUsername());
			stmt.setString(7, book.getType().toLowerCase());	
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result==1 ? true : false;
	}

	/**
	 * 
	 * @param bcid BookCrossing ID number
	 * @return true se il codice BCID passato come stringa è disponibile, potendo quidni essere assegnato.
	 */
	public boolean isBCIDavailable(final String bcid) {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.bcidAvailableQuery);

		try {
			stmt.setString(1, bcid);
			ResultSet rs = stmt.executeQuery();
			if(rs.next() && rs.getInt(1) == 0)	//if rs is not empty and query result is 0 then the BCID is available
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * @param book libro oggetto della prenotazione
	 * @param userThatMadeReservation utente richiedente il libro
	 */
	public AlgorithmResult reserveBook(Book book, String userThatMadeReservation) {
		AlgorithmResult result = new AlgorithmResult();
		String readerUsername = "";
		ArrayList<User> booker = book.getPrenotanti();
		if(booker.isEmpty()) {
			readerUsername = book.getActualOwnerUsername();
		} else {
			User lastBookerBeforeMe = booker.get(booker.size()-1);
			readerUsername = lastBookerBeforeMe.getUsername();
			if(readerUsername.equals(userThatMadeReservation)) {
				System.out.println("You are the last user that made reservation for this book!!!");
				result.resultFlag = true;
				return result;
			}
		}


		if(readerUsername == null) {
			System.out.println("Error on retrieving reader L from POSSESSO table");
			result.resultFlag = false;
			return result;
		}
		System.out.println("User that own the book request is " + readerUsername);

		User l = Algorithm.getUserFromUsername(readerUsername);
		User me = Algorithm.getUserFromUsername(userThatMadeReservation);

		if(booker.contains(me)) {
			System.out.println("You've already done the reservation!!!");
			result.resultFlag = true;
			return result;
		}

		Localization readerPos = new Localization(l.getLatitude(), l.getLongitude());
		Localization userPos = new Localization(me.getLatitude(), me.getLongitude());

		System.out.println("Position of reader (L): " + readerPos.toString());
		System.out.println("Position of me: " + userPos.toString());
		System.out.println("Distance of mine from " + l.getUsername() +" = " + me.computeDistance(l));

		result = Algorithm.step_1(l, me);

		for(User u: result.userPath) {
			System.out.println("Name: " + u.getFirstName() + " Surname: " 
					+ u.getLastName() + " Latitudine: " + u.getLatitude() + " Longit: " + u.getLongitude());
		}

		return result;
	}

	/**
	 * 
	 * @param bcid
	 * @return true if book is under reading by other user
	 */
	private static boolean isUnderReading(String bcid) {
		PreparedStatement stmtState = DBConnector.getDBConnector().prepareStatement(Queries.underReadingBookQuery);
		try {
			stmtState.setString(1, bcid);
			ResultSet rs = stmtState.executeQuery();
			if(rs.next() && rs.getInt(1) != 0) {
				//System.out.println("#_ " + rs.getString(1) +"  " +rs.getString(2));
				return true;
			}
			else {
				return false;
			}	
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @param title
	 * @return list of books that has title passed as parameter
	 */
	public static ArrayList<Book> searchBookByTitle(String title) {
		ArrayList<Book> result = new ArrayList<Book>();
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.searchByTitleQuery);

		try {
			stmt.setString(1, title.toLowerCase());
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				//System.out.println("#_ " + rs.getString(1) +"  " +rs.getString(2));
				String bcid = rs.getString(1);
				String t = rs.getString(2);
				String a = rs.getString(3);
				int pubbDate = Integer.parseInt(rs.getString(4) == null ? "0" : rs.getString(4));
				int editionNumber = 0; //TODO
				String isbn = rs.getString(5);
				String bookType = rs.getString(6);
				String actualOwner = rs.getString(7);

				//TODO: gestire Type e Edition number
				Book b = new Book(t,a, pubbDate, editionNumber, bookType);
				b.setActualOwnerUsername(actualOwner);
				b.setBCID(bcid);
				b.setISBN(isbn);
				b.setUnderReading(isUnderReading(bcid));
				result.add(b);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 
	 * @param author
	 * @return list of books that have author passed as parameter
	 */
	public static ArrayList<Book> searchBookByAuthor(String author) {
		ArrayList<Book> result = new ArrayList<Book>();
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.searchByAuthorQuery);

		try {
			stmt.setString(1, author.toLowerCase());
			ResultSet rs = stmt.executeQuery();


			while(rs.next()) {
				//System.out.println("#_ " + rs.getString(1) +"  " +rs.getString(2));
				String bcid = rs.getString(1);
				String t = rs.getString(2);
				String a = rs.getString(3);
				int pubbDate = Integer.parseInt(rs.getString(4) == null ? "0" : rs.getString(4));
				int editionNumber = 0; //TODO
				String isbn = rs.getString(5);
				String bookType = rs.getString(6);
				String actualOwner = rs.getString(7);

				//TODO: gestire Type e Edition number
				Book b = new Book(t,a, pubbDate, editionNumber, bookType);
				b.setActualOwnerUsername(actualOwner);
				b.setBCID(bcid);
				b.setISBN(isbn);
				b.setUnderReading(isUnderReading(bcid));
				result.add(b);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 
	 * @param title
	 * @param author
	 * @return list of books which has title and author passed as parameters
	 */
	public static ArrayList<Book> searchBook(String title, String author) {
		ArrayList<Book> result = new ArrayList<Book>();
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.searchByTitleAndAuthorQuery);

		try {
			stmt.setString(1, title.toLowerCase());
			stmt.setString(2, author.toLowerCase());
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				//System.out.println("#_ " + rs.getString(1) +"  " +rs.getString(2));
				String bcid = rs.getString(1);
				String t = rs.getString(2);
				String a = rs.getString(3);
				int pubbDate = Integer.parseInt(rs.getString(4).equals("null") ? "0" : rs.getString(4));
				int editionNumber = 0; //TODO
				String isbn = rs.getString(5);
				String bookType = rs.getString(6);
				String actualOwner = rs.getString(7);

				//TODO: gestire Type e Edition number
				Book b = new Book(t,a, pubbDate, editionNumber, bookType);
				b.setActualOwnerUsername(actualOwner);
				b.setBCID(bcid);
				b.setISBN(isbn);
				b.setUnderReading(actualOwner.equals("null")? false : true);
				result.add(b);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param user
	 * @return list of books that user has reserved
	 */
	public ArrayList<Book> onRouteBooks(String user) {
		ArrayList<Book> booksOnRoute = new ArrayList<Book>();
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.queryForUserNotifications);
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				if(rs.getString(3).contains(user)){
					Book b = new Book();
					b.setTitle(rs.getString(1));
					b.setAuthor(rs.getString(2));
					booksOnRoute.add(b);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return booksOnRoute;
	}
}
