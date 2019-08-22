package com.bc.bookcrossing.bookcrossing.Structure;

import android.support.annotation.Nullable;

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

	private String title;
	private String author;
	private @Nullable
	int yearOfPubblication;
	private @Nullable int editionNumber;
	private BookType type;

	
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

	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "TITLE:" + title + ";" +
				"AUTHOR:" + author + ";" +
				"YEAR:" + yearOfPubblication + ";" +
				"EDITION:" + editionNumber + ";" +
				"TYPE:" + type + ";";
	}

}
