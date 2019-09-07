package user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dataManager.DBConnector;
import dataManager.Queries;
/**
 * 
 * UserData class implements UserQuery interface for obtaing status about user that is connected to server
 * Implemented as Singleton
 * 
 * 
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
* @version 1.0
* @since 2018/2019
*/
public final class UserData implements UserQuery {

	
	private static UserData instance;

	private UserData() {
	}

	/**
	 * 
	 * @return instance if it is null.
	 */
	public static UserData getInstance() {
		if (instance == null) {
			instance = new UserData();
		}
		return instance;
	}

	/**
	 * @param User user: user who is connected to server and uses apk services
	 * @return LoginStatus-Success if connection is done(user and password are correct)
	 * @return LoginStatus-WRONG_PWD if password is wrong
	 * @return LoginStatus-WRONG_USERNAME if user is wrong
	 */
	public LoginStatus login(User user) {
		try {
			PreparedStatement stmt;
			// Check username in database
			String queryCheckUsername = "SELECT COUNT(USERNAME) AS RESULT FROM UTENTE WHERE USERNAME = ?";
			stmt = DBConnector.getDBConnector().prepareStatement(queryCheckUsername);
			stmt.setString(1, user.getUsername());
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				return LoginStatus.WRONG_USERNAME;
			}
			if (!(rs.getInt("RESULT") == 1)) {
				return LoginStatus.WRONG_USERNAME;
			}

			// Check correctness of tuple
			String queryCheckUserAndPwd = "SELECT COUNT(USERNAME) AS RESULT FROM UTENTE WHERE USERNAME = ? AND PASSWORD = ?";
			stmt = DBConnector.getDBConnector().prepareStatement(queryCheckUserAndPwd);
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

			/*
			 * PreparedStatement stmt_debug_all; stmt_debug_all =
			 * DBConnector.getDBConnector().prepareStatement("select * FROM Utente");
			 * ResultSet rs_all_debug = stmt_debug_all.executeQuery(); ResultSetMetaData
			 * rsmd_all = rs_all_debug.getMetaData(); int columnsNumberAll =
			 * rsmd_all.getColumnCount(); System.out.println("Columns number: " +
			 * columnsNumberAll); while (rs_all_debug.next()) { for(int i = 1; i <=
			 * columnsNumberAll; i++) { //if(rsmd_all.getColumnName(i) == "PASSWORD") { //
			 * System.out.println("PASSWORD: "); //
			 * System.out.println(rs_all_debug.getString(i)); //} else {
			 * System.out.print(rs_all_debug.getString(i) + " "); //} }
			 * System.out.println(); }
			 */

			// Debug query result
			/*
			 * System.out.println("Debug query result"); PreparedStatement stmt_debug;
			 * stmt_debug =
			 * DBConnector.getDBConnector().prepareStatement(queryCheckUserAndPwd);
			 * stmt_debug.setString(1, user.getUsername()); stmt_debug.setString(2,
			 * user.getPassword() +
			 * "                                                                                                                                           "
			 * );
			 * 
			 * ResultSet rs_debug = stmt_debug.executeQuery(); ResultSetMetaData rsmd =
			 * rs_debug.getMetaData(); int columnsNumber = rsmd.getColumnCount();
			 * 
			 * while (rs_debug.next()) { for (int i = 1; i <= columnsNumber; i++) { if (i >
			 * 1) System.out.print(",  "); int columnValue = rs_debug.getInt(i);
			 * System.out.print(columnValue + " " + rsmd.getColumnName(i)); }
			 * System.out.println(""); }
			 */
		} catch (SQLException e) {
			System.out.println("error: " + e.toString());
			e.printStackTrace();
		}
		return LoginStatus.UNSUCCES;
	}
	
	/**
	 * @param username string that describes username of connected user
	 * @return true if user exist, false if user does not exist
	 */

	public boolean exist(String username) {
		// TODO Auto-generated method stub
		String sql = "SELECT Count(Username) AS Result FROM Utente Where Username = ?";
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(sql);

		try {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next() && rs.getInt(1) == 1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	/**
	 * @param user string that describes username of connected user
	 * @return ArrayList<String> that contains paths where user is in
	 */
	public ArrayList<String> pathOfUsers(String user) {
		ArrayList<String> usersForNotifications = new ArrayList<String>();
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.queryForUserNotifications);
		try {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getString(3).contains(user)){
					usersForNotifications.add(rs.getString(3));
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return usersForNotifications;
	}

}
