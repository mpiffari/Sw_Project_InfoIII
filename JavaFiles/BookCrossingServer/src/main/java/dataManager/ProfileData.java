package dataManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import profile.LoginStatus;
import profile.Profile;
/**
 * 
 * La classe UserData implementa l'interfaccia UserQuery per riuscire ad ottenere lo stato
 * dell'utente che risulta essere connesso al server.
 * Questa classe è implementata come un oggetto Singleton
 * 
 * 
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public final class ProfileData implements ProfileQuery {

	
	private static ProfileData instance;

	private ProfileData() {
	}

	public static ProfileData getInstance() {
		if (instance == null) {
			instance = new ProfileData();
		}
		return instance;
	}

	/**
	 * @param user: utente il quale è attualmente connesso al server e utilizza le api del server stesso
	 * @return LoginStatus-Success se la connessione è andata a buon fine (user e password sono corretti)
	 * @return LoginStatus-WRONG_PWD se la password è sbagliata
	 * @return LoginStatus-WRONG_USERNAME se il nome utente è sbagliato
	 */
	public LoginStatus login(Profile user) {
		try {
			PreparedStatement stmt;
			// Check username in database
			
			stmt = DBConnector.getDBConnector().prepareStatement(Queries.queryCheckUsername);
			stmt.setString(1, user.getUsername());
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				return LoginStatus.WRONG_USERNAME;
			}
			if (!(rs.getInt("RESULT") == 1)) {
				return LoginStatus.WRONG_USERNAME;
			}

			// Check correctness of tuple
			stmt = DBConnector.getDBConnector().prepareStatement(Queries.queryCheckUserAndPwd);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			rs = stmt.executeQuery();
			if (rs.next() && rs.getInt("RESULT") == 1) {
				return LoginStatus.SUCCESS;
			} else {
				return LoginStatus.WRONG_PWD;
			}

			// Debug all DB
			// System.out.println("Query username: " + user.getUsername());
			// System.out.println("Query psw: " + user.getPassword());
		} catch (SQLException e) {
			System.out.println("error: " + e.toString());
			e.printStackTrace();
		}
		return LoginStatus.UNSUCCES;
	}
	
	/**
	 * @param username Stringa la quale descrive l'username (ID) dell'utente connesso
	 * @return true se l'utente esiste, falso se non esiste
	 */
	public static ResultSet exist(String username) {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.checkUserExistance);
		ResultSet rs = null;
		try {
			stmt.setString(1, username);
			rs = stmt.executeQuery();
			if (rs.next() && rs.getInt(1) == 1) {
				return rs;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rs;
	}
	/**
	 * @param user Stringa la quale descrive l'username (ID) dell'utente connesso
	 * @return ArrayList<String> il quale contiene il percorso dove l'utente si trova
	 */
	public static ResultSet pathOfUsers(String user) {
		
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.queryForUserNotifications);
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

}
