package user;

import java.util.ArrayList;

/**
 * 
 * @author Gruppo Paganessi - Piffari - Villa
 * this interface contains methods about queries on database for user informations
 */
public interface UserQuery {
	public LoginStatus login(User user);
	public boolean exist(String username);
	public ArrayList<String> pathOfUsers(String user);
}
