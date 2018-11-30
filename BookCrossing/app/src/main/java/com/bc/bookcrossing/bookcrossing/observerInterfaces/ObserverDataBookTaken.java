package com.bc.bookcrossing.bookcrossing.observerInterfaces;

import com.bc.bookcrossing.bookcrossing.BookInfo;

import java.util.ArrayList;
import java.util.Observer;

public interface ObserverDataBookTaken extends ObserverForUiInformation {
    public void callbackBookTaken(ArrayList<BookInfo> bookInformations);
}
