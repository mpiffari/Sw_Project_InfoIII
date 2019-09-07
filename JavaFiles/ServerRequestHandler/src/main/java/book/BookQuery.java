package book;

import java.util.ArrayList;

import algorithmReservationHandler.AlgorithmResult;
/**
 * 
 * inteface that contains methods for managing book in book crossing program
 *
 *@author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public interface BookQuery {

	public boolean insertBook(Book book);
	public AlgorithmResult reserveBook(Book book, String userThatMadeReservation);
	public ArrayList<Book> onRouteBooks(String user);
}
