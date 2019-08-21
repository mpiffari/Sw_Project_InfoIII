package dataManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import user.User;

public class UserData implements UserQuery{

	private static UserData instance;
	
	private UserData() {
		
	}
	
	public static UserData getInstance() {
		if(instance == null) {
			instance = new UserData();
		}
		return instance;
	}
	
	public boolean login(User user) {
		//query login -> select conta righe
		String sql = "SELECT Count(Username) AS Result FROM Utente Where Username = ? AND Password = ?";
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(sql);
		
		try {
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			ResultSet rs = stmt.executeQuery();
			if(rs.next() && rs.getInt(1) == 0)	//if rs is not empty and query result is 0 then the BCID is available
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
