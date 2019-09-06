package com.bc.bookcrossing.src.UnitTest;

import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * 
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 * 
 * String format received from client:
 * - TITLE:(space).....\r\n
 * - AUTHOR:(space).....\r\n
 * - YEAR:(space).....\r\n
 * - EDITION:(space).....\r\n
 * - TYPE:(space).....\r\n
 *
 */

public class Book implements Serializable{

	private String BCID = "";
	private String title;
	private String author;
	private @Nullable
	int yearOfPubblication;
	private @Nullable int editionNumber;
	private String type;
	private String ISBN;
	private String user;
	private boolean underReading;

    public Book(){}

	public Book(String title, String author, @Nullable int yearOfPubblication, @Nullable int editionNumber, String type) {
		this.title = title;
		this.author = author;
		this.yearOfPubblication = yearOfPubblication;
		this.editionNumber = editionNumber;
		this.type = type;
	}

	public Book(String msg) {
		String lines[] = msg.split(";");

		for(String line : lines) {

        }


		this.title = getTitleFromString(lines[0<lines.length ? 0 : null]);
		this.author = getAuthorFromString(lines[1<lines.length ? 1 : null]);
		this.yearOfPubblication = getYearOfPubblicationFromString(lines[2<lines.length ? 2 : null]);
		this.editionNumber = getEditionNumberFromString(lines[3<lines.length ? 3 : null]);
		this.type = getBookTypeFromString(lines[4<lines.length ? 4 : null]);
        this.user = getUserFromString(lines[5<lines.length ? 5 : null]);
        this.ISBN = getISBNFromString(lines[6<lines.length ? 6 : null]);
        this.underReading = getStateFromString(lines[7<lines.length ? 7 : null]);
        this.BCID = getBCIDFromString(lines[8<lines.length ? 8 : null]);
	}

	public Book(String title, String author, @Nullable int yearOfPubblication, @Nullable int editionNumber, String type, String ISBN) {
		this.title = title;
		this.author = author;
		this.yearOfPubblication = yearOfPubblication;
		this.editionNumber = editionNumber;
		this.type = type;
		this.ISBN = ISBN;
	}

    public void setISBN(String ISBN) {
        this.ISBN  = ISBN;
    }
	public String getISBN() {
		return ISBN;
	}

    public void setBCID(String BCID) {
        this.BCID = BCID;
    }
    public String getBCID() {
        return BCID;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
	public String getAuthor() {
		return author;
	}

    public void setTitle(String title) {
        this.title = title;
    }
	public String getTitle() {
		return title;
	}

    public void setYearOfPubblication(int yearOfPubblication) {
        this.yearOfPubblication = yearOfPubblication;
    }
	public int getYearOfPubblication() {
		return yearOfPubblication;
	}

    public void setEditionNumber(int editionNumber) {
        this.editionNumber = editionNumber;
    }
	public int getEditionNumber() {
		return editionNumber;
	}

    public void setType(String type) {
        this.type = type;
    }
	public String getType() {
		return type;
	}

    public String getUser() {
        return user;
    }


    private String getTitleFromString(String msg) {
        if(msg.length() != 0 && msg != null) {
            String words[] = msg.split(":");
            return words[1];
        } else {
            return  "";
        }
	}

	private String getAuthorFromString(String msg) {
        if(msg.length() != 0 && msg != null) {
            String words[] = msg.split(":");
            return words[1];
        } else {
            return  "";
        }
	}

	private Integer getYearOfPubblicationFromString(String msg) {
        if(msg.length() != 0 && msg != null) {
            String words[] = msg.split(":");
            int year = 0;
            try {
                year = Integer.parseInt(words[1]);
            }catch (NumberFormatException e) {
                return null;
            }
            return year;
        } else {
            return null;
        }
	}

	private Integer getEditionNumberFromString(String msg) {
        if(msg.length() != 0 && msg != null) {
            String words[] = msg.split(":");
            int edition = 0;
            try {
                edition = Integer.parseInt(words[1]);
            }catch (NumberFormatException e) {
                return null;
            }
            return edition;
        } else {
            return null;
        }
	}

	private String getBookTypeFromString(String msg) {
        if(msg.length() != 0 && msg != null) {
            String words[] = msg.split(":");
            return words[1];
        } else {
            return  "";
        }
	}

    private String getISBNFromString(String msg) {
        if(msg.length() != 0 && msg != null) {
            String words[] = msg.split(":");
            return words[1];
        } else {
            return  "";
        }
    }

    private String getUserFromString(String msg) {
        if(msg.length() != 0 && msg != null) {
            String words[] = msg.split(":");
            return words[1];
        } else {
            return  "";
        }
    }

	private String getBCIDFromString(String msg) {
        if(msg.length() != 0 && msg != null) {
            String words[] = msg.split(":");
            return words[1];
        } else {
            return  "";
        }
	}

    private boolean getStateFromString(String msg){
        if(msg.length() != 0 && msg != null) {
            String words[] = msg.split(":");
            return words[1].equals("1");
        } else {
            return Boolean.parseBoolean(null);
        }
	}

	public boolean isUnderReading() {
		return underReading;
	}

    public String encode() {
        return "TITLE:" + title + ";" +
                "AUTHOR:" + author + ";" +
                "YEAR:" + yearOfPubblication + ";" +
                "EDITION:" + editionNumber + ";" +
                "TYPE:" + type + ";" +
                "USER:" + user + ";" +
                "ISBN:" + ISBN + ";" +
                "STATE:" + (underReading == true ? 1:0) + ";" +
                "BCID:" + BCID;
    }

    @Override
    public String toString() {
        return "BCID: " + BCID + "\n" +
                title + " di " + author + ".\n" +
                "Categoria: " + type + ".\n" +
                "Libro raccolto dall'utente: " + user + ".";
    }
}
