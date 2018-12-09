package com.bc.bookcrossing.bookcrossing.GUI;

import com.bc.bookcrossing.bookcrossing.BookInfo;
import com.bc.bookcrossing.bookcrossing.Comunication.ReceiveData;
import com.bc.bookcrossing.bookcrossing.GUI.Observer.ObserverBookDataRegistration;
import com.bc.bookcrossing.bookcrossing.GUI.Observer.ObserverDataBookPickUp;
import com.bc.bookcrossing.bookcrossing.GUI.Observer.ObserverDataBookTaken;
import com.bc.bookcrossing.bookcrossing.GUI.Observer.ObserverDataLogin;
import com.bc.bookcrossing.bookcrossing.GUI.Observer.ObserverDataProfile;
import com.bc.bookcrossing.bookcrossing.GUI.Observer.ObserverDataSignIn;
import com.bc.bookcrossing.bookcrossing.GUI.Observer.ObserverForUiInformation;
import com.bc.bookcrossing.bookcrossing.LoginInStatus;
import com.bc.bookcrossing.bookcrossing.SignInStatus;
import com.bc.bookcrossing.bookcrossing.UserInformations;
import com.bc.bookcrossing.bookcrossing.Comunication.ProcessingSingleton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataDispatcherSingleton implements ReceiveData, DelegateSendData {
    public static final DataDispatcherSingleton ourInstance = new DataDispatcherSingleton();
    private DataDispatcherSingleton() {
    }

    private ProcessingSingleton p = ProcessingSingleton.getInstance();
    //region Declaration of vector of observer
    private List<ObserverBookDataRegistration> observersBookDataRegistration = new ArrayList<>();
    private List<ObserverDataBookPickUp> observersDataBookPickUp = new ArrayList<>();
    private List<ObserverDataBookTaken> observersDataBookTaken = new ArrayList<>();
    private List<ObserverDataLogin> observersDataLogin = new ArrayList<>();
    private List<ObserverDataSignIn> observersDataSignIn = new ArrayList<>();
    private List<ObserverDataProfile> observersDataProfile = new ArrayList<>();
    //endregion

    //Metodi per piccolo controllo: stringhe vuote o in formato non valido

    //region Delegate SendData functions
    @Override
    public void sendDataLogin(String username, String password) {
        //TODO CONTROLLO MINIMO
        p.generateRequestForDataLogin(username,password);
    }

    @Override
    public void sendDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts, String password, int actionArea) {
        //TODO CONTROLLO MINIMO
        p.generateRequestForDataSignIn(name, lastName, username, DOB, contacts, password, actionArea);
    }

    @Override
    public void sendDataPickUp(String BCID) {
        //TODO CONTROLLO MINIMO
        p.generateRequestForDataPickUp(BCID);
    }

    @Override
    public void sendDataTakenBooks() {
        //TODO CONTROLLO MINIMO
        p.generateRequestForDataTakenBooks();
    }

    @Override
    public void sendDataBookRegistration(String ISBN) {
        //TODO CONTROLLO MINIMO
        p.generateRequestForDataBookRegistration(ISBN);
    }

    @Override
    public void sendDataBookRegistration(String title, String author, Date pubblicationDate) {
        //TODO CONTROLLO MINIMO
        p.generateRequestForDataBookRegistration(title, author, pubblicationDate);
    }

    @Override
    public void sendDataProfileInformations(String username, String password) {
        //TODO CONTROLLO MINIMO
        p.generateRequestForDataProfileInformations(username, password);
    }
    //endregion


    //region callback ReceiveData functions
    @Override
    public void callbackRegistration(boolean result, String bookCodeID) {
        for (ObserverBookDataRegistration obs : observersBookDataRegistration) {
            obs.callbackRegistration(result, bookCodeID);

        }
    }

    @Override
    public void callbackPickUp(short bookStatus) {
        for (ObserverDataBookPickUp obs : observersDataBookPickUp) {
            obs.callbackPickUp(bookStatus);

        }

    }

    @Override
    public void callbackBookTaken(ArrayList<BookInfo> bookInformations) {
        for (ObserverDataBookTaken obs : observersDataBookTaken) {
            obs.callbackBookTaken(bookInformations);
        }

    }

    @Override
    public void callbackLogin(List<LoginInStatus> status) {
        for (ObserverDataLogin obs : observersDataLogin) {
            obs.callbackLogin(status);
        }

    }

    @Override
    public void callbackProfile(UserInformations userInformations) {
        for (ObserverDataProfile obs : observersDataProfile) {
            obs.callbackProfile(userInformations);

        }

    }

    @Override
    public void callbackSignIn(List<SignInStatus> status) {

    }
    //endregion

    @Override
    public void register(ObserverForUiInformation observerForUiInformation) {

        if ((observerForUiInformation instanceof ObserverBookDataRegistration) && (!observersBookDataRegistration.contains(observerForUiInformation))) {
            observersBookDataRegistration.add((ObserverBookDataRegistration) observerForUiInformation);
        } else if ((observerForUiInformation instanceof ObserverDataBookPickUp) && (!observersDataBookPickUp.contains(observerForUiInformation))) {
            observersDataBookPickUp.add((ObserverDataBookPickUp) observerForUiInformation);
        } else if ((observerForUiInformation instanceof ObserverDataBookTaken) && (!observersDataBookTaken.contains(observerForUiInformation))) {
            observersDataBookTaken.add((ObserverDataBookTaken) observerForUiInformation);
        } else if ((observerForUiInformation instanceof ObserverDataLogin) && (!observersDataLogin.contains(observerForUiInformation))) {
            observersDataLogin.add((ObserverDataLogin) observerForUiInformation);
        } else if ((observerForUiInformation instanceof ObserverDataSignIn) && (!observersDataSignIn.contains(observerForUiInformation))) {
            observersDataSignIn.add((ObserverDataSignIn) observerForUiInformation);
        } else if ((observerForUiInformation instanceof ObserverDataProfile) && (!observersDataProfile.contains(observerForUiInformation))) {
            observersDataProfile.add((ObserverDataProfile) observerForUiInformation);
        }
    }

    @Override
    public boolean unRegister(ObserverForUiInformation observerForUiInformation) {

        if (observerForUiInformation instanceof ObserverBookDataRegistration)
            return observersBookDataRegistration.remove(observerForUiInformation);

        else if (observerForUiInformation instanceof ObserverDataBookPickUp)
            return observersDataBookPickUp.remove(observerForUiInformation);

        else if (observerForUiInformation instanceof ObserverDataBookTaken)
            return observersDataBookTaken.remove(observerForUiInformation);

        else if (observerForUiInformation instanceof ObserverDataLogin)
            return observersDataLogin.remove(observerForUiInformation);

        else if (observerForUiInformation instanceof ObserverDataProfile)
            return observersDataProfile.remove(observerForUiInformation);

        else
            return observersDataSignIn.remove(observerForUiInformation);


    }
}