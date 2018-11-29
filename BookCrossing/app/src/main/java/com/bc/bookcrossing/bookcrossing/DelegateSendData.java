package com.bc.bookcrossing.bookcrossing;

import com.bc.bookcrossing.bookcrossing.observerInterfaces.ObserverDataBookPickUp;
import com.bc.bookcrossing.bookcrossing.observerInterfaces.ObserverDataBookTaken;
import com.bc.bookcrossing.bookcrossing.observerInterfaces.ObserverDataLogin;
import com.bc.bookcrossing.bookcrossing.observerInterfaces.ObserverDataSignIn;

import java.util.Date;

public interface DelegateSendData {

    public void sendDataLogin(String username, String password);
    public void sendDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts, String password, int actionArea); //TODO : LAT e LONG
    public void sendDataPickUp(String BCID);
    public void sendDataTakenBooks();
    public void sendDataBookRegistration(String ISBN);
    public void sendDataBookRegistration(String title, String author, Date pubblicationDate);

    public void register(Observer observer);
    public void unRegister(Observer observer);


}
