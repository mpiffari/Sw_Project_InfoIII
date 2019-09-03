package algorithmReservationHandler;

import java.util.ArrayList;

import user.User;

public class AlgorithmResult {
	public boolean resultFlag = false; // No path found
	public boolean directMeetingIsPossible = false;
	public ArrayList<User> userPath = new ArrayList<User>();
	
	public AlgorithmResult() {}
}
