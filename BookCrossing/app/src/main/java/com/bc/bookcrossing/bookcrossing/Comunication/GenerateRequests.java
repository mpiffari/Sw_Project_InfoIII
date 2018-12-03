package com.bc.bookcrossing.bookcrossing.Comunication;

import com.bc.bookcrossing.bookcrossing.Repository.DelegateSendData;

import java.util.Date;

public interface GenerateRequests {

    public void generateRequestForDataLogin(String username, String password);
    public void generateRequestForDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts, String password, int actionArea); //TODO : LAT e LONG
    public void generateRequestForDataPickUp(String BCID);
    public void generateRequestForDataTakenBooks();
    public void generateRequestForDataBookRegistration(String ISBN);
    public void generateRequestForDataBookRegistration(String title, String author, Date pubblicationDate);
    public void generateRequestForDataProfileInformations(String username, String password);

    public void register(DelegateSendData observerForDispatching);
    public boolean unRegister(DelegateSendData observerForDispatching);

}
