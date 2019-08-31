package algorithm_reservation_handler;

import java.awt.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import dataManager.DBConnector;
import user.User;
import user.UserLocalizer;

import java.util.*;

import book.Book;

public class Algorithm {
	
	static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
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
	
	@SuppressWarnings("unchecked")
	private TreeMap<User,Double> step_0(User L, Book book) {
		Book bookRequested = null;
		for (Book b : L.getChasingBooks()) {
			if(b.getBCID().equals(book.getBCID())) {
				bookRequested = b;
				break;
			}
		}
		TreeMap<User, Double> distancePrenotantiFromReader = new TreeMap<User, Double>();
		if(bookRequested == null) {
			//Error
			return distancePrenotantiFromReader;
		} else {
			//ArrayList<Double> distancePrenotantiFromRead = new ArrayList<Double>();
			ArrayList<User> prenotantiForSpecificBook = bookRequested.getPrenotanti();
			for (User u : prenotantiForSpecificBook) {
				distancePrenotantiFromReader.put(u, u.computeDistance(L));
			}
			distancePrenotantiFromReader = (TreeMap<User, Double>) entriesSortedByValues(distancePrenotantiFromReader);
		}
		return distancePrenotantiFromReader;
	}
	
	private void step_1(User L, Book b) {
		// Raggi si sovrappongono --> scambio direttamente
		
		TreeMap<User,Double> personWhoBooks = step_0(L, b);
		Map.Entry<User,Double> entry = personWhoBooks.entrySet().iterator().next();
		User nearestUser = entry.getKey();
		Double distance = entry.getValue();
		
		boolean isOverlapping = VerificaPuntoIncontro(L, nearestUser);
		
		if(isOverlapping) {
			// Incontro fisico: notificare utente L e utente nearestUser
		} else {
			// Cerco tutti gli utenti che si trovano tra lettore e prenotante
			double radiusUserSearchArea = 0.5 * distance;
			ArrayList<User> allUsers = getAllUsers();
			ArrayList<User> handToHandUsers = new ArrayList<User>();
			for (User u : allUsers) {
				if(u.computeDistance(L) <= radiusUserSearchArea || u.computeDistance(nearestUser) <= radiusUserSearchArea) {
					handToHandUsers.add(u);
				}
			}
			
			
			// Ordino hand to hand user per distanza dal reader (ovvero il Lettore L)
			TreeMap<User, Double> distanceUsersFromReader = new TreeMap<User, Double>();
			for (User u : handToHandUsers) {
				distanceUsersFromReader.put(u, u.computeDistance(L));
			}
			distanceUsersFromReader = (TreeMap<User, Double>) entriesSortedByValues(distanceUsersFromReader);
			
			// Creo il link tra gli utenti che hanno l'area di azione in comune
			ArrayList<User> userPath = new ArrayList<User>();
			User u = handToHandUsers.get(0);
			userPath.add(u);
			boolean overlappingFound = false;
			
			ArrayList<User> handToHandUsersCopy = new ArrayList<User>();
			Collections.copy(handToHandUsersCopy, handToHandUsers);
			handToHandUsersCopy.remove(0);
			while(true) {
				overlappingFound = false;
				
				for(User uu: handToHandUsersCopy) {
					if(VerificaPuntoIncontro(u, uu)) {
						userPath.add(uu);
						u = uu;
						handToHandUsersCopy.remove(uu);
						overlappingFound = true;
						break;
					}
				}
				
				if(!overlappingFound || handToHandUsersCopy.isEmpty()) {
					break;
				}
			}
		}
	}
	
	private ArrayList<User> getAllUsers() {
		String queryAllUsers = "SELECT * FROM UTENTE";
		PreparedStatement stmtState = DBConnector.getDBConnector().prepareStatement(queryAllUsers);
		try {
			ResultSet rs = stmtState.executeQuery();
			ArrayList<User> users = new ArrayList<User>();
			User u = new User();
			while(rs.next()) {
				u.setUsername(rs.getString(1));
				u.setFirstName(rs.getString(2));
				u.setLastName(rs.getString(3));
				u.setLatitude(rs.getDouble(7));
				u.setLongitude(rs.getDouble(8));
				u.setActionArea(rs.getDouble(9));
				users.add(u);
			}
			return users;	
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<User>();
		}
	}
	
	//TODO: rename this function
	private boolean VerificaPuntoIncontro(User L, User p) {
		
		double r_L = L.getActionArea();
		double r_p = p.getActionArea();
		double distance = L.computeDistance(p) - r_L - r_p;
		boolean isOverlapping = distance <= 0;
		return isOverlapping;
	}
	
	private UserLocalizer dataPersonWhoBooks(String username) {
		UserLocalizer personWhoBooksPosition = new UserLocalizer();
		
		// Ricerca dati prenotante
		String query1 = "SELECT RESIDENZALAT, RESIDENZALONG, RAGGIOAZIONE FROM Utente Where USERNAME = ?";
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(query1);

		try {
			stmt = DBConnector.getDBConnector().prepareStatement(query1);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				personWhoBooksPosition.lat = rs.getDouble(1);
				personWhoBooksPosition.longit = rs.getDouble(2); 
				personWhoBooksPosition.radius = rs.getDouble(3);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return personWhoBooksPosition;
	}
	
	private UserLocalizer dataReader(Book book) {
		UserLocalizer readerPosition = new UserLocalizer();
		
		// Ricerca dati lettore
		String bookBCIDForReservation = book.getBCID();
		String query1 = "SELECT USERNAME FROM Possesso Where BCID = ?";
		PreparedStatement stmt = DBConnector.getDBConnector().prepareStatement(query1);

		try {
			stmt.setString(1, bookBCIDForReservation);
			ResultSet rs = stmt.executeQuery();
			String readerUsername = null;
			if(rs.next()) {
				readerUsername = rs.getString(1);
			}
			
			if(readerUsername == null) {
				//return false;
			} else {
				String query2 = "SELECT RESIDENZALAT, RESIDENZALONG, RAGGIOAZIONE FROM Utente Where USERNAME = ?";
				stmt = DBConnector.getDBConnector().prepareStatement(query2);
				stmt.setString(1, readerUsername);
				rs = stmt.executeQuery();
				if(rs.next()) {
					readerPosition.lat = rs.getDouble(1);
					readerPosition.longit = rs.getDouble(2); 
					readerPosition.radius = rs.getDouble(3);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return readerPosition;
	}
}