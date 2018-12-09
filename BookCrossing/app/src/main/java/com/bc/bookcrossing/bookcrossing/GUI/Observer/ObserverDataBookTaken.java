package com.bc.bookcrossing.bookcrossing.GUI.Observer;

import com.bc.bookcrossing.bookcrossing.BookInfo;

import java.util.ArrayList;

public interface ObserverDataBookTaken extends ObserverForUiInformation {
    public void callbackBookTaken(ArrayList<BookInfo> bookInformations);
}