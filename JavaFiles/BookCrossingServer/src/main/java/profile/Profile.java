package profile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import book.Book;
import dataManager.DBConnector;
import dataManager.Localization;
import dataManager.Queries;
import dataManager.ProfileData;

/**
 * 
 * User class - un istanza di questa classe descrive un utente della community di BookCrossing
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public class Profile implements Comparable<Profile>, ProfileManager {

	private String username;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String password;
	private Localization localization = new Localization();

	private ArrayList<Book> booksOwned = new ArrayList<Book>();


	/**
	 * 
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param dateOfBirth
	 * @param password
	 * @param latitude
	 * @param longitude
	 * @param action
	 */
	public Profile(String username, String firstName, String lastName, Date dateOfBirth, String password, double latitude,
			double longitude, double action) {

		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.password = password;
		this.localization.lat = latitude;
		this.localization.longit = longitude;
		this.localization.radius = action;
	}

	/**
	 * 
	 * @param username
	 * @param password
	 */
	public Profile(String username, String password){
		this.username = username;
		this.password = password;
	}

	/**
	 * 
	 * @param msg
	 */
	public Profile(String msg) {	
		String lines[] = msg.split(";");
		this.username = getUserFromString(lines[0]);
		this.password = getPasswordFromString(lines[1]);   
	}

	public Profile() {}

	private String getUserFromString(String msg) {
		String words[] = msg.split(":");
		return words[1];
	}

	private String getPasswordFromString(String msg) {
		String words[] = msg.split(":");
		return words[1];
	}

	public String getUsername() {
		return username.trim();
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPassword() {
		if(password == null) {
			return "null";
		} else {
			return password.trim();	
		}
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getLatitude() {
		return localization.lat;
	}

	public void setLatitude(double latitude) {
		this.localization.lat = latitude;
	}

	public double getLongitude() {
		return localization.longit;
	}

	public void setLongitude(double longitude) {
		this.localization.longit = longitude;
	}

	public double getActionArea() {
		return localization.radius;
	}

	public void setActionArea(double action) {
		this.localization.radius = action;
	}

	public LoginStatus login() {
		return ProfileData.getInstance().login(this);
	}

	public void setChasingBook(Book book) {
		this.booksOwned.add(book);
	}
	
	/**
	 * 
	 * @return ArrayList<Book> contiene i libri posseduti dall'utente
	 */
	public ArrayList<Book> getChasingBooks() {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.getBooksOwnedBy);
		try {
			stmt.setString(1, this.username);
			ResultSet rs = stmt.executeQuery();
			booksOwned.clear();
			Book b;
			while(rs.next()) {
				b = new Book(rs.getString(1), rs.getString(2));
				booksOwned.add(b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.booksOwned;
	}

	@Override
	public String toString() {
		return "USER: " + username + ";"
				+ "PASSWORD: " + password + "\r\n";
	}

	/**
	 * 
	 * @param o altro utente
	 * @return distance tra questo utente e l'utente o
	 */
	public double computeDistance(Profile o) {
		double lat_this = this.localization.lat;
		double long_this = this.localization.longit;
		double lat_o = o.localization.lat;
		double long_o = o.localization.longit;

		double distance = Math.sqrt(Math.pow((lat_this - lat_o), 2) + Math.pow((long_this - long_o),2));
		return distance;
	}

	
	/**
	 * 
	 * @param o altro utente
	 * @return distance tra questo utente e l'utente o
	 */
	public double computeDistance(Localization o) {
		double lat_this = this.localization.lat;
		double long_this = this.localization.longit;
		double lat_o = o.lat;
		double long_o = o.longit;

		double distance = Math.sqrt(Math.pow((lat_this - lat_o), 2) + Math.pow((long_this - long_o),2));
		return distance;
	}


	
	public int compareTo(Profile o) {
		// Ordinamento alfabetico in base all'username
		if(this.username.compareTo(o.getUsername()) > 0) {
			return 1;
		} else if(this.username.compareTo(o.getUsername()) < 0) {
			return -1;
		} else {
			return 0;
		}
	}
}
