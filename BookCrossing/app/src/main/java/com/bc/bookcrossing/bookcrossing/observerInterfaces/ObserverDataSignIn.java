package com.bc.bookcrossing.bookcrossing.observerInterfaces;

import com.bc.bookcrossing.bookcrossing.SignInStatus;

import java.util.List;
import java.util.Observer;

public interface ObserverDataSignIn extends ObserverForUiInformation {
    public void callbackSignIn(List<SignInStatus> status);

}
