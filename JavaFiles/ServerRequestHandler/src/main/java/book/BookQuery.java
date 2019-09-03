package book;

import algorithmReservationHandler.AlgorithmResult;

public interface BookQuery {
	
	public boolean insertBook(Book book);
	public AlgorithmResult reserveBook(Book book, String userThatMadeReservation);
}
