package com.bc.bookcrossing.bookcrossing.RequestManager;

import com.bc.bookcrossing.bookcrossing.GUI.DataDispatcherSingleton;
import com.bc.bookcrossing.bookcrossing.Globals;
import com.bc.bookcrossing.bookcrossing.Structures.Book;
import com.bc.bookcrossing.bookcrossing.Structures.LoginStatus;
import com.bc.bookcrossing.bookcrossing.Structures.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import static com.bc.bookcrossing.bookcrossing.RequestManager.Communication.singletonCommunication;

public class Processing implements GenerateRequests, ReceiveAnswer {
    private static final Processing singletonProcessing = new Processing();
    private static final String separator = ";";

    private Processing() {
    }

    public static Processing getInstance(){
        return singletonProcessing;
    }

    @Override
    public void generateRequestForDataLogin(User user) {
        String username = user.getUsername();
        singletonCommunication.send(username + separator + Globals.reqType  + RequestType.LOGIN.toString() + separator + user.toString());
    }

    @Override
    public void generateRequestForDataBookRegistration(Book book) {
        //TODO: check username
        String username = "Pippo";
        singletonCommunication.send(username + separator + Globals.reqType + RequestType.BOOK_REGISTRATION_MANUAL.toString() + separator + book.toString());
    }

    @Override
    public void generateRequestForDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts, String password, int actionArea) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        singletonCommunication.send(RequestType.SIGN_IN.toString() + separator + name + separator + lastName + separator + df.format(DOB) +
                separator + contacts.toString() + separator + hash(password) + separator + actionArea);
    }

    @Override
    public void generateRequestForDataPickUp(String BCID) {
        singletonCommunication.send(RequestType.PICK_UP.toString() + BCID);
    }

    @Override
    public void generateRequestForDataTakenBooks() {
        singletonCommunication.send(RequestType.TAKEN_BOOKS.toString());
    }

    @Override
    public void generateRequestForDataBookRegistration(String ISBN) {
        String username = "Pippo";
        singletonCommunication.send(username + separator + Globals.reqType + RequestType.BOOK_REGISTRATION_AUTOMATIC.toString() + separator + ISBN);
    }

    @Override
    public void generateRequestForDataProfileInformations(String username, String password) {
        singletonCommunication.send(RequestType.PROFILE_INFO.toString() + separator + username + separator + hash(password));
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
    public void processAnswer(String data) {
        int i = data.indexOf(";", 0);
        int j = data.indexOf(":", 0);
        RequestType requestType = RequestType.getEnumReqType(data.substring(j+1, i));

        switch (requestType) {
            case BOOK_REGISTRATION_MANUAL:
                int k = data.indexOf(":", i+1);
                int kk = data.indexOf(":", k+1);
                int res = Integer.parseInt(data.substring(k+1,k+2));
                String BCID = data.substring(kk+1);
                DataDispatcherSingleton.getInstance().callbackRegistration(res == 1 ? true : false, BCID);
                break;
            case BOOK_RESERVATION:
                break;
            case LOGIN:
                int result = data.indexOf(":", i+1);
                String msg = data.substring(result+1);
                boolean flag =false;
                LoginStatus logStaus = LoginStatus.NONE;
                switch (msg) {
                    case "Success":
                        flag = true;
                        logStaus = LoginStatus.SUCCESS;
                        break;
                    case "KO_Username":
                        flag = false;
                        logStaus = LoginStatus.WRONG_USERNAME;
                        break;
                    case "KO_Password":
                        flag = false;
                        logStaus = LoginStatus.WRONG_PASSWORD;
                        break;
                }
                DataDispatcherSingleton.getInstance().callbackLogin(flag, logStaus);
                break;
            case SIGN_IN:
                break;
            case BOOK_REGISTRATION_AUTOMATIC:
                break;
            case PROFILE_INFO:
                break;
            case TAKEN_BOOKS:
                break;
            case PICK_UP:
                break;
        }
    }
}
