package book;

import java.security.SecureRandom;

import org.eclipse.jdt.annotation.Nullable;

import dataManager.BookData;
import dataManager.BookQuery;

/**
 * 
 * @author Michele
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
    private @Nullable int yearOfPubblication;
    private @Nullable int editionNumber;
    private BookType type;
    private String BCID;
    private String ISBN;
    private String username;
    
    public Book(String msg) {	
    	this();
    	String lines[] = msg.split(";");
        this.title = getTitleFromString(lines[0]);
        this.author = getAuthorFromString(lines[1]);
        this.yearOfPubblication = getYearOfPubblicationFromString(lines[2]);
        this.editionNumber = getEditionNumberFromString(lines[3]);
        this.type = getBookTypeFromString(lines[4]);
    }
    
    
    public Book(String title, String author, @Nullable int yearOfPubblication, @Nullable int editionNumber, BookType type) {
        this();
    	this.title = title;
        this.author = author;
        this.yearOfPubblication = yearOfPubblication;
        this.editionNumber = editionNumber;
        this.type = type;
    }
    
    public Book() {
    	SecureRandom rnd = new SecureRandom();
    	byte[] token = new byte[15];
    	BCID = new String(token);
    	
    	while(!BookData.getInstance().isBCIDavailable(BCID)) {
    		rnd.nextBytes(token);
    		BCID = new String(token);
    	}
    }
    
    
    public boolean insert() {
    	return BookData.getInstance().insertBook(this);
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


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
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

	public BookType getType() {
		return type;
	}

	public void setType(BookType type) {
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
	
	private @Nullable Integer getYearOfPubblicationFromString(String msg) {
		String words[] = msg.split(":");
		if (words[1].equalsIgnoreCase("null")) {
			return null;
		} else {
			return Integer.parseInt(words[1]);
		}
	}
	
	private @Nullable Integer getEditionNumberFromString(String msg) {
		String words[] = msg.split(":");
		if (words[1].equalsIgnoreCase("null")) {
			return null;
		} else {
			return Integer.parseInt(words[1]);
		}
	}
	
	private BookType getBookTypeFromString(String msg) {
		String words[] = msg.split(":");
		return BookType.valueOf(words[1]);
	}
}
