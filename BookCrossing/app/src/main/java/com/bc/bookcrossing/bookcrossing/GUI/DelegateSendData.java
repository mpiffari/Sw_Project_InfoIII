package com.bc.bookcrossing.bookcrossing.GUI;

import com.bc.bookcrossing.bookcrossing.GUI.Observer.ObserverForUiInformation;

import java.util.Date;

public interface DelegateSendData {

    public boolean sendDataLogin(String username, String password);
    public boolean sendDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts, String password, int actionArea); //TODO : LAT e LONG
    public boolean sendDataPickUp(String BCID);
    public boolean sendDataTakenBooks();
    public boolean sendDataBookRegistration(String ISBN);
    public boolean sendDataBookRegistration(String title, String author, String yearOfPubb, String edition, String bookTypeDesc);
    public boolean sendDataProfileInformations(String username, String password);

    public void register(ObserverForUiInformation observerForUiInformation);
    public boolean unRegister(ObserverForUiInformation observerForUiInformation);


}
