package dataManager;

import java.util.ArrayList;

import book.Book;

public interface BookQuery {
	
	public boolean insertBook(Book book);
	public ArrayList<Book> searchBook(String title);
	public int reserveBook(Book book, String username);
	

}
