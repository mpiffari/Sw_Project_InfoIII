package book;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import dataManager.DBConnector;

class Coordinate {
	public double lat;
	public double longit;
	
	public Coordinate() {
		
	}
}

public class BookData implements BookQuery {

	private static BookData instance = null;

	private BookData() {}


	public static BookData getInstance() {
		if(instance == null)
			instance = new BookData();
		return instance;
	}

	//main per provare il metodo insertBook
	/*public static void main(String[] args) {
		Book b = new Book("A", "A", 1, 1, BookType.HORROR);
		b.setProprietario("Pippo");
		b.setISBN("aa");
		BookData.getInstance().insertBook(b);
	}*/

	public boolean insertBook(Book book) {
		String sql = "INSERT INTO LIBRO (BCID, TITOLO, AUTORE, ISBN, PROPRIETARIO, GENERE) VALUES (?,?,?,?,?,?)";
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(sql);

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result==1 ? true : false;
	}

	//main per provare il metodo isBCIDavaialble
	/*public static void main(String[] args) {
		BookData.getInstance().isBCIDavailable("A");
	}*/

	public boolean isBCIDavailable(String BCID) {
		String sql = "SELECT Count(BCID) AS Result FROM Libro Where BCID = ?";
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(sql);

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

	/**
	 * prenota libro-> sfrutto metodo cerca
	 * @param book
	 * @return
	 */

	@SuppressWarnings("null")
	public boolean reserveBook(Book book, String username) {
		//TODO: cercare informazioni sul lettore che ha in possesso il libro
		double r_l = 0.0; // Raggio d'azione lettore
		Coordinate z_l = new Coordinate(); // Zona di residenza lettore
		
		// Ricerca dati lettore
		String bookBCIDForReservation = book.getBCID();
		String query1 = "SELECT USERNAME FROM Possesso Where BCID = ?";
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(query1);

		try {
			stmt.setString(1, bookBCIDForReservation);
			ResultSet rs = stmt.executeQuery();
			String readerUsername = null;
			if(rs.next()) {
				readerUsername = rs.getString(1);
			}
			
			if(readerUsername == null) {
				return false;
			} else {
				String query2 = "SELECT RESIDENZALAT, RESIDENZALONG, RAGGIOAZIONE FROM Utente Where USERNAME = ?";
				stmt = DBConnector.getDBConnector().prepareStatement(query2);
				stmt.setString(1, readerUsername);
				rs = stmt.executeQuery();
				if(rs.next()) {
					z_l.lat = rs.getDouble(1);
					z_l.longit = rs.getDouble(2); 
					r_l = rs.getDouble(3);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(searchBookByTitle(book.getTitle()) != null) {
			//ResultSet rs = searchBook(book.getTitle()); //ho i libri che cerco e relative posizioni

			//algoritmo->da sopra ho posizione del libro. con query database sfruttando param user ricavo posizione di chi prenota

			return true;
		}
		else {
			//TODO libro non trovato
			return false;
		}


	}

	/**
	 * ricerca il libro per titolo
	 * @param book
	 * @return 
	 */

	private static boolean isUnderReading(String bcid) {
		String sqlState = "SELECT COUNT(LIBRO) FROM Possesso WHERE LIBRO = ? AND DATAFINE IS NULL AND LUOGORILASCIO IS NULL";
		PreparedStatement stmtState = DBConnector.getDBConnector().prepareStatement(sqlState);
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

		//String sql = "SELECT Titolo, Autore FROM Libro AS L JOIN Possesso AS P ON L.BCID = P.LIBRO WHERE Titolo = ?";
		String sql = "SELECT * FROM Libro L WHERE Titolo = ?";

		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(sql);

		try {
			stmt.setString(1, title);
			//stmtState.setString(1, title);

			ResultSet rs = stmt.executeQuery();
			//ResultSet rsState = stmtState.executeQuery();

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

				//rsState.next();
				b.setUnderReading(isUnderReading(BCID));

				result.add(b);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * ricerca il libro per autore
	 * @param book
	 * @return 
	 */
	public static ArrayList<Book> searchBookByAuthor(String author) {
		ArrayList<Book> result = new ArrayList<Book>();

		//String sql = "SELECT Titolo, Autore FROM Libro AS L JOIN Possesso AS P ON L.BCID = P.LIBRO WHERE Titolo = ?";
		String sql = "SELECT * FROM Libro L WHERE Autore = ?";
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(sql);

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

	/**
	 * ricerca il libro per titolo AND autore
	 * @param book
	 * @return 
	 */
	public static ArrayList<Book> searchBook(String title, String author) {
		ArrayList<Book> result = new ArrayList<Book>();

		//String sql = "SELECT Titolo, Autore FROM Libro AS L JOIN Possesso AS P ON L.BCID = P.LIBRO WHERE Titolo = ?";
		String sql = "SELECT * FROM Libro L WHERE Titolo = ? AND Autore = ?";
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(sql);
		

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
