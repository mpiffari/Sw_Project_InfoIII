package com.bc.bookcrossing.bookcrossing.RequestManager;

import android.support.annotation.Nullable;

import com.bc.bookcrossing.bookcrossing.Structures.BookInfo;
import com.bc.bookcrossing.bookcrossing.Structures.LoginStatus;
import com.bc.bookcrossing.bookcrossing.Structures.SignInStatus;
import com.bc.bookcrossing.bookcrossing.Structures.UserInformations;

import java.util.ArrayList;
import java.util.List;

public interface ReceiveData {
    void callbackRegistration(boolean result, String bookCodeID);
    void callbackPickUp(short bookStatus);
    void callbackBookTaken(ArrayList<BookInfo> bookInformations);
    void callbackLogin(final boolean result,@Nullable LoginStatus s);
    void callbackProfile(UserInformations userInformations);
    void callbackSignIn(List<SignInStatus> status);
}
