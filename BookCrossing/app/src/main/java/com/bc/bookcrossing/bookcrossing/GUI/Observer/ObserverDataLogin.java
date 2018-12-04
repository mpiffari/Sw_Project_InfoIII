package com.bc.bookcrossing.bookcrossing.GUI.Observer;

import com.bc.bookcrossing.bookcrossing.LoginInStatus;

import java.util.List;

public interface ObserverDataLogin extends ObserverForUiInformation {
    public void callbackLogin(List<LoginInStatus> status);
}
