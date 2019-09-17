package dataManager;

import java.util.ArrayList;

import profile.LoginStatus;
import profile.Profile;

/**
 * 
 * Questa interfaccia contiene i metodi per gestire le queries verso 
 * il db per ottenere le informazioni utente.
 * 
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public interface ProfileQuery {
	public LoginStatus login(Profile user);
	public boolean exist(String username);
	public ArrayList<String> pathOfUsers(String user);
}
