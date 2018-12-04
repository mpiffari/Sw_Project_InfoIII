package com.bc.bookcrossing.bookcrossing.GUI.Observer;

public interface ObserverBookDataRegistration extends ObserverForUiInformation {

    public void callbackRegistration(boolean result, String bookCodeID);

}
