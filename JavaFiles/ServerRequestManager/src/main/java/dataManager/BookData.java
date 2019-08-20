package dataManager;

import book.Book;

public class BookData implements BookQuery{

	public static BookData getInstance() {
		return null;
	}
	
	public boolean insertBook(Book book) {
		String query = "INSERT INTO LIBRO (BCID, TITOLO, AUTORE, DATAPUBBLICAZIONE, ISBN, PROPRIETARIO) VALUES (" 
	+ book.getBCID() + "," + book.getTitle() + "," + book.getAuthor() + "," + book.getYearOfPubblication() + "," + book.getISBN() + "," + book.getUsername()+");";
		return DBConnector.getInstance().executeQuery(query);	
	}
	
	
	public boolean isBCIDavailable(String BCID) {
		return ?? DBConnector.getInstance().executeQuery("SELECT Count(BCID) AS Result FROM Libro Where BCID = '"+ BCID +"'");
	}

}
