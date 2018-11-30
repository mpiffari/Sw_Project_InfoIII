package com.bc.bookcrossing.bookcrossing.observerInterfaces;

import com.bc.bookcrossing.bookcrossing.LoginInStatus;

import java.util.List;
import java.util.Observer;

public interface ObserverDataLogin extends ObserverForUiInformation {
    public void callbackLogin(List<LoginInStatus> status);
}
