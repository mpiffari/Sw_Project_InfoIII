package com.bc.bookcrossing.bookcrossing.GUI.Observer;

public interface ObserverDataBookRegistration extends ObserverForUiInformation {
    void callbackRegistration(boolean result, String bookCodeID);
}
