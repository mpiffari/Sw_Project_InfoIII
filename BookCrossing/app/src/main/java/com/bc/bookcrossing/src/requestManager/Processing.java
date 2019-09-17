package com.bc.bookcrossing.src.requestManager;

import android.util.Log;

import com.bc.bookcrossing.src.GUI.DataDispatcher.DataDispatcherSingleton;
import com.bc.bookcrossing.src.Globals;
import com.bc.bookcrossing.src.ClientModels.Book;
import com.bc.bookcrossing.src.ClientModels.Enums.LoginStatus;
import com.bc.bookcrossing.src.ClientModels.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.bc.bookcrossing.src.requestManager.communication.Communication.singletonCommunication;

/**
 *
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public class Processing implements GenerateRequests, ReceiveAnswer {
    private static final Processing singletonProcessing = new Processing();
    private static final String separator = ";";

    private Processing() {}

    public static Processing getInstance(){
        return singletonProcessing;
    }

    @Override
    public boolean generateRequestForDataLogin(User user) {
        String username = user.getUsername();
        return singletonCommunication.send(username + separator + Globals.reqType  + RequestType.LOGIN.toString() + separator + user.toString());
    }

    @Override
    public boolean generateRequestForDataBookSearch(String title, String author) {
        if (Globals.usernameLoggedIn == null) {
            Log.d("Processing", "[ASSERT]: no username saved!");
            return false;
        } else {
            String username = Globals.usernameLoggedIn;
            String request = "";
            if (title.length() == 0) {
                request = "AUTHOR:" + author;
            } else if (author.length() == 0) {
                request = "TITLE:" + title;
            } else {
                request = "TITLE:" + title + separator + "AUTHOR:" + author;
            }
            return singletonCommunication.send(username + separator + Globals.reqType + RequestType.BOOK_SEARCH.toString() + separator + request);
        }
    }

    @Override
    public boolean generateRequestForDataBookReservation(Book bookForReservation) {
        if (Globals.usernameLoggedIn == null) {
            Log.d("Processing", "[ASSERT]: no username saved!");
            return false;
        } else {
            String username = Globals.usernameLoggedIn;
            return singletonCommunication.send(username + separator + Globals.reqType + RequestType.BOOK_RESERVATION.toString() + separator + bookForReservation.encode());
        }
    }

    @Override
    public boolean generateRequestForDataBookRegistrationAuto(Book book) {
        if (Globals.usernameLoggedIn == null) {
            Log.d("Processing", "[ASSERT]: no username saved!");
            return false;
        } else {
            String username = Globals.usernameLoggedIn;
            return singletonCommunication.send(username + separator + Globals.reqType + RequestType.BOOK_REGISTRATION_AUTOMATIC.toString() + separator + book.encode());
        }
    }

    public boolean generateRequestForDataBookRegistrationManual(Book book) {
        if (Globals.usernameLoggedIn == null) {
            Log.d("Processing", "[ASSERT]: no username saved!");
            return false;
        } else {
            String username = Globals.usernameLoggedIn;
            return singletonCommunication.send(username + separator + Globals.reqType + RequestType.BOOK_REGISTRATION_MANUAL.toString() + separator + book.encode());
		}
    }

    @Override
    public boolean generateRequestForDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts, String password, int actionArea) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return singletonCommunication.send(RequestType.SIGN_IN.toString() + separator + name + separator + lastName + separator + df.format(DOB) +
                separator + contacts.toString() + separator + hash(password) + separator + actionArea);
    }

    @Override
    public boolean generateRequestForDataPickUp(String BCID) {
        return singletonCommunication.send(RequestType.PICK_UP.toString() + BCID);
    }

    @Override
    public boolean generateRequestForDataTakenBooks() {
        return singletonCommunication.send(RequestType.TAKEN_BOOKS.toString());
    }

    @Override
    public boolean generateRequestForDataProfileInformations(String username, String password) {
        return singletonCommunication.send(RequestType.PROFILE_INFO.toString() + separator + username + separator + hash(password));
    }

    /*
        When communication call Processing on specific callback, after call this
        DataDispatcherSingleton.ourInstance.notifyLogin();
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
                DataDispatcherSingleton.getInstance().callbackRegistration(res == 1, BCID);
                break;
            case BOOK_RESERVATION:
                k = data.indexOf(":", i+1);
                kk = data.indexOf(":", k+1);
                res = Integer.parseInt(data.substring(k+1,k+2));
                DataDispatcherSingleton.getInstance().callbackReservation(res == 1);
                break;
            case LOGIN:
                int result = data.indexOf(":", i+1);
                int indexNotifications = data.indexOf(";", i + 1);
                String msg = data.substring(result+1, indexNotifications);

                String notifications = data.substring(indexNotifications + 1);
                Globals.notifications = notifications;
                Log.d("TAG", Globals.notifications);

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
                k = data.indexOf(":", i+1);
                kk = data.indexOf(":", k+1);
                res = Integer.parseInt(data.substring(k+1,k+2));
                BCID = data.substring(kk+1);
                DataDispatcherSingleton.getInstance().callbackRegistration(res == 1, BCID);
                break;
            case PROFILE_INFO:
                break;
            case TAKEN_BOOKS:
                break;
            case PICK_UP:
                break;
            case BOOK_SEARCH:

                List<Book> booksFound = new ArrayList<>();
                k = data.indexOf(":", i+1);
                kk = data.indexOf(":", k+1);
                res = Integer.parseInt(data.substring(k+1,k+2));
                Log.d("RES", "" + res);

                String jsonMsg = data.substring(kk + 1);
                Log.d("MSG", "" + jsonMsg);

                try {
                    JSONObject reader = new JSONObject(jsonMsg);
                    Log.d("size", reader.getString("size"));
                    Log.d("elem", "" + reader.getJSONArray("books").get(0));
                    Log.d("len array", "" + reader.getJSONArray("books").length());
                    for(int v = 0; v < reader.getJSONArray("books").length(); v++) {
                        Book b = new Book("" + reader.getJSONArray("books").get(v));
                        Log.d("Book", b.toString());
                        booksFound.add(b);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DataDispatcherSingleton.getInstance().callbackBookSearch(res == 1, booksFound);
                break;
        }
    }
}
