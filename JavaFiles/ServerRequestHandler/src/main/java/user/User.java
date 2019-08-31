package user;

import java.util.ArrayList;
import java.util.Date;

import book.Book;

public class User {

	private String username;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String password;
	private UserLocalizer localization;
	//TODO: valutare di togliere, e fare query a tabella "POSSESSO"
	private ArrayList<Book> booksOwned = new ArrayList<Book>();

	public User(String username, String firstName, String lastName, Date dateOfBirth, String password, double latitude,
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
	
	public User(String username, String password){
        this.username = username;
        this.password = password;
    }
	
	public User(String msg) {	
    	String lines[] = msg.split(";");
        this.username = getUserFromString(lines[0]);
        this.password = getPasswordFromString(lines[1]);   
    }
	
	public User() {}

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
		return password.trim();
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
		return UserData.getInstance().login(this);
	}
	
	public void setChasingBook(Book book) {
		this.booksOwned.add(book);
	}
	
	public ArrayList<Book> getChasingBooks() {
		return this.booksOwned;
	}
	
	@Override
	public String toString() {
		return "USER: " + username + ";"
				+ "PASSWORD: " + password;
	}

	public double computeDistance(User o) {
		double lat_this = this.localization.lat;
		double long_this = this.localization.longit;
		double lat_o = o.localization.lat;
		double long_o = o.localization.longit;
		
		double distance = Math.sqrt(Math.pow((lat_this - lat_o), 2) + Math.pow((long_this - long_o),2));
		return distance;
	}
}
