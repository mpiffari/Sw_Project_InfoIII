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
		TreeMap<User, Double> distancePrenotantiFromRead = new TreeMap<User, Double>();
		if(bookRequested == null) {
			//Error
			return distancePrenotantiFromRead;
		} else {
			//ArrayList<Double> distancePrenotantiFromRead = new ArrayList<Double>();
			ArrayList<User> prenotantiForSpecificBook = bookRequested.getPrenotanti();
			for (User u : prenotantiForSpecificBook) {
				distancePrenotantiFromRead.put(u, u.computeDistance(L));
			}
			distancePrenotantiFromRead = (TreeMap<User, Double>) entriesSortedByValues(distancePrenotantiFromRead);
		}
		return distancePrenotantiFromRead;
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
			// else algorithm
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