package user;

import java.util.Date;

public class User {

	private String username;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String password;
	private double latitude;
	private double longitude;
	private double action;

	public User(String username, String firstName, String lastName, Date dateOfBirth, String password, double latitude,
			double longitude, double action) {
		
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.password = password;
		this.latitude = latitude;
		this.longitude = longitude;
		this.action = action;
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
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAction() {
		return action;
	}

	public void setAction(double action) {
		this.action = action;
	}
	
	public LoginStatus login() {
		return UserData.getInstance().login(this);
	}
	
	@Override
	public String toString() {
		return "USER: " + username + ";"
				+ "PASSWORD: " + password;
	}

}
