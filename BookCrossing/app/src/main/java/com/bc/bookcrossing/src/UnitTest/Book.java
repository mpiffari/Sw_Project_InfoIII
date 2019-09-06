package com.bc.bookcrossing.src.UnitTest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.Serializable;

/**
 * Classe che rappresenta gli oggetti al centro dell'applicazione: i libri.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public class Book implements Serializable{

	private String BCID = "";
	private String title;
	private String author;
	private @Nullable int yearOfPubblication;
	private @Nullable int editionNumber;
	private String type;
	private String ISBN;
	private String user;
	private boolean underReading;

    /**
     * Empty Book init
     */
    public Book(){}

    /**
     * Book initi throught fields: year of pubblication and edition number can be omitted.
     *
     * @param title
     * @param author
     * @param yearOfPubblication
     * @param editionNumber
     * @param type
     * @param ISBN
     */
    public Book(@NonNull String title,@NonNull String author, @Nullable int yearOfPubblication, @Nullable int editionNumber,@NonNull String type,@NonNull String ISBN) {
        this.title = title;
        this.author = author;
        this.yearOfPubblication = yearOfPubblication;
        this.editionNumber = editionNumber;
        this.type = type;
        this.ISBN = ISBN;
    }

    /**
     *
     * @param title
     * @param author
     * @param yearOfPubblication
     * @param editionNumber
     * @param type
     */
	public Book(@NonNull String title, String author, @Nullable int yearOfPubblication, @Nullable int editionNumber,@NonNull String type) {
		this.title = title;
		this.author = author;
		this.yearOfPubblication = yearOfPubblication;
		this.editionNumber = editionNumber;
		this.type = type;
	}

    /**
     * La stringa <p>
     *     msg
     * </p>
     * ci si aspetta che abbia questa struttura:
     *  <ul>
     *      <li>TITLE:.....\r\n</li>
     *      <li>AUTHOR:.....\r\n</li>
     *      <li>YEAR:.....\r\n</li>
     *      <li>EDITION:.....\r\n</li>
     *      <li>TYPE:.....\r\n</li>
     *      <li>USER:.....\r\n</li>
     *      <li>ISBN:.....\r\n</li>
     *      <li>STATE:.....\r\n</li>
     *      <li>BCID:.....\r\n</li>
     *  </ul>
     * @param msg
     */
	public Book(@NonNull String msg) {
		String lines[] = msg.split(";");
        String copyLines[] = new String[8];
		int indexForCopy = 0;

        for(String line : lines) {
            copyLines[indexForCopy] = line;
            indexForCopy ++;
        }

		this.title = getTitleFromString(copyLines[0]);
		this.author = getAuthorFromString(copyLines[1]);
		this.yearOfPubblication = getYearOfPubblicationFromString(copyLines[2]);
		this.editionNumber = getEditionNumberFromString(copyLines[3]);
		this.type = getBookTypeFromString(copyLines[4]);
        this.user = getUserFromString(copyLines[5]);
        this.ISBN = getISBNFromString(copyLines[6]);
        this.underReading = getStateFromString(copyLines[7]);
        this.BCID = getBCIDFromString(copyLines[8]);
	}

    /**
     * Imposta l'ISBN per questo libro
     *
     * @param ISBN
     */
    public void setISBN(String ISBN) {
        this.ISBN  = ISBN;
    }
    /**
     * Ritorna l'ISBN di questo libro.
     *
     * @return ISBN
     */
	public String getISBN() {
		return ISBN;
	}

    /**
     * Imposta l'BCID per questo libro
     *
     * @param BCID
     */
    public void setBCID(String BCID) {
        this.BCID = BCID;
    }
    /**
     * Ritorna il codice identificativo della piattaforma di questo libro.
     *
     * @return BCID
     */
    public String getBCID() {
        return BCID;
    }

    /**
     * Imposta l'autore per questo libro
     *
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    /**
     * Ritorna l'autore di questo libro.
     *
     * @return ISBN
     */
	public String getAuthor() {
		return author;
	}

    /**
     * Imposta il titolo per questo libro
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * Ritorna il titolo di questo libro.
     *
     * @return TITLE
     */
	public String getTitle() {
		return title;
	}

    /**
     * Imposta l'anno di pubblicazione per questo libro
     *
     * @param yearOfPubblication
     */
    public void setYearOfPubblication(int yearOfPubblication) {
        this.yearOfPubblication = yearOfPubblication;
    }
    /**
     * Ritorna l'anno in cui questo libro fu pubblicato.
     *
     * @return YEAR
     */
	public int getYearOfPubblication() {
		return yearOfPubblication;
	}

    /**
     * Imposta il numero dell'edizione per questo libro
     *
     * @param editionNumber
     */
    public void setEditionNumber(int editionNumber) {
        this.editionNumber = editionNumber;
    }
    /**
     * Ritorna il numero di questa edizione.
     *
     * @return EDTION_NUMBER
     */
	public int getEditionNumber() {
		return editionNumber;
	}

    /**
     * Imposta la tipologia per questo libro
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Ritorna la tipologia di questo libro.
     *
     * @return TYPE
     */
	public String getType() {
		return type;
	}
    /**
     * Ritorna l'attuale possessore di questo libro.
     *
     * @return USER
     */
    public String getUser() {
        return user;
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
            if(words[0].toUpperCase() == "TITLE") {
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
            if(words[0].toUpperCase() == "AUTHOR") {
                return words[1];
            } else {
                return "";
            }
        } else {
            return  "";
        }
	}

	private Integer getYearOfPubblicationFromString(String msg) {
        if(msg.length() != 0 && msg != null) {
            String words[] = msg.split(":");
            int year = 0;
            try {
                if(words[0].toUpperCase() == "YEAR") {
                    year = Integer.parseInt(words[1]);
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

	private Integer getEditionNumberFromString(String msg) {
        if(msg.length() != 0 && msg != null) {
            String words[] = msg.split(":");
            int edition = 0;
            try {
                if(words[0].toUpperCase() == "EDITION") {
                    edition = Integer.parseInt(words[1]);
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
            if(words[0].toUpperCase() == "TYPE") {
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
            if(words[0].toUpperCase() == "ISBN") {
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
            if(words[0].toUpperCase() == "USER") {
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
            if(words[0].toUpperCase() == "BCID") {
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
            if(words[0].toUpperCase() == "STATE") {
                return words[1].equals("1");
            } else {
                return Boolean.parseBoolean(null);
            }
        } else {
            return Boolean.parseBoolean(null);
        }
	}

	public boolean isUnderReading() {
		return underReading;
	}

    /**
     *
     * @return Oggetto Book trasformato in stringa secondo la convenzione scelta
     */
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

    /**
     *
     * @return Generica rappresentazione dell'oggetto Book per print di debug
     */
    @Override
    public String toString() {
        return "BCID: " + BCID + "\n" +
                title + " di " + author + ".\n" +
                "Categoria: " + type + ".\n" +
                "Libro raccolto dall'utente: " + user + ".";
    }
}
