package com.bc.bookcrossing.src.GUI.DataDispatcher;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.bc.bookcrossing.src.GUI.Observer.ObserverDataBookReservation;
import com.bc.bookcrossing.src.GUI.Observer.ObserverDataBookResearch;
import com.bc.bookcrossing.src.ClientModels.BookInfo;
import com.bc.bookcrossing.src.requestManager.ReceiveData;
import com.bc.bookcrossing.src.GUI.Observer.ObserverDataBookRegistration;
import com.bc.bookcrossing.src.GUI.Observer.ObserverDataBookPickUp;
import com.bc.bookcrossing.src.GUI.Observer.ObserverDataBookTaken;
import com.bc.bookcrossing.src.GUI.Observer.ObserverDataLogin;
import com.bc.bookcrossing.src.GUI.Observer.ObserverDataProfile;
import com.bc.bookcrossing.src.GUI.Observer.ObserverDataSignIn;
import com.bc.bookcrossing.src.GUI.Observer.ObserverForUiInformation;
import com.bc.bookcrossing.src.ClientModels.Enums.LoginStatus;
import com.bc.bookcrossing.src.ClientModels.Enums.SignInStatus;
import com.bc.bookcrossing.src.ClientModels.User;
import com.bc.bookcrossing.src.ClientModels.UserInformations;
import com.bc.bookcrossing.src.requestManager.Processing;
import com.bc.bookcrossing.src.ClientModels.Book;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Implementazione dei metodi per poter offrire un interfaccia a tutti coloro che
 * desiderano spedire richieste al server.
 * Presenta inoltre strutture dati per poter gestire le registrazione/deregistrazione dei fragments
 * e notificarne la presenza delle risposte.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public class DataDispatcherSingleton implements ReceiveData, DelegateSendData {
    public static final DataDispatcherSingleton ourInstance = new DataDispatcherSingleton();
    private DataDispatcherSingleton() { }

    public static DataDispatcherSingleton getInstance(){
        return ourInstance;
    }
    private Processing p = Processing.getInstance();

    //region Declaration of vector of observers
    // ===================== ITERAZIONE 1 ===================================
    private List<ObserverDataBookRegistration> observersDataBookRegistration = new ArrayList<>(); // Used also for automatic and manual registration
    private List<ObserverDataBookResearch> observarDataBookResearch = new ArrayList<>();

    // ===================== ITERAZIONE 2 ===================================
    private List<ObserverDataLogin> observersDataLogin = new ArrayList<>();
    private List<ObserverDataBookReservation> observarDataBookReservation = new ArrayList<>();

    // ===================== PROSSIMA ITERAZIONE ===================================
    private List<ObserverDataBookPickUp> observersDataBookPickUp = new ArrayList<>();
    private List<ObserverDataBookTaken> observersDataBookTaken = new ArrayList<>();
    private List<ObserverDataSignIn> observersDataSignIn = new ArrayList<>();
    private List<ObserverDataProfile> observersDataProfile = new ArrayList<>();
    //endregion

    //region Delegate SendData functions
    // ===================== ITERAZIONE 1 ===================================
    @Override
    public boolean sendDataBookRegistrationManual(String title, String author, String yearOfPubb, String edition, String bookTypeDesc) {
        // BookType bookType = BookType.fromString(bookTypeDesc);
        Book newBook = new Book(title, author, Integer.parseInt(yearOfPubb), Integer.parseInt(edition), bookTypeDesc);
        newBook.setBCID("");

        return p.generateRequestForDataBookRegistrationManual(newBook);
    }

    @Override
    public boolean sendDataBookSearch(String title, String author) {
        return p.generateRequestForDataBookSearch(title, author);
    }


    // ===================== ITERAZIONE 2 ===================================
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean sendDataLogin(String username, String password) {
        // HASH 256
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encodedPsw = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        User u = new  User(username, bytesToHex(encodedPsw));
        return p.generateRequestForDataLogin(u);
    }

    @Override
    public boolean sendDataBookRegistrationAuto(String title, String author, String yearOfPubb, String edition, String bookTypeDesc, String ISBN) {
        Book newBook = new Book(title, author, Integer.parseInt(yearOfPubb), Integer.parseInt(edition), bookTypeDesc, ISBN);
        newBook.setBCID("");
        return p.generateRequestForDataBookRegistrationAuto(newBook);
    }

    @Override
    public boolean sendDataBookReservation(Book bookForReservation) {
        return p.generateRequestForDataBookReservation(bookForReservation);
    }


    // ===================== PROSSIMA ITERAZIONE ===================================
    @Override
    public boolean sendDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts, String password, int actionArea) {
        return p.generateRequestForDataSignIn(name, lastName, username, DOB, contacts, password, actionArea);
    }

    @Override
    public boolean sendDataPickUp(String BCID) {
        return p.generateRequestForDataPickUp(BCID);
    }

    @Override
    public boolean sendDataTakenBooks() {
        return p.generateRequestForDataTakenBooks();
    }

    @Override
    public boolean sendDataProfileInformations(String username, String password) {
        return p.generateRequestForDataProfileInformations(username, password);
    }
    //endregion

    //region callback ReceiveData functions
    // ===================== ITERAZIONE 1 ===================================
    @Override
    public void callbackRegistration(boolean result, String bookCodeID) { // Manual and automatic registration
        for (ObserverDataBookRegistration obs : observersDataBookRegistration) {
            obs.notifyRegistration(result, bookCodeID);
        }
    }

    @Override
    public void callbackBookSearch(boolean b, List<Book> booksFound) {
        for (ObserverDataBookResearch obs : observarDataBookResearch) {
            obs.notifyBookSearch(b, booksFound);
        }
    }

    // ===================== ITERAZIONE 2 ===================================
    @Override
    public void callbackLogin(final boolean result, LoginStatus s) {
        for (ObserverDataLogin obs : observersDataLogin) {
            obs.notifyLogin(result,s);
        }
    }

    @Override
    public void callbackReservation(boolean result) {
        for (ObserverDataBookReservation obs : observarDataBookReservation) {
            obs.notifyReservation(result);
        }
    }


    // ===================== PROSSIMA ITERAZIONE ===================================
    @Override
    public void callbackPickUp(short bookStatus) {
        for (ObserverDataBookPickUp obs : observersDataBookPickUp) {
            obs.notifyPickUp(bookStatus);
        }
    }

    @Override
    public void callbackBookTaken(ArrayList<BookInfo> bookInformations) {
        for (ObserverDataBookTaken obs : observersDataBookTaken) {
            obs.notifyBookTaken(bookInformations);
        }
    }

    @Override
    public void callbackProfile(UserInformations userInformations) {
        for (ObserverDataProfile obs : observersDataProfile) {
            obs.notifyProfile(userInformations);
        }
    }

    @Override
    public void callbackSignIn(List<SignInStatus> status) {
        for (ObserverDataSignIn obs : observersDataSignIn) {
            obs.notifySignIn(status);
        }
    }
    //endregion

    //Function related to SHA-256 encoding: encode password
    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Register observer for receiving back data
     * @param observerForUiInformation
     */
    @Override
    public void register(ObserverForUiInformation observerForUiInformation) {
        // ===================== ITERAZIONE 1 ===================================
        if ((observerForUiInformation instanceof ObserverDataBookRegistration) && (!observersDataBookRegistration.contains(observerForUiInformation))) {
            observersDataBookRegistration.add((ObserverDataBookRegistration) observerForUiInformation);
        } else if ((observerForUiInformation instanceof ObserverDataBookResearch) && (!observarDataBookResearch.contains(observerForUiInformation))) {
            observarDataBookResearch.add((ObserverDataBookResearch) observerForUiInformation);
        }

        // ===================== ITERAZIONE 2 ===================================
        else if ((observerForUiInformation instanceof ObserverDataBookReservation) && (!observarDataBookReservation.contains(observerForUiInformation))) {
            observarDataBookReservation.add((ObserverDataBookReservation) observerForUiInformation);
        } else if ((observerForUiInformation instanceof ObserverDataLogin) && (!observersDataLogin.contains(observerForUiInformation))) {
            observersDataLogin.add((ObserverDataLogin) observerForUiInformation);
        }

        // ===================== PROSSIMA ITERAZIONE ===================================
        else if ((observerForUiInformation instanceof ObserverDataBookPickUp) && (!observersDataBookPickUp.contains(observerForUiInformation))) {
            observersDataBookPickUp.add((ObserverDataBookPickUp) observerForUiInformation);
        } else if ((observerForUiInformation instanceof ObserverDataBookTaken) && (!observersDataBookTaken.contains(observerForUiInformation))) {
            observersDataBookTaken.add((ObserverDataBookTaken) observerForUiInformation);
        }  else if ((observerForUiInformation instanceof ObserverDataSignIn) && (!observersDataSignIn.contains(observerForUiInformation))) {
            observersDataSignIn.add((ObserverDataSignIn) observerForUiInformation);
        } else if ((observerForUiInformation instanceof ObserverDataProfile) && (!observersDataProfile.contains(observerForUiInformation))) {
            observersDataProfile.add((ObserverDataProfile) observerForUiInformation);
        }
    }

    /**
     * deRegister observer from receiving data
     * @param observerForUiInformation
     * @return
     */
    @Override
    public boolean unRegister(ObserverForUiInformation observerForUiInformation) {
        // ===================== ITERAZIONE 1 ===================================
        if (observerForUiInformation instanceof ObserverDataBookRegistration) {
            return observersDataBookRegistration.remove(observerForUiInformation);
        } else if (observerForUiInformation instanceof ObserverDataBookResearch) {
            return observarDataBookResearch.remove(observerForUiInformation);
        }

        // ===================== ITERAZIONE 2 ===================================
        else if (observerForUiInformation instanceof ObserverDataBookReservation) {
            return observarDataBookReservation.remove(observerForUiInformation);
        } else if (observerForUiInformation instanceof ObserverDataLogin) {
            return observersDataLogin.remove(observerForUiInformation);
        }

        // ===================== PROSSIMA ITERAZIONE ===================================
        else if (observerForUiInformation instanceof ObserverDataBookPickUp) {
            return observersDataBookPickUp.remove(observerForUiInformation);
        } else if (observerForUiInformation instanceof ObserverDataBookTaken) {
            return observersDataBookTaken.remove(observerForUiInformation);
        }  else if (observerForUiInformation instanceof ObserverDataProfile) {
            return observersDataProfile.remove(observerForUiInformation);
        } else if (observerForUiInformation instanceof ObserverDataSignIn) {
            return observersDataSignIn.remove(observerForUiInformation);
        }
        return false;
    }
}
