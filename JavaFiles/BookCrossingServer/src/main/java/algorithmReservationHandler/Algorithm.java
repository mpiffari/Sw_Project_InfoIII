package algorithmReservationHandler;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import dataManager.DBConnector;
import dataManager.Localization;
import dataManager.Queries;
import profile.Profile;

import java.util.*;
import java.util.Map.Entry;
import book.Book;


/**
 *
 * Classe contenente metodi statici per poter andare a generare il percorso di utenti che il 
 * libro prenotato dovrebbe seguire per poter raggiungere l'utente prenotante.
 * 
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 *
 */
public class Algorithm {
	
	/**
	 *  Unused method
	 * @param L
	 * @param book
	 * @return
	 */
	public static ArrayList<Profile> step_0(Profile L, Book book) {
		
		// Retrieve books owned by the user L: this query is necessary to obtain
		// all informations of the book object of the reservation
		// The result will store in the bookRequested object.
		ArrayList<Book> books = L.getChasingBooks();
		System.out.println("Books owned by " + L.toString() + " are: " + books.toString());
		Book bookRequested = null;
		for (Book b : books) {
			if(b.getBCID().equals(book.getBCID())) {
				bookRequested = b;
				break;
			}
		}
		
		if(bookRequested == null) {
			System.out.println("Book requested for reservation: not found");
			return null;
		} else {
			System.out.println("Book requested for reservation: " + bookRequested.toString());
			
			// This TreeMap will store the distance of each user that have booked the specific book
			// with the relative distance from the reader the is the actual owner of the book object of the reservation
			TreeMap<Profile, Double> distancePrenotantiFromReader = new TreeMap<Profile, Double>();
			ArrayList<Profile> prenotantiForSpecificBook = bookRequested.getPrenotanti();
			for (Profile u : prenotantiForSpecificBook) {
				// Compute the distance from each user that made a reservation for the book and the 
				// reader the actually own the book
				distancePrenotantiFromReader.put(u, u.computeDistance(L));
			}
			ArrayList<Profile> temp = new ArrayList<Profile>((distancePrenotantiFromReader).keySet());
			return temp;
		}
	}
	
