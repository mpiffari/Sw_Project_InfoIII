package dataManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import book.Book;
import book.BookType;

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

}
