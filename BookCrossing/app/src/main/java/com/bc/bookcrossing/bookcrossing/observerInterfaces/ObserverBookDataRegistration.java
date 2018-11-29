package com.bc.bookcrossing.bookcrossing.observerInterfaces;

import java.util.Observer;

public interface ObserverBookDataRegistration extends Observer{

    public void callbackRegistration(boolean result, String bookCodeID);

}
