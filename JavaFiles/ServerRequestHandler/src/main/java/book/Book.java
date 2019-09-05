package book;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import algorithmReservationHandler.Algorithm;
import algorithmReservationHandler.AlgorithmResult;
import dataManager.DBConnector;
import dataManager.Queries;
import user.User;

/**
 * 
 * 
 * String format received from client:
 * - TITLE:.....;
 * - AUTHOR:.....;
 * - YEAR:.....;
 * - EDITION:.....;
 * - TYPE:.....;
 *
 */

public class Book {

	private String title;
	private String author;
	private int yearOfPubblication;
	private int editionNumber;
	private String type;
	private String BCID;
	private String ISBN;
	private String actualOwnerUsername;
	private boolean underReading;
	private ArrayList<User> prenotanti = new ArrayList<User>();
	private int idPrenotazione; 
	
	public Book(String msg) {	
		this();        
		String lines[] = msg.split(";");
		this.title = getTitleFromString(lines[0]);
		this.author = getAuthorFromString(lines[1]);
		this.yearOfPubblication = getYearOfPubblicationFromString(lines[2]);
		this.editionNumber = getEditionNumberFromString(lines[3]);
		this.type = getBookTypeFromString(lines[4]);
		this.actualOwnerUsername = getUserFromString(lines[5]);
		this.ISBN = getISBNFromString(lines[6]);
		this.underReading = getStateFromString(lines[7]);
		//this.BCID = getBCIDFromString(lines[8]);
	}

	public Book(String title, String author, int yearOfPubblication, int editionNumber, String type) {
		this();
		this.title = title;
		this.author = author;
		this.yearOfPubblication = yearOfPubblication;
		this.editionNumber = editionNumber;
		this.type = type;
	}

	public Book(String BCID, String owner) {
		this();
		this.BCID = BCID;
		this.actualOwnerUsername = owner;
	}
	
	public Book() {
		do {
			BCID = generateBCID();
		}while(!BookData.getInstance().isBCIDavailable(BCID));
	}


	public boolean insert() {
		return BookData.getInstance().insertBook(this);
	}

	public boolean reserve(String username) {
		User u = new User();
		u.setUsername(username);
		AlgorithmResult res = BookData.getInstance().reserveBook(this, username);
		if(!(res.userPath.isEmpty()) && res.resultFlag == true){
			setPrenotante(u);
			if(Algorithm.savePath(res.userPath)) {
				return true;
			} else {
				return false;
			}
		} else if(res.resultFlag == false) {
			return false;
		}
		return false;
	}

	public ArrayList<User> getPrenotanti() {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.getUserInfoByJoin);
		try {
			stmt.setString(1, this.BCID);
			ResultSet rs = stmt.executeQuery();
			prenotanti.clear();
			User u;
			while(rs.next()) {
				u = new User();
				u.setUsername(rs.getString(1));
				u.setFirstName(rs.getString(5));
				u.setLastName(rs.getString(6));
				u.setDateOfBirth(rs.getDate(7));
				u.setPassword(rs.getString(9));
				u.setLatitude(rs.getBigDecimal(10).doubleValue());
				u.setLongitude(rs.getBigDecimal(11).doubleValue());
				u.setActionArea(rs.getBigDecimal(12).doubleValue());
				prenotanti.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.prenotanti;
	}

	
	
	public boolean setPrenotante(User user) {

		PreparedStatement statement = DBConnector.getDBConnector().prepareStatement(Queries.insertNewReservationQuery);
		        
		int result = 0;
		try {			
			statement.setString(1, user.getUsername());
			statement.setString(2, this.BCID);
			result = statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result == 1 ? true : false;
		
	}

	public String getActualOwnerUsername() {
		return actualOwnerUsername;
	}

	public void setActualOwnerUsername(String actualOwner) {
		this.actualOwnerUsername = actualOwner;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public String getBCID() {
		return BCID;
	}


	public void setBCID(String bCID) {
		BCID = bCID;
	}


	public String getISBN() {
		return ISBN;
	}


	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public int getYearOfPubblication() {
		return yearOfPubblication;
	}

	public void setYearOfPubblication(int yearOfPubblication) {
		this.yearOfPubblication = yearOfPubblication;
	}

	public int getEditionNumber() {
		return editionNumber;
	}

	public void setEditionNumber(int editionNumber) {
		this.editionNumber = editionNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String getTitleFromString(String msg) {
		String words[] = msg.split(":");
		return words[1];
	}

	private String getAuthorFromString(String msg) {
		String words[] = msg.split(":");
		return words[1];
	}

	private Integer getYearOfPubblicationFromString(String msg) {
		String words[] = msg.split(":");
		if (words[1].equalsIgnoreCase("null")) {
			return null;
		} else {
			return Integer.parseInt(words[1]);
		}
	}

	private Integer getEditionNumberFromString(String msg) {
		String words[] = msg.split(":");
		if (words[1].equalsIgnoreCase("null")) {
			return null;
		} else {
			return Integer.parseInt(words[1]);
		}
	}

	private String getBookTypeFromString(String msg) {
		String words[] = msg.split(":");
		return words[1];
	}

	public void setUnderReading(boolean underReading) {
		this.underReading = underReading;
	}

	private String getUserFromString(String msg) {
		String words[] = msg.split(":");
		return words[1];
	}

	private String getBCIDFromString(String msg) {
		String words[] = msg.split(":");
		return words[1];
	}

	private boolean getStateFromString(String msg){
		String words[] = msg.split(":");
		return words[1].equals("1");
	}

	private String getISBNFromString(String msg) {
		String words[] = msg.split(":");
		if (words[1].equals("null")) {
			return "";
		} else {
			return words[1];
		}
	}

	public boolean isUnderReading() {
		return underReading;
	}

	public String generateBCID() {

		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		String generatedString = buffer.toString();
		return generatedString;
	}

	@Override
	public String toString() {
		return "TITLE:" + title + ";" +
				"AUTHOR:" + author + ";" +
				"YEAR:" + yearOfPubblication + ";" +
				"EDITION:" + editionNumber + ";" +
				"TYPE:" + type + ";" + 
				"ACTUALOWNER:" + actualOwnerUsername + ";" +
				"ISBN:" + ISBN + ";" + 
				"STATE:" + (underReading == true ? 1:0) + ";" +
				"BCID:" + BCID;
	}


}
