package com.bc.bookcrossing.bookcrossing.GUI.Observer;

import com.bc.bookcrossing.bookcrossing.Structures.Book;
import java.util.List;

public interface ObserverDataBookResearch extends ObserverForUiInformation {
    void callbackBookSearch(boolean result, List<Book> books);
}
