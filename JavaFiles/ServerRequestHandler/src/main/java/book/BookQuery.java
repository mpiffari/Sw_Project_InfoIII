package book;

import java.util.ArrayList;

import algorithmReservationHandler.AlgorithmResult;
/**
 * 
 * @author Gruppo Paganessi - Piffari - Villa
 * inteface that contains methods for managing book in book crossing program
 */
public interface BookQuery {
	
	public boolean insertBook(Book book);
	public AlgorithmResult reserveBook(Book book, String userThatMadeReservation);
	public ArrayList<Book> onRouteBooks(String user);
}
