package algorithmReservationHandler;

import java.awt.List;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import dataManager.DBConnector;
import dataManager.Queries;
import user.User;
import user.UserLocalizationInfo;

import java.util.*;
import java.util.Map.Entry;

import book.Book;

public class Algorithm {
	
	// Unused
	public static ArrayList<User> step_0(User L, Book book) {
		Book bookRequested = null;
		ArrayList<Book> books = L.getChasingBooks();
		System.out.println("Books owned by " + L.toString() + " are: " + books.toString());
		for (Book b : books) {
			if(b.getBCID().equals(book.getBCID())) {
				bookRequested = b;
				break;
			}
		}
		
		TreeMap<User, Double> distancePrenotantiFromReader = new TreeMap<User, Double>();
		if(bookRequested == null) {
			System.out.println("Book requested for reservation: NULL");
			return null;
		} else {
			System.out.println("Book requested for reservation: " + bookRequested.toString());
			ArrayList<User> prenotantiForSpecificBook = bookRequested.getPrenotanti();
			for (User u : prenotantiForSpecificBook) {
				distancePrenotantiFromReader.put(u, u.computeDistance(L));
			}
			ArrayList<User> temp = new ArrayList<User>((distancePrenotantiFromReader).keySet());
			return temp;
		}
	}
	
	public static AlgorithmResult step_1(User L, User me) {
		// Raggi si sovrappongono --> scambio direttamente
		AlgorithmResult result = new AlgorithmResult();
		boolean isOverlapping = VerificaPuntoIncontro(L, me);
		ArrayList<User> userPath = new ArrayList<User>();
		
		if(isOverlapping) {
			result.directMeetingIsPossible = true;
			result.resultFlag = true;
		} else {
			// Cerco tutti gli utenti che si trovano tra lettore e prenotante
			double radiusUserSearchArea = 0.5 * me.computeDistance(L);
			
			//Creo utente fittizio che rappresenta il punto medio tra utente L e utente "me"

			UserLocalizationInfo center = centerOfInterestArea(L, me);
			
			System.out.println("User fittizio position --> lat: " + center.lat + " long: " + center.longit);
			System.out.println("radius search area: " + radiusUserSearchArea);
			ArrayList<User> allUsers = getAllUsers();
			ArrayList<User> handToHandUsers = new ArrayList<User>();
			for (User u : allUsers) {
				System.out.println("User u: " + u.getUsername() + " distance from center "
				+ " " + u.computeDistance(center) + " and distance from me " + u.computeDistance(me));
				if((u.computeDistance(center) <= radiusUserSearchArea)
						&& !(u.getUsername().equals(L.getUsername()))) {
					handToHandUsers.add(u);
				}
			}
			
			// Ordino hand to hand user per distanza dal reader (ovvero il Lettore L)
			TreeMap<User, Double> distanceUsersFromReader = new TreeMap<User, Double>();
			for (User u : handToHandUsers) {
				distanceUsersFromReader.put(u, u.computeDistance(L));
			}			
			SortedSet<Map.Entry<User,Double>> res = entriesSortedByValues(distanceUsersFromReader);
			System.out.println("Res:  " + res.toString());	
			ArrayList<User> temp = new ArrayList<User>();
		
			Iterator<Entry<User,Double>> it = res.iterator();
			while(it.hasNext()) {
				 temp.add(it.next().getKey());
			}
			handToHandUsers.clear();
			handToHandUsers = new ArrayList<User>(temp);
			System.out.println("User between me and reader " + L.getUsername() + " ordered by distance: " + handToHandUsers.toString());
			
			// Creo il link tra gli utenti che hanno l'area di azione in comune
			User previousUser = L;
			User nextUser = new User();
			
			if(VerificaPuntoIncontro(L, previousUser)) {
				userPath.add(previousUser);
				ArrayList<User> handToHandUsersCopy =(ArrayList<User>) handToHandUsers.clone();
				ArrayList<User> overlappingUsers = new ArrayList<User>();
				double max_radius = 0.0;
				double min_distance = Double.POSITIVE_INFINITY;
				
				//TODO: move this comment to documentation
				// Epsilon for epsilon greedy algorithm: during the path calculation for the reservation
				// we choose usually the next user, as the nearest one.
				// Using the greedy algorithm, with a probability that depends on the epsilon variable value, we choose the user
				// with the biggest radius.
				// epsilon lower --> algorithm stronger (always foound path to me)
				// epsilon higher --> algorithm found sorthest path
				double epsilon = 0.1;
				//= new ArrayList<User>(handToHandUsers.size());
				//Collections.copy(handToHandUsersCopy, handToHandUsers);
				while(true) {
					max_radius = 0.0;
					min_distance = Double.POSITIVE_INFINITY;
					overlappingUsers.clear();
					
					for(User uu: handToHandUsersCopy) {
						if(VerificaPuntoIncontro(previousUser, uu)) {
							overlappingUsers.add(uu);
						}
					}
					System.out.println("Users that overlap for user " + previousUser.getUsername() + " : " + overlappingUsers.toString());
					
					if(overlappingUsers.isEmpty()) {
						System.out.println("Not exists complete path from user " + previousUser.getUsername() + " to me");
						result.directMeetingIsPossible = false;
						result.resultFlag = false;
						break;
					} else {
						for(User overLapUser: overlappingUsers) {
							if(Math.random() < epsilon) {
								double dist = overLapUser.computeDistance(previousUser);
								if(overLapUser.computeDistance(previousUser) < min_distance) {
									nextUser = overLapUser;
									min_distance = dist;
								}
							} else {
								if(overLapUser.getActionArea() > max_radius) {
									nextUser = overLapUser;
									max_radius = overLapUser.getActionArea();
								}
							}
							
						}
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
				System.out.println("Book request cannot automatically move away from user " + L.getUsername());
				userPath.clear();
				result.directMeetingIsPossible = false;
				result.resultFlag = false;
			}
		}
		result.userPath = userPath;
		return result;
	}
	
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
	
	private static UserLocalizationInfo centerOfInterestArea(User L, User me) {
		double latCenter = (L.getLatitude() + me.getLatitude()) / 2;
		double longitCenter = (L.getLongitude() + me.getLongitude()) / 2;
		UserLocalizationInfo u = new UserLocalizationInfo(latCenter, longitCenter);
		return u;
	}
	
	private static ArrayList<User> getAllUsers() {
		PreparedStatement stmtState = DBConnector.getDBConnector().prepareStatement(Queries.allUsersQuery);
		try {
			ResultSet rs = stmtState.executeQuery();
			ArrayList<User> users = new ArrayList<User>();
			User u;
			while(rs.next()) {
				u = new User();
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
			return new ArrayList<User>();
		}
	}
	
	//TODO: rename this function
	private static boolean VerificaPuntoIncontro(User L, User p) {
		double r_L = L.getActionArea();
		double r_p = p.getActionArea();
		double distance = L.computeDistance(p) - r_L - r_p;
		boolean isOverlapping = distance <= 0;
		return isOverlapping;
	}
	
	public static User getUserFromUsername(String username) {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.getUserInformationsQuery);
		User u = null;
		try {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				u = new User();
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
	
	public static boolean savePath(ArrayList<User> users) {
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(Queries.storePath);
		
		int result = 0;
		String path = "";
		for(User i : users) {
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
}