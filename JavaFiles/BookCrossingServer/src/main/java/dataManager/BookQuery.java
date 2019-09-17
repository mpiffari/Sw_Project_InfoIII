package dataManager;

import java.util.ArrayList;

import algorithmReservationHandler.AlgorithmResult;
import book.Book;
/**
 * Interfaccia che contiene i metodi per gestire lo scambio di libri
 * nel sistema di book crossing.
 * 
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public interface BookQuery {

	public boolean insertBook(Book book);
	public AlgorithmResult reserveBook(Book book, String userThatMadeReservation);
	public ArrayList<Book> onRouteBooks(String user);
}
