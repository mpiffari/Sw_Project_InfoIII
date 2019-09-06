package com.bc.bookcrossing.src.Controllers;

import android.support.annotation.Nullable;

import com.bc.bookcrossing.src.UnitTest.Book;
import com.bc.bookcrossing.src.UnitTest.BookInfo;
import com.bc.bookcrossing.src.UnitTest.Enums.LoginStatus;
import com.bc.bookcrossing.src.UnitTest.Enums.SignInStatus;
import com.bc.bookcrossing.src.UnitTest.UserInformations;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public interface ReceiveData {
    void callbackRegistration(boolean result, String bookCodeID);
    void callbackPickUp(short bookStatus);
    void callbackBookTaken(ArrayList<BookInfo> bookInformations);
    void callbackLogin(final boolean result,@Nullable LoginStatus s);
    void callbackProfile(UserInformations userInformations);
    void callbackSignIn(List<SignInStatus> status);
    void callbackBookSearch(boolean result, List<Book> booksFound);
    void callbackReservation(boolean result);
}
