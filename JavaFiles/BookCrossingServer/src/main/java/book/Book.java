package book;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import org.eclipse.jdt.annotation.Nullable;

import algorithmReservationHandler.Algorithm;
import algorithmReservationHandler.AlgorithmResult;
import dataManager.BookData;
import dataManager.Localization;
import profile.Profile;


/**
 * 
 * Un'instanza di questa classe descrive un libro il quale � inserito all'interno del programma di book crossing
 * 
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public class Book implements BookManager{

	private String title;
	private String author;
	private int yearOfPubblication;
	private int editionNumber;
	private String type;
	private String bcid;
	private String isbn;
	private String actualOwnerUsername;
	private boolean underReading;
	private ArrayList<Profile> prenotanti = new ArrayList<Profile>();
	//private int idPrenotazione; 
	private Localization localization;

	/**
	 * 
	 * @param msg - Stringa con una struttura ben precisa per permettere la costruzione di un libro
	 */
	//TODO: allineare book alla classe Book lato Android

	public Book(String msg) {	

		final String lines[] = msg.split(";");

		//String lines[] = msg.split(";");
		String copyLines[] = new String[9];
		int indexForCopy = 0;

		for(String line : lines) {
			copyLines[indexForCopy] = line;
			indexForCopy ++;

			String words[] = line.split(":");
			if(words.length > 0) {
				switch (words[0].toUpperCase()) {
				case "TITLE": {
					this.title = getTitleFromString(line);
					break;
				}
				case "AUTHOR": {
					this.author = getAuthorFromString(line);
					break;
				}
				case "YEAR": {
					this.yearOfPubblication = getYearOfPubblicationFromString(line);
					break;
				}
				case "EDITION": {
					this.editionNumber = getEditionNumberFromString(line);
					break;
				}
				case "TYPE": {
					this.type = getBookTypeFromString(line);
					break;
				}
				case "USER": {
					this.actualOwnerUsername = getUserFromString(line);
					break;
				}
				case "ISBN": {
					this.isbn = getISBNFromString(line);
					break;
				}
				case "STATE": {
					this.underReading = getStateFromString(line);
					break;
				}
				case "BCID": {
					String temp = getBCIDFromString(lines[8]);
					if(temp.equals("")) {
							bcid = generateBCID();
					}
					else {
						this.bcid = temp;
					}      
				}

				}
			}
		}

		/*this.title = getTitleFromString(lines[0]);
		this.author = getAuthorFromString(lines[1]);
		this.yearOfPubblication = getYearOfPubblicationFromString(lines[2]);
		this.editionNumber = getEditionNumberFromString(lines[3]);
		this.type = getBookTypeFromString(lines[4]);
		this.actualOwnerUsername = getUserFromString(lines[5]);
		this.isbn = getISBNFromString(lines[6]);
		this.underReading = getStateFromString(lines[7]);
		String temp = getBCIDFromString(lines[8]);
		if(temp.equals("")) {
			do {
				bcid = generateBCID();
			}while(!BookData.getInstance().isBCIDavailable(bcid));
		}
		else {
			this.bcid = temp;
		}*/      

	}

	/**
	 * 
	 * @param title
	 * @param author
	 * @param yearOfPubblication
	 * @param editionNumber
	 * @param type
	 */

	public Book(String title, String author, int yearOfPubblication, int editionNumber, String type) {
		this();
		this.title = title;
		this.author = author;
		this.yearOfPubblication = yearOfPubblication;
		this.editionNumber = editionNumber;
		this.type = type;
	}

	/**
	 * 
	 * Init semplificato
	 * 
	 * @param bcid
	 * @param owner
	 */
	public Book(String bcid, String owner) {
		this();
		this.bcid = bcid;
		this.actualOwnerUsername = owner;
	}


	/**
	 * Sorta di super init, il quale va a generare un BCID casualmente, 
	 * controllando poi che questo BCID appena creato non esista gi� nel database: in questo
	 * caso quindi, un altro BCID verrebbe ricreato immediatamente.
	 */
	public Book() {
		bcid = generateBCID();
	}

	/**
	 * 
	 * @return true se il libro � stato inserito nel db, altrimenti false
	 */
	public boolean insert() {
		return BookData.getInstance().insertBook(this);
	}

	/**
	 * 
	 * @param username
	 * @return true se la prenotazione � stata completata
	 */
	public boolean reserve(String username) {
		Profile u = new Profile();
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

	/**
	 * 
	 * @return ArrayList di user che hanno prenotato questo specifico libro
	 */
	public ArrayList<Profile> getPrenotanti() {
		ResultSet rs = BookData.getPrenotanti(this.bcid);
		try {
			prenotanti.clear();
			Profile u;
			while(rs.next()) {
				u = new Profile();
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


	/**
	 * 
	 * @param user Utente che richiede questo libro
	 * @return
	 */
	public boolean setPrenotante(Profile user) {
		return BookData.setPrenotante(user.getUsername(), this.bcid);
	}


	/**
	 * 
	 * @param title
	 * @return list of books that has title passed as parameter
	 */
	public static ArrayList<Book> searchBookByTitle(String title) {
		ArrayList<Book> result = new ArrayList<Book>();
		ResultSet rs = BookData.getBookByTitle(title);

		try {
			while(rs.next()) {
				//System.out.println("#_ " + rs.getString(1) +"  " +rs.getString(2));
				String bcid = rs.getString(1);
				String t = rs.getString(2);
				String a = rs.getString(3);
				int pubbDate = Integer.parseInt(rs.getString(4) == null ? "0" : rs.getString(4));
				int editionNumber = 0; //TODO
				String isbn = rs.getString(5);
				String bookType = rs.getString(6);
				String actualOwner = rs.getString(7);

				//TODO: gestire Type e Edition number
				Book b = new Book(t,a, pubbDate, editionNumber, bookType);
				b.setActualOwnerUsername(actualOwner);
				b.setBCID(bcid);
				b.setISBN(isbn);
				b.setUnderReading(BookData.isUnderReading(bcid));
				result.add(b);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param author
	 * @return list of books that have author passed as parameter
	 */
	public static ArrayList<Book> searchBookByAuthor(String author) {
		ArrayList<Book> result = new ArrayList<Book>();
		ResultSet rs = BookData.getBookByAuthor(author);


		try {
			while(rs.next()) {
				//System.out.println("#_ " + rs.getString(1) +"  " +rs.getString(2));
				String bcid = rs.getString(1);
				String t = rs.getString(2);
				String a = rs.getString(3);
				int pubbDate = Integer.parseInt(rs.getString(4) == null ? "0" : rs.getString(4));
				int editionNumber = 0; //TODO
				String isbn = rs.getString(5);
				String bookType = rs.getString(6);
				String actualOwner = rs.getString(7);

				//TODO: gestire Type e Edition number
				Book b = new Book(t,a, pubbDate, editionNumber, bookType);
				b.setActualOwnerUsername(actualOwner);
				b.setBCID(bcid);
				b.setISBN(isbn);
				b.setUnderReading(BookData.isUnderReading(bcid));
				result.add(b);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public static ArrayList<Book> searchBook(String title, String author) {
		ArrayList<Book> result = new ArrayList<Book>();

		try {

			ResultSet rs = BookData.getBooks(title, author);

			while(rs.next()) {
				//System.out.println("#_ " + rs.getString(1) +"  " +rs.getString(2));
				String bcid = rs.getString(1);
				String t = rs.getString(2);
				String a = rs.getString(3);
				int pubbDate = Integer.parseInt(rs.getString(4).equals("null") ? "0" : rs.getString(4));
				int editionNumber = 0; //TODO
				String isbn = rs.getString(5);
				String bookType = rs.getString(6);
				String actualOwner = rs.getString(7);

				//TODO: gestire Type e Edition number
				Book b = new Book(t,a, pubbDate, editionNumber, bookType);
				b.setActualOwnerUsername(actualOwner);
				b.setBCID(bcid);
				b.setISBN(isbn);
				b.setUnderReading(actualOwner.equals("null")? false : true);
				result.add(b);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
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
		return bcid;
	}


	public void setBCID(String bCID) {
		bcid = bCID;
	}


	public String getISBN() {
		return isbn;
	}


	public void setISBN(String iSBN) {
		isbn = iSBN;
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

	public void setLocalization(Localization localization) {
		this.localization = localization;
	}

	public Localization getLocalization() {
		return localization;
	}

	/**
	 * L'insieme di questo metodo get, unitamente agli altri, ha il compito di estrarre, dall'oggetto Book
	 * sottoposto ad un corretto encoding, tutti i campi necessari.
	 *
	 * @param msg
	 * @return  Titolo estratto da msg con specifica struttura
	 */
	private String getTitleFromString(String msg) {
		if(msg.length() != 0 && msg != null) {
			String words[] = msg.split(":");
			if(words[0].toUpperCase().equals("TITLE") && words.length > 1) {
				return words[1];
			} else {
				return "";
			}
		} else {
			return  "";
		}
	}

	private String getAuthorFromString(String msg) {
		if(msg.length() != 0 && msg != null) {
			String words[] = msg.split(":");
			if(words[0].toUpperCase().equals("AUTHOR") && words.length > 1) {
				return words[1];
			} else {
				return "";
			}
		} else {
			return  "";
		}
	}

	@Nullable
	private Integer getYearOfPubblicationFromString(String msg) {
		if(msg.length() != 0 && msg != null) {
			String words[] = msg.split(":");
			int year = 0;
			try {
				if(words[0].toUpperCase().equals("YEAR") && words.length > 1) {
					try {
						year = Integer.parseInt(words[1]);
					} catch (NumberFormatException e) {
						return null;
					}
				} else {
					return null;
				}
			}catch (NumberFormatException e) {
				return null;
			}
			return year;
		} else {
			return null;
		}
	}

	@Nullable
	private Integer getEditionNumberFromString(String msg) {
		if(msg.length() != 0 && msg != null) {
			String words[] = msg.split(":");
			int edition = 0;
			try {
				if(words[0].toUpperCase().equals("EDITION") && words.length > 1) {
					try {
						edition = Integer.parseInt(words[1]);
					} catch (NumberFormatException e) {
						return null;
					}
				} else {
					return null;
				}
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
			if(words[0].toUpperCase().equals("TYPE") && words.length > 1) {
				return words[1];
			} else {
				return  "";
			}
		} else {
			return  "";
		}
	}

	private String getISBNFromString(String msg) {
		if(msg.length() != 0 && msg != null) {
			String words[] = msg.split(":");
			if(words[0].toUpperCase().equals("ISBN") && words.length > 1) {
				return words[1];
			} else {
				return  "";
			}
		} else {
			return  "";
		}
	}

	private String getUserFromString(String msg) {
		if(msg.length() != 0 && msg != null) {
			String words[] = msg.split(":");
			if(words[0].toUpperCase().equals("USER") && words.length > 1) {
				return words[1];
			} else {
				return  "";
			}
		} else {
			return  "";
		}
	}

	private String getBCIDFromString(String msg) {
		if(msg.length() != 0 && msg != null) {
			String words[] = msg.split(":");
			if(words[0].toUpperCase().equals("BCID") && words.length > 1) {
				return words[1];
			} else {
				return  "";
			}
		} else {
			return  "";
		}
	}

	private boolean getStateFromString(String msg){
		if(msg.length() != 0 && msg != null) {
			String words[] = msg.split(":");
			if(words[0].toUpperCase().equals("STATE") && words.length > 1) {
				return words[1].equals("1");
			} else {
				return Boolean.parseBoolean(null);
			}
		} else {
			return Boolean.parseBoolean(null);
		}
	}



	public void setUnderReading(boolean underReading) {
		this.underReading = underReading;
	}

	public boolean isUnderReading() {
		return underReading;
	}
	
	public static ArrayList<Book> onRouteBooks(String user) {
		ArrayList<Book> booksOnRoute = new ArrayList<Book>();
		
		try {
			ResultSet rs = BookData.onRouteBooks(user);
			while(rs.next()) {
				if(rs.getString(3).contains(user)){
					Book b = new Book();
					b.setTitle(rs.getString(1));
					b.setAuthor(rs.getString(2));
					booksOnRoute.add(b);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return booksOnRoute;
	}

	/**
	 * 
	 * @return BCID univoco
	 */

	
	public static String generateBCID() {
		String generatedString;
		
		do {
				
			int leftLimit = 97; // letter 'a'
			int rightLimit = 122; // letter 'z'
			int targetStringLength = 10;
			Random random = new Random();
			StringBuilder buffer = new StringBuilder(targetStringLength);
			for (int i = 0; i < targetStringLength; i++) {
				int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
				buffer.append((char) randomLimitedInt);
			}
			generatedString = buffer.toString();
		}while(!BookData.getInstance().isBCIDavailable(generatedString));
		
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
				"ISBN:" + isbn + ";" + 
				"STATE:" + (underReading == true ? 1:0) + ";" +
				"BCID:" + bcid;
	}


}
