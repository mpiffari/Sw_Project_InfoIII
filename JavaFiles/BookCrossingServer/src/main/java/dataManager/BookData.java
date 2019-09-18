package dataManager;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import algorithmReservationHandler.Algorithm;
import algorithmReservationHandler.AlgorithmResult;
import book.Book;
import profile.Profile;

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
		ArrayList<Profile> booker = book.getPrenotanti();
		if(booker.isEmpty()) {
			readerUsername = book.getActualOwnerUsername();
		} else {
			Profile lastBookerBeforeMe = booker.get(booker.size()-1);
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

		Profile l = Algorithm.getUserFromUsername(readerUsername);
		Profile me = Algorithm.getUserFromUsername(userThatMadeReservation);

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

		for(Profile u: result.userPath) {
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
	public static boolean isUnderReading(String bcid) {
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
	public static ResultSet getBookByTitle(String title) {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.searchByTitleQuery);
		ResultSet rs = null;
		try {
			stmt.setString(1, title.toLowerCase());
			rs = stmt.executeQuery();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return rs;
	}

	/**
	 * 
	 * @param author
	 * @return list of books that have author passed as parameter
	 */
	public static ResultSet getBookByAuthor(String author) {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.searchByAuthorQuery);
		ResultSet rs = null;
		
		try {
			stmt.setString(1, author.toLowerCase());
			rs = stmt.executeQuery();
		}catch (SQLException e) {
			e.printStackTrace();
		}

		return rs;
	}

	/**
	 * 
	 * @param title
	 * @param author
	 * @return list of books which has title and author passed as parameters
	 */
	public static ResultSet getBooks(String title, String author) {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.searchByTitleAndAuthorQuery);
		ResultSet rs = null;
		try {
			stmt.setString(1, title.toLowerCase());
			stmt.setString(2, author.toLowerCase());
			rs = stmt.executeQuery();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * @param user
	 * @return list of books that user has reserved
	 */
	public static ResultSet onRouteBooks(String user) {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.queryForUserNotifications);
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
}
