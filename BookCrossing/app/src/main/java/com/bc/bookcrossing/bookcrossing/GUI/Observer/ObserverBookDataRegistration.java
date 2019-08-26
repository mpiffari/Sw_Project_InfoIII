package com.bc.bookcrossing.bookcrossing.GUI.Observer;

public interface ObserverBookDataRegistration extends ObserverForUiInformation {
    void callbackRegistration(boolean result, String bookCodeID);
}
