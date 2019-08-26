package com.bc.bookcrossing.bookcrossing.RequestManager;

import com.bc.bookcrossing.bookcrossing.Structures.Book;
import com.bc.bookcrossing.bookcrossing.Structures.User;
import java.util.Date;

public interface GenerateRequests {
	   boolean generateRequestForDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts, String password, int actionArea); //TODO : LAT e LONG
   boolean generateRequestForDataPickUp(String BCID);
   boolean generateRequestForDataTakenBooks();
   boolean generateRequestForDataBookRegistrationAuto(Book book, String ISBN);
   boolean generateRequestForDataBookRegistrationManual(Book book);
   boolean generateRequestForDataLogin(User user);
   boolean generateRequestForDataProfileInformations(String username, String password);
   boolean generateRequestForDataBookSearch(String title, String author);
}
