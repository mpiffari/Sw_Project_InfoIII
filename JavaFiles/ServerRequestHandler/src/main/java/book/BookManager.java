package book;

import java.util.ArrayList;

import user.User;

public interface BookManager {

	public ArrayList<User> getPrenotanti();
	public boolean reserve(String username);
	public boolean insert();
	public String generateBCID();
	public boolean setPrenotante(User user);
}
