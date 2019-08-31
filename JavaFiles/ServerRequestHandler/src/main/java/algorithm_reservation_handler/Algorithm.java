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
	
	private void step_0(User L, Book book) {
		Book bookRequested;
		for (Book b : L.getChasingBooks()) {
			if(b.getBCID().equals(book.getBCID())) {
				bookRequested = b;
				break;
			}
		}
		
		
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