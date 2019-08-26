package com.bc.bookcrossing.bookcrossing.RequestManager;

import com.bc.bookcrossing.bookcrossing.Structures.Book;
import com.bc.bookcrossing.bookcrossing.Structures.User;
import java.util.Date;

public interface GenerateRequests {
    public boolean generateRequestForDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts, String password, int actionArea); //TODO : LAT e LONG
    public boolean generateRequestForDataPickUp(String BCID);
    public boolean generateRequestForDataTakenBooks();
    public boolean generateRequestForDataBookRegistrationAuto(Book book, String ISBN);
    public boolean generateRequestForDataLogin(User user);
    public boolean generateRequestForDataProfileInformations(String username, String password);
    public boolean generateRequestForDataBookRegistrationManual(Book book);
}
