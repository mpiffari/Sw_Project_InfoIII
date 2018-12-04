package com.bc.bookcrossing.bookcrossing.Comunication;

import com.bc.bookcrossing.bookcrossing.GUI.DataDispatcherSingleton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import static com.bc.bookcrossing.bookcrossing.Comunication.CommunicationSingleton.singletonCommunication;

public class ProcessingSingleton implements GenerateRequests, ReceiveAnswer {
    public static final ProcessingSingleton singletonProcessing = new ProcessingSingleton();
    private ProcessingSingleton() {
    }

    private String separator = " ";

    @Override
    public void generateRequestForDataLogin(String username, String password) {
        singletonCommunication.send(RequestType.LOGIN + separator + username + separator + password);
    }

    @Override
    public void generateRequestForDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts, String password, int actionArea) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        singletonCommunication.send(RequestType.SIGN_IN + separator + name + separator + lastName + separator + df.format(DOB) +
                separator + contacts.toString() + separator + hash(password) + separator + actionArea);
    }

    @Override
    public void generateRequestForDataPickUp(String BCID) {
        singletonCommunication.send(RequestType.PICK_UP + BCID);
    }

    @Override
    public void generateRequestForDataTakenBooks() {
        singletonCommunication.send(RequestType.TAKEN_BOOKS.toString());
    }

    @Override
    public void generateRequestForDataBookRegistration(String ISBN) {
        singletonCommunication.send(RequestType.BOOK_REGISTRATION_AUTOMATIC + separator + ISBN);
    }

    @Override
    public void generateRequestForDataBookRegistration(String title, String author, Date pubblicationDate) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        singletonCommunication.send(RequestType.BOOK_REGISTRATION_MANUAL + separator + title + separator +author + separator + df.format(pubblicationDate));
    }

    @Override
    public void generateRequestForDataProfileInformations(String username, String password) {
        singletonCommunication.send(RequestType.PROFILE_INFO + separator + username + separator + hash(password));
    }

    /*
        When communication call Processing on specific callback, after call this
        DataDispatcherSingleton.ourInstance.callbackLogin();
     */

    public String hash(String password) {
        MessageDigest sha256 = null;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] passBytes = password.getBytes();
        byte[] passHash = sha256.digest(passBytes);
        return passHash.toString();
    }

    @Override
    public void sendAnswer(String data) {
        // TODO: make unpacking of string

        //TODO: result of unpacking
        /*RequestType r = RequestType.BOOK_REGISTRATION_AUTOMATIC;
        switch (r) {
            case LOGIN:
                DataDispatcherSingleton.ourInstance.callbackLogin();
        }*/
    }
}
