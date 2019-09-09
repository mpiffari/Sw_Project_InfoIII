package user;

import java.util.ArrayList;

import book.Book;

public interface UserManager {
	public double computeDistance(User o);
	public double computeDistance(UserLocalizationInfo o);
	public ArrayList<Book> getChasingBooks();
}
