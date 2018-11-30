package com.bc.bookcrossing.bookcrossing;

import com.bc.bookcrossing.bookcrossing.observerInterfaces.ObserverForUiInformation;

import java.util.Date;

public class DataDispatcher implements DelegateSendData {
    @Override
    public void sendDataLogin(String username, String password) {

    }

    @Override
    public void sendDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts, String password, int actionArea) {

    }

    @Override
    public void sendDataPickUp(String BCID) {

    }

    @Override
    public void sendDataTakenBooks() {

    }

    @Override
    public void sendDataBookRegistration(String ISBN) {


    }

    @Override
    public void sendDataBookRegistration(String title, String author, Date pubblicationDate) {

    }

    @Override
    public void register(ObserverForUiInformation observerForUiInformation) {

    }

    @Override
    public void unRegister(ObserverForUiInformation observerForUiInformation) {

    }
}
