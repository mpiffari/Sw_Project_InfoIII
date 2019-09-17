package algorithmReservationHandler;

import java.util.ArrayList;

import user.User;

/**
 * Struttura per contenere i risultati dell'esecuzione dell'algoritmo
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 *
 */
public class AlgorithmResult {
	public boolean resultFlag = false; // No path found
	public boolean directMeetingIsPossible = false;
	public ArrayList<User> userPath = new ArrayList<User>();

	public AlgorithmResult() {}
}
