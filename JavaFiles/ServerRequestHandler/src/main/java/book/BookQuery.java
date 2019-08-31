package book;

import java.util.ArrayList;

public interface BookQuery {
	
	public boolean insertBook(Book book);
	public boolean reserveBook(Book book, String username);
}
