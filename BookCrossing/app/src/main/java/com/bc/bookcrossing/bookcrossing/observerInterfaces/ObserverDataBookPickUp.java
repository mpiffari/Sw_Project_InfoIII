package com.bc.bookcrossing.bookcrossing.observerInterfaces;

import java.util.Observer;

public interface ObserverDataBookPickUp extends ObserverForUiInformation {

    public void callbackPickUp(short bookStatus);
}
