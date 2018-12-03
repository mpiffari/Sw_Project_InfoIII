package com.bc.bookcrossing.bookcrossing.Comunication;

import com.bc.bookcrossing.bookcrossing.BookInfo;
import com.bc.bookcrossing.bookcrossing.LoginInStatus;
import com.bc.bookcrossing.bookcrossing.SignInStatus;
import com.bc.bookcrossing.bookcrossing.UserInformations;

import java.util.ArrayList;
import java.util.List;

public interface ReceiveData {
    public void callbackRegistration(boolean result, String bookCodeID);
    public void callbackPickUp(short bookStatus);
    public void callbackBookTaken(ArrayList<BookInfo> bookInformations);
    public void callbackLogin(List<LoginInStatus> status);
    public void callbackProfile(UserInformations userInformations);
    public void callbackSignIn(List<SignInStatus> status);
}
