package user;

import java.util.ArrayList;

/**
 * 
 * this interface contains methods about queries on database for user informations
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public interface UserQuery {
	public LoginStatus login(User user);
	public boolean exist(String username);
	public ArrayList<String> pathOfUsers(String user);
}