	/**
	 *  This method, as specified in the documentation, returns the list of users
	 *  involved in the reservation
	 * @param actualBookOwner
	 * @param me
	 * @return List of users
	 */
	public static AlgorithmResult step_1(Profile actualBookOwner, Profile me) {
		AlgorithmResult result = new AlgorithmResult();
		
		// Check if is possible for the users to meeting themself directly
		boolean isOverlapping = checkOverlap(actualBookOwner, me);
		ArrayList<Profile> userPath = new ArrayList<Profile>();
		
		if(isOverlapping) {
			result.directMeetingIsPossible = true;
			result.resultFlag = true;
		} else {
			// Try to create a radius area in which choose the possible users that could be active in the reservation.
			// This area is centered around this dummy user: we'll use it for a easier calculation of the user
			// that live in the area around.
			double radiusUserSearchArea = 0.5 * me.computeDistance(actualBookOwner);
			System.out.println("radius search area: " + radiusUserSearchArea);
			Localization dummyUser = centerOfInterestArea(actualBookOwner, me);
			System.out.println("User fittizio position --> lat: " + dummyUser.lat + " long: " + dummyUser.longit);
			
			// Search the possible users from all the users signed in to the community
			ArrayList<Profile> allUsers = getAllUsers();
			// ArrayList of the user that will be part of the reservation
			ArrayList<Profile> handToHandUsers = new ArrayList<Profile>();
			for (Profile u : allUsers) {
				System.out.println("User u: " + u.getUsername() + " distance from center "
				+ " " + u.computeDistance(dummyUser) + " and distance from me " + u.computeDistance(me));
				// If the user is near the center, and is not the user that actually own the book, add it to the possible
				// user chain.
				if((u.computeDistance(dummyUser) <= radiusUserSearchArea)
						&& !(u.getUsername().equals(actualBookOwner.getUsername()))) {
					handToHandUsers.add(u);
				}
			}
			
			// Sort the users selected by the distance from the actual owner of the book (L).
			TreeMap<Profile, Double> distanceUsersFromReader = new TreeMap<Profile, Double>();
			for (Profile u : handToHandUsers) {
				distanceUsersFromReader.put(u, u.computeDistance(actualBookOwner));
			}			
			SortedSet<Map.Entry<Profile,Double>> res = entriesSortedByValues(distanceUsersFromReader);
			System.out.println("User ordered by distance:  " + res.toString());	
			
			// Get only the User information, deleting the distance from actual owner,
			// but keeping the order.
			ArrayList<Profile> temp = new ArrayList<Profile>();
			Iterator<Entry<Profile,Double>> it = res.iterator();
			while(it.hasNext()) {
				 temp.add(it.next().getKey());
			}
			handToHandUsers.clear();
			handToHandUsers = new ArrayList<Profile>(temp);
			System.out.println("User between me and reader " + actualBookOwner.getUsername() + " ordered by distance: " + handToHandUsers.toString());
			
			// Create the user chain that could be part of the hand by hand book passage
			Profile previousUser = actualBookOwner;
			Profile nextUser = new Profile();
			
			if(checkOverlap(actualBookOwner, previousUser)) {
				userPath.add(previousUser);
				ArrayList<Profile> handToHandUsersCopy =(ArrayList<Profile>) handToHandUsers.clone();
				ArrayList<Profile> overlappingUsers = new ArrayList<Profile>();
				double max_radius = 0.0;
				double min_distance = Double.POSITIVE_INFINITY;
				
				// Until the algorithm hasn't found out a solution
				while(true) {
					max_radius = 0.0;
					min_distance = Double.POSITIVE_INFINITY;
					overlappingUsers.clear();
					
					for(Profile uu: handToHandUsersCopy) {
						if(checkOverlap(previousUser, uu)) {
							overlappingUsers.add(uu);
						}
					}
					System.out.println("Users that overlap for user " + previousUser.getUsername() + " : " + overlappingUsers.toString());
					
					if(overlappingUsers.isEmpty()) {
						// The chain is interrupted
						System.out.println("Not exists complete path from user " + previousUser.getUsername() + " to me");
						result.directMeetingIsPossible = false;
						result.resultFlag = false;
						break;
					} else {
						
						// Between all the nearest users, that overlap the actual one, choose the best one in an epsilon greedy way
						nextUser = Algorithm.greedyParadigm(previousUser, overlappingUsers);
						
						userPath.add(nextUser);
						previousUser = nextUser;
						handToHandUsersCopy.remove(nextUser);
						if(nextUser.getUsername().equals(me.getUsername())) {
							System.out.println("Path found");
							result.directMeetingIsPossible = false;
							result.resultFlag = true;
							break;
						}
					}
				}
			} else {
				System.out.println("Book request cannot automatically move away from user " + actualBookOwner.getUsername());
				userPath.clear();
				result.directMeetingIsPossible = false;
				result.resultFlag = false;
			}
		}
		
		// Also in case of missing complete path (for example for a missing user) update the path with 
		// the one found.
		result.userPath = userPath;
		return result;
	}
	
	
	/**
	 * Algorithm that choose the next user among all the overlapping users.
	 * It use a randomly way to choose between the nearest one and the one that as big possibility
	 * of moving (and so of reaching more users)
	 * @param previousUser
	 * @param users
	 * @return User choosen
	 */
	private static Profile greedyParadigm(Profile previousUser, ArrayList<Profile> users) {
		double epsilon = 0.1;
		double max_radius = 0.0;
		double min_distance = Double.POSITIVE_INFINITY;
		Profile result = null;
		
		for(Profile overLapUser: users) {
			// Epsilon greedy choice
			if(Math.random() < epsilon) {
				double dist = overLapUser.computeDistance(previousUser);
				if(dist < min_distance) {
					result = overLapUser;
					min_distance = dist;
				}
			} else {
				double area = overLapUser.getActionArea();
				if(area > max_radius) {
					result = overLapUser;
					max_radius = area;
				}
			}
		}
		
		if(result == null) {
			// No solutions found --> the user hasn't link with no one
			return null; 
		} else {
			return result;
		}
		
	}
	
