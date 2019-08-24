package com.bc.bookcrossing.bookcrossing.GUI.Observer;

import com.bc.bookcrossing.bookcrossing.Structures.BookInfo;
import java.util.ArrayList;

public interface ObserverDataBookTaken extends ObserverForUiInformation {
    void callbackBookTaken(ArrayList<BookInfo> bookInformations);
}
