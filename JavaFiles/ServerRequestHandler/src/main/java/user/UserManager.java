package user;

import java.util.ArrayList;

import book.Book;
import dataManager.Localization;

public interface UserManager {
	public double computeDistance(User o);
	public double computeDistance(Localization o);
	public ArrayList<Book> getChasingBooks();
}
