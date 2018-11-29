package com.bc.bookcrossing.bookcrossing.observerInterfaces;

import java.util.Observer;

public interface ObserverDataBookPickUp extends Observer {

    public void callbackPickUp(short bookStatus);
}
