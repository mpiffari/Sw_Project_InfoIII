package book;

import java.util.ArrayList;

import algorithmReservationHandler.AlgorithmResult;

public interface BookQuery {
	
	public boolean insertBook(Book book);
	public AlgorithmResult reserveBook(Book book, String userThatMadeReservation);
	public ArrayList<Book> onRouteBooks(String user);
}
