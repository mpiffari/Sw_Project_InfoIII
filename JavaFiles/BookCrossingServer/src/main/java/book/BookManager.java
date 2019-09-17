package book;

import java.util.ArrayList;

import profile.Profile;

public interface BookManager {

	public ArrayList<Profile> getPrenotanti();
	public boolean reserve(String username);
	public boolean insert();
	public String generateBCID();
	public boolean setPrenotante(Profile user);
}
