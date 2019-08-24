package user;

public interface UserQuery {
	public LoginStatus login(User user);
	public boolean exist(String username);
}
