package com.bc.bookcrossing.src.ClientModels;

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
	private @Nullable Integer yearOfPubblication;
	private @Nullable Integer editionNumber;
	private String type;
	private @Nullable String ISBN;
	private String user;
	private boolean underReading;
    private Double latitude;
    private Double longitude;

    private final String TITLE = "TITLE";
    private final String AUTHOR = "AUTHOR";
    private final String YEAR = "YEAR";
    private final String EDITION = "EDITION";
    private final String TYPE = "TYPE";
    private final String ACTUALOWNER = "ACTUALOWNER";
    private final String _ISBN = "ISBN";
    private final String STATE = "STATE";
    private final String _BCID = "BCID";

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
     *      <li>ACTUALOWNER:.....\r\n</li>
     *      <li>_ISBN:.....\r\n</li>
     *      <li>STATE:.....\r\n</li>
     *      <li>_BCID:.....\r\n</li>
     *  </ul>
     * @param msg
     */

	public Book(@NonNull String msg) {
		String lines[] = msg.split(";");
        String copyLines[] = new String[9];
		int indexForCopy = 0;

        for(String line : lines) {
            copyLines[indexForCopy] = line;
            indexForCopy ++;

            String words[] = line.split(":");
            if(words.length > 0) {
                String response = words[0].toUpperCase();
                switch  (response) {
                    case TITLE: {
                        this.title = getTitleFromString(line);
                        break;
                    }
                    case AUTHOR: {
                        this.author = getAuthorFromString(line);
                        break;
                    }
                    case YEAR: {
                        this.yearOfPubblication = getYearOfPubblicationFromString(line);
                        break;
                    }
                    case EDITION: {
                        this.editionNumber = getEditionNumberFromString(line);
                        break;
                    }
                    case TYPE: {
                        this.type = getBookTypeFromString(line);
                        break;
                    }
                    case ACTUALOWNER: {
                        this.user = getUserFromString(line);
                        break;
                    }
                    case _ISBN: {
                        this.ISBN = getISBNFromString(line);
                        break;
                    }
                    case STATE: {
                        this.underReading = getStateFromString(line);
                        break;
                    }
                    case _BCID: {
                        this.BCID = getBCIDFromString(line);
                        break;
                    }

                }
            }
        }
	}

    /**
     *
     * @param latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @param longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return posizione/latitudine di questo libro
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     *
     * @return la posizione/latitudine di questo libro
     */
    public Double getLongitude() {
        return longitude;
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
    public void setYearOfPubblication(Integer yearOfPubblication) {
        this.yearOfPubblication = yearOfPubblication;
    }
    /**
     * Ritorna l'anno in cui questo libro fu pubblicato.
     *
     * @return YEAR
     */
	public Integer getYearOfPubblication() {
		return yearOfPubblication;
	}

    /**
     * Imposta il numero dell'edizione per questo libro
     *
     * @param editionNumber
     */
    public void setEditionNumber(Integer editionNumber) {
        this.editionNumber = editionNumber;
    }
    /**
     * Ritorna il numero di questa edizione.
     *
     * @return EDTION_NUMBER
     */
	public Integer getEditionNumber() {
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
            if(words[0].toUpperCase().equals(TITLE) && words.length > 1) {
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
            if(words[0].toUpperCase().equals(AUTHOR) && words.length > 1) {
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
                if(words[0].toUpperCase().equals(YEAR) && words.length > 1) {
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
                if(words[0].toUpperCase().equals(EDITION) && words.length > 1) {
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
            if(words[0].toUpperCase().equals(TYPE) && words.length > 1) {
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
            if(words[0].toUpperCase().equals(_ISBN) && words.length > 1) {
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
            if(words[0].toUpperCase().equals(ACTUALOWNER) && words.length > 1) {
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
            if(words[0].toUpperCase().equals(_BCID) && words.length > 1) {
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
            if(words[0].toUpperCase().equals(STATE) && words.length > 1) {
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
        String res = "";
        res = res + "BCID: " + BCID + "\n";
        res = res + "Title: " + title + "\n";
        res = res + "Author: " + author + "\n";
        res = res + "Categoria: " + type;
        if(!user.equals("null")) {
            res = res + "\nLibro in possesso dell'utente: " + user;
        }
        return res;
    }
}