	/**
	 * Funzione statica utilizzata per ordinare strutture <key,value> non in 
	 * base alla chiave, ma bensi secondo il valore.
	 * @param map
	 * @return Sorted map
	 */
	private static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	                int res = e1.getValue().compareTo(e2.getValue());
	                return res != 0 ? res : 1;
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}
	
	/**
	 * 
	 * Calcola le coordinate del punto centrale tra i due utenti
	 * 
	 * @param L
	 * @param me
	 * @return Middle point
	 */
	private static Localization centerOfInterestArea(Profile L, Profile me) {
		double latCenter = (L.getLatitude() + me.getLatitude()) / 2;
		double longitCenter = (L.getLongitude() + me.getLongitude()) / 2;
		Localization u = new Localization(latCenter, longitCenter);
		return u;
	}
	
	/**
	 * 
	 * Controlla se due generici utenti iscritti al programma hanno la possibilità di incontrarsi
	 * direttamente, in base a quelle che sono le disponibilità a spostarsi di ognuno.
	 * 
	 * @param actualBookOwner
	 * @param p
	 * @return Boolean flag
	 */
	private static boolean checkOverlap(Profile actualBookOwner, Profile p) {
		double r_L = actualBookOwner.getActionArea();
		double r_p = p.getActionArea();
		double distance = actualBookOwner.computeDistance(p) - r_L - r_p;
		boolean isOverlapping = distance <= 0;
		return isOverlapping;
	}

	/**
	 * Salvataggio sul database del percorso calcolato per mezzo dell'algoritmo
	 * @param users
	 * @return Risultato della query di insert
	 */
	public static boolean savePath(ArrayList<Profile> users) {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.storePath);
		
		int result = 0;
		String path = "";
		for(Profile i : users) {
			path += i.getUsername() + ";";
		}
		
		try {
			stmt.setString(1, path);
			stmt.setBigDecimal(2, new BigDecimal(getLastId()));
			result = stmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result == 1 ? true : false;
	}
	
	/**
	 * Query al database per ottenere l'id della prenotazione per mantenere consistenza nel
	 * salvataggio dei dati all'interno della tabella "Passaggio"
	 * @return id
	 */
	private static double getLastId() {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.getId);
		double id = 0;
		try {
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				id = rs.getBigDecimal(1).doubleValue();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * 
	 * Query al database per ottenere tutti gli utenti iscritti alla piattaforma.
	 * @return
	 */
	private static ArrayList<Profile> getAllUsers() {
		PreparedStatement stmtState = DBConnector.getDBConnector().prepareStatement(Queries.allUsersQuery);
		try {
			ResultSet rs = stmtState.executeQuery();
			ArrayList<Profile> users = new ArrayList<Profile>();
			Profile u;
			while(rs.next()) {
				u = new Profile();
				u.setUsername(rs.getString(1));
				u.setFirstName(rs.getString(2));
				u.setLastName(rs.getString(3));
				u.setLatitude(rs.getBigDecimal(7).doubleValue());
				u.setLongitude(rs.getBigDecimal(8).doubleValue());
				u.setActionArea(rs.getBigDecimal(9).doubleValue());
				users.add(u);
			}
			return users;	
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<Profile>();
		}
	}
	
	/**
	 * 
	 * Query al databse per ottenere le informazioni complete relativamente ad uno specifico username 
	 * 
	 * @param username
	 * @return Complete user object
	 */
	public static Profile getUserFromUsername(String username) {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.getUserInformationsQuery);
		Profile u = null;
		try {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				u = new Profile();
				u.setUsername(rs.getString(1));
				u.setFirstName(rs.getString(2));
				u.setLastName(rs.getString(3));
				u.setLatitude(rs.getBigDecimal(7).doubleValue());
				u.setLongitude(rs.getBigDecimal(8).doubleValue());
				u.setActionArea(rs.getBigDecimal(9).doubleValue());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return u;
	}
}