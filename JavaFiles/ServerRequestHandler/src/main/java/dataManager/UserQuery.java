package dataManager;

import user.User;

public interface UserQuery {
	
	public boolean login(User user);
	public boolean exist(String username);
}
