package book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
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
    private String owner;
    private boolean underReading;
    private ArrayList<User> prenotanti = new ArrayList<User>();
    

	public Book(String msg) {	
    	this();
    	String lines[] = msg.split(";");
        this.title = getTitleFromString(lines[0]);
        this.author = getAuthorFromString(lines[1]);
        this.yearOfPubblication = getYearOfPubblicationFromString(lines[2]);
        this.editionNumber = getEditionNumberFromString(lines[3]);
        this.type = getBookTypeFromString(lines[4]);
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
    	this.owner = owner;
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
    	return BookData.getInstance().reserveBook(this, username);
    }
    
    public ArrayList<User> getPrenotanti() {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.getReservationQuery);
		try {
			stmt.setString(1, this.BCID);
			ResultSet rs = stmt.executeQuery();
			prenotanti.clear();
			User u;
			while(rs.next()) {
				u = new User();
				u.setUsername(rs.getString(1));
				prenotanti.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.prenotanti;
	}

	public boolean setPrenotante(User user) {
		this.prenotanti.add(user);
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.insertNewReservationQuery);

		int result = 0;
		try {
			stmt.setString(1, user.getUsername());
			stmt.setString(2, this.BCID);
			stmt.setDouble(3, Math.random());	
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result==1 ? true : false;
	}
    
    public String getProprietario() {
		return owner;
	}

	public void setProprietario(String proprietario) {
		this.owner = proprietario;
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
				"USER: " + owner + ";" +
				"ISBN: " + ISBN + ";" + 
				"STATE:" + (underReading == true ? 1:0) + ";" +
				"BCID: " + BCID;
	}
	
	
}
