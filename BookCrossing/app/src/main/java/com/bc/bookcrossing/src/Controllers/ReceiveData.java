package com.bc.bookcrossing.src.Controllers;

import android.support.annotation.Nullable;

import com.bc.bookcrossing.src.ClientModels.Book;
import com.bc.bookcrossing.src.ClientModels.BookInfo;
import com.bc.bookcrossing.src.ClientModels.Enums.LoginStatus;
import com.bc.bookcrossing.src.ClientModels.Enums.SignInStatus;
import com.bc.bookcrossing.src.ClientModels.UserInformations;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Primo livello di callback, chiamato nel momento in cui sono presenti dati dal server.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public interface ReceiveData {
    void callbackRegistration(boolean result, String bookCodeID);
    void callbackLogin(final boolean result,@Nullable LoginStatus s);
    void callbackBookSearch(boolean result, List<Book> booksFound);
    void callbackReservation(boolean result);

    void callbackPickUp(short bookStatus);
    void callbackBookTaken(ArrayList<BookInfo> bookInformations);
    void callbackProfile(UserInformations userInformations);
    void callbackSignIn(List<SignInStatus> status);
}
