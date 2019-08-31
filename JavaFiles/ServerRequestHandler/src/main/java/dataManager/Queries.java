package dataManager;

public class Queries {
	public static String readerBookQuery = "SELECT USERNAME FROM Possesso Where BCID = ?";
	public static String readerLocationQuery = "SELECT RESIDENZALAT, RESIDENZALONG, RAGGIOAZIONE FROM Utente Where USERNAME = ?";
	public static String allUsersQuery = "SELECT * FROM UTENTE";
	public static String underReadingBookQuery = "SELECT COUNT(LIBRO) FROM Possesso WHERE LIBRO = ? AND DATAFINE IS NULL AND LUOGORILASCIO IS NULL";
	public static String searchByTitleQuery = "SELECT * FROM Libro L WHERE Titolo = ?";
	public static String searchByAuthorQuery = "SELECT * FROM Libro L WHERE Autore = ?";
	public static String searchByTitleAndAuthorQuery = "SELECT * FROM Libro L WHERE Titolo = ? AND Autore = ?";
	public static String bcidAvailableQuery = "SELECT Count(BCID) AS Result FROM Libro Where BCID = ?";
	public static String insertBookQuery = "INSERT INTO LIBRO (BCID, TITOLO, AUTORE, ISBN, PROPRIETARIO, GENERE) VALUES (?,?,?,?,?,?)";
	public static String getUserInformationsQuery = "SELECT * FROM Utente WHERE Username = ?";
	public static String getBooksOwnedBy = "SELECT BCID FROM Possesso WHERE Username = ?";
	public static String insertNewReservationQuery = "INSERT INTO PRENOTAZIONE (UTENTE, LIBRO, ID) VALUES (?,?,?)";
	public static String getReservationQuery = "SELECT UTENTE FROM Prenotazione WHERE BCID = ?";
}
