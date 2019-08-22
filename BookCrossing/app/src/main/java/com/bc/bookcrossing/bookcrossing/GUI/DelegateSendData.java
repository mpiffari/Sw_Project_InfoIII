package com.bc.bookcrossing.bookcrossing.GUI;

import com.bc.bookcrossing.bookcrossing.GUI.Observer.ObserverForUiInformation;

import java.util.Date;

public interface DelegateSendData {

    public void sendDataLogin(String username, String password);
    public void sendDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts, String password, int actionArea); //TODO : LAT e LONG
    public void sendDataPickUp(String BCID);
    public void sendDataTakenBooks();
    public void sendDataBookRegistration(String ISBN);
    public void sendDataBookRegistration(String title, String author, String yearOfPubb, String edition, String bookTypeDesc);
    public void sendDataProfileInformations(String username, String password);

    public void register(ObserverForUiInformation observerForUiInformation);
    public boolean unRegister(ObserverForUiInformation observerForUiInformation);


}
