package book;

public interface BookQuery {
	
	public boolean insertBook(Book book);
	public boolean reserveBook(Book book, String userThatMadeReservation);
}
