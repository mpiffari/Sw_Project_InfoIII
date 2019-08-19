package serverSide;

import org.eclipse.jdt.annotation.Nullable;

/**
 * 
 * @author Michele
 * 
 * String format received from client:
 * - TITLE:(space).....\r\n
 * - AUTHOR:(space).....\r\n
 * - YEAR:(space).....\r\n
 * - EDITION:(space).....\r\n
 * - TYPE:(space).....\r\n
 *
 */

public class Book {

    private String title;
	private String author;
    private @Nullable int yearOfPubblication;
    private @Nullable int editionNumber;
    private BookType type;

    public Book(String msg) {
    	String lines[] = msg.split("\\r?\\n");
        this.title = getTitleFromString(lines[0]);
        this.author = getAuthorFromString(lines[1]);
        this.yearOfPubblication = getYearOfPubblicationFromString(lines[2]);
        this.editionNumber = getEditionNumberFromString(lines[3]);
        this.type = getBookTypeFromString(lines[4]);
    }
    
    
    public Book(String title, String author, @Nullable int yearOfPubblication, @Nullable int editionNumber, BookType type) {
        this.title = title;
        this.author = author;
        this.yearOfPubblication = yearOfPubblication;
        this.editionNumber = editionNumber;
        this.type = type;
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
		String words[] = msg.split(" ");
		return words[1];
	}
	
	private String getAuthorFromString(String msg) {
		String words[] = msg.split(" ");
		return words[1];
	}
	
	private @Nullable Integer getYearOfPubblicationFromString(String msg) {
		String words[] = msg.split(" ");
		if (words[1].equalsIgnoreCase("null")) {
			return null;
		} else {
			return Integer.parseInt(words[1]);
		}
	}
	
	private @Nullable Integer getEditionNumberFromString(String msg) {
		String words[] = msg.split(" ");
		if (words[1].equalsIgnoreCase("null")) {
			return null;
		} else {
			return Integer.parseInt(words[1]);
		}
	}
	
	private BookType getBookTypeFromString(String msg) {
		String words[] = msg.split(" ");
		return BookType.valueOf(words[1]);
	}
}
