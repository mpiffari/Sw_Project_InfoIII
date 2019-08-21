package dataManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import book.Book;
import book.BookType;
import user.User;

public class BookData implements BookQuery{

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
		String sql = "INSERT INTO LIBRO (BCID, TITOLO, AUTORE, ISBN, PROPIETARIO) VALUES (?,?,?,?,?)";
		
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(sql);
		
		int result = 0;
		try {
		
			stmt.setString(1, book.getBCID());
			stmt.setString(2, book.getTitle());
			stmt.setString(3, book.getAuthor());
			stmt.setString(4, book.getISBN());
			stmt.setString(5, book.getProprietario());

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
	
	public int reserveBook(Book book, String username) {
		if(searchBook(book.getTitle()) != null) {
			ResultSet rs = searchBook(book.getTitle()); //ho i libri che cerco e relative posizioni
			
			//algoritmo->da sopra ho posizione del libro. con query database sfruttando param user ricavo posizione di chi prenota
			
			return 1;
		}
		else {
			//TODO libro non trovato
			return 0;
		}
		
		
	}
	
	/**
	 * ricerca per titolo di libri che sono in possesso
	 * @param book
	 * @return 
	 */
	
	public ResultSet searchBook(String title) {
		String sql = "SELECT Titolo, Autore FROM Libro AS L JOIN Possesso AS P ON L.BCID = P.LIBRO WHERE Titolo = ?";
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(sql);
		try {
			stmt.setString(1, title);
			ResultSet rs = stmt.executeQuery();
			if(rs.getRow() > 0)	//if rs return more than one row -> there are books
				return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
