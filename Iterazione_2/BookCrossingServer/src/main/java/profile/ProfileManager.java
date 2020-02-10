package profile;

import java.util.ArrayList;

import book.Book;
import dataManager.Localization;

public interface ProfileManager {
	public double computeDistance(Profile o);
	public double computeDistance(Localization o);
	public ArrayList<Book> getChasingBooks();
}
