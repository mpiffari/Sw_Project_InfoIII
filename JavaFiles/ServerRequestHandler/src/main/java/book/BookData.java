package book;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import algorithmReservationHandler.Algorithm;
import dataManager.DBConnector;
import dataManager.Queries;
import user.User;
import user.UserLocalizationInfo;

public class BookData implements BookQuery {

	private static BookData instance = null;

	private BookData() {}


	public static BookData getInstance() {
		if(instance == null)
			instance = new BookData();
		return instance;
	}

	public boolean insertBook(Book book) {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.insertBookQuery);

		int result = 0;
		try {
			stmt.setString(1, book.getBCID());
			stmt.setString(2, book.getTitle());
			stmt.setString(3, book.getAuthor());
			stmt.setString(4, book.getISBN());
			stmt.setString(5, book.getProprietario());
			stmt.setString(6, book.getType());	
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result==1 ? true : false;
	}

	public boolean isBCIDavailable(String BCID) {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.bcidAvailableQuery);

		try {
			stmt.setString(1, BCID);

			ResultSet rs = stmt.executeQuery();
			if(rs.next() && rs.getInt(1) == 0)	//if rs is not empty and query result is 0 then the BCID is available
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean reserveBook(Book book, String userThatMadeReservation) {
		
		String readerUsername = Algorithm.retrievingReader(book);
		if(readerUsername == null) {
			System.out.println("Error on retrieving reader L from POSSESSO table");
			return false;
		}
		System.out.println("User that own the book request is " + readerUsername);
		
		UserLocalizationInfo readerPos = Algorithm.userLocalization(readerUsername);
		if(readerPos == null) {
			System.out.println("Error on localization of reader L");
			return false;
		}
		UserLocalizationInfo userPos = Algorithm.userLocalization(userThatMadeReservation);
		if(userPos == null) {
			System.out.println("Error on localization of user");
			return false;
		}
		
		System.out.println("Position of reader (L): " + readerPos.toString());
		System.out.println("Position of booker (p): " + userPos.toString());
		
		User L = Algorithm.getUserFromUsername(readerUsername);
		TreeMap<User,Double> orderedUser = Algorithm.step_0(L, book);
		if(orderedUser == null) {
			System.out.println("Error on ordering user by distance");
			return false;
		}
		System.out.println("User ordered by position:\r\n" + orderedUser.toString());
		

		Map.Entry<User,Double> entry = orderedUser.entrySet().iterator().next();
		User nearestUser = entry.getKey();
		Algorithm.step_1(L, nearestUser);
		
		//TODO: use the result of the algorithm...
	}

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

	public static ArrayList<Book> searchBookByTitle(String title) {
		ArrayList<Book> result = new ArrayList<Book>();
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.searchByTitleQuery);

		try {
			stmt.setString(1, title);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				//System.out.println("#_ " + rs.getString(1) +"  " +rs.getString(2));
				String BCID = rs.getString(1);
				String t = rs.getString(2);
				String author = rs.getString(3);
				int pubbDate = Integer.parseInt(rs.getString(4) == null ? "0" : rs.getString(4));
				String isbn = rs.getString(5);
				String proprietario = rs.getString(6);

				//TODO: gestire Type e Edition number
				Book b = new Book(t,author, pubbDate, 0, rs.getString(7));
				b.setProprietario(proprietario);
				b.setBCID(BCID);
				b.setISBN(isbn);

				b.setUnderReading(isUnderReading(BCID));
				result.add(b);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static ArrayList<Book> searchBookByAuthor(String author) {
		ArrayList<Book> result = new ArrayList<Book>();
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.searchByAuthorQuery);

		try {
			stmt.setString(1, author);
			ResultSet rs = stmt.executeQuery();


			while(rs.next()) {
				//System.out.println("#_ " + rs.getString(1) +"  " +rs.getString(2));
				String BCID = rs.getString(1);
				String t = rs.getString(2);
				String a = rs.getString(3);
				int pubbDate = Integer.parseInt(rs.getString(4) == null ? "0" : rs.getString(4));
				String isbn = rs.getString(5);
				String proprietario = rs.getString(6);

				//TODO: gestire Type e Edition number
				Book b = new Book(t,a, pubbDate, 0, rs.getString(7));
				b.setProprietario(proprietario);
				b.setBCID(BCID);
				b.setISBN(isbn);
				b.setUnderReading(isUnderReading(BCID));
				result.add(b);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static ArrayList<Book> searchBook(String title, String author) {
		ArrayList<Book> result = new ArrayList<Book>();
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.searchByTitleAndAuthorQuery);

		try {
			stmt.setString(1, title);
			stmt.setString(2, author);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				//System.out.println("#_ " + rs.getString(1) +"  " +rs.getString(2));
				String BCID = rs.getString(1);
				String t = rs.getString(2);
				String a = rs.getString(3);
				int pubbDate = Integer.parseInt(rs.getString(4) == null ? "0" : rs.getString(4));
				String isbn = rs.getString(5);
				String proprietario = rs.getString(6);
				

				//TODO: gestire Type e Edition number
				Book b = new Book(t,a, pubbDate, 0, rs.getString(7));
				b.setProprietario(proprietario);
				b.setBCID(BCID);
				b.setISBN(isbn);
				b.setUnderReading(isUnderReading(BCID));
				result.add(b);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
