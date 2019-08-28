package com.bc.bookcrossing.bookcrossing.Structures;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.Serializable;



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

public class Book implements Serializable{

	private String BCID;
	private String title;
	private String author;
	private @Nullable
	int yearOfPubblication;
	private @Nullable int editionNumber;
	private String type;
	private String ISBN;
	private String user;
	private boolean underReading;
	private String urlImage;

	public Book(String title, String author, @Nullable int yearOfPubblication, @Nullable int editionNumber, String type) {
		this.title = title;
		this.author = author;
		this.yearOfPubblication = yearOfPubblication;
		this.editionNumber = editionNumber;
		this.type = type;
	}

	public Book(String msg) {
		this();
		String lines[] = msg.split(";");
		this.title = getTitleFromString(lines[0]);
		this.author = getAuthorFromString(lines[1]);
		this.yearOfPubblication = getYearOfPubblicationFromString(lines[2]);
		this.editionNumber = getEditionNumberFromString(lines[3]);
		this.type = getBookTypeFromString(lines[4]);
        this.user = getUserFromString(lines[5]);
        this.ISBN = getISBNFromString(lines[6]);
        this.underReading = getStateFromString(lines[7]);
        this.BCID = getBCIDFromString(lines[8]);
	}

	public Book(String title, String author, @Nullable int yearOfPubblication, @Nullable int editionNumber, String type, String ISBN, String urlImage) {
		this.title = title;
		this.author = author;
		this.yearOfPubblication = yearOfPubblication;
		this.editionNumber = editionNumber;
		this.type = type;
		this.ISBN = ISBN;
		this.urlImage = urlImage;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public String getISBN() {
		return ISBN;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Book(){

	}

	public String getBCID() {
		return BCID;
	}

	public String encode() {
		// TODO Auto-generated method stub
		return "TITLE:" + title + ";" +
				"AUTHOR:" + author + ";" +
				"YEAR:" + yearOfPubblication + ";" +
				"EDITION:" + editionNumber + ";" +
				"TYPE:" + type + ";";
	}

    @Override
    public String toString() {
        return title + " di " + author + " : " + " inserito da " + user +  " " + underReading + " " + BCID + " " + user;
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

    private String getISBNFromString(String msg) {
        String words[] = msg.split(":");
        if (words[1].equals("null")) {
            return "";
        } else {
            return words[1];
        }
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

	public boolean isUnderReading() {
		return underReading;
	}

	public String getUser() {
		return user;
	}
}
