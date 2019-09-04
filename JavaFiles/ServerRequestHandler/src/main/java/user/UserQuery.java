package user;

import java.util.ArrayList;

public interface UserQuery {
	public LoginStatus login(User user);
	public boolean exist(String username);
	public ArrayList<String> pathOfUsers(String user);
}
