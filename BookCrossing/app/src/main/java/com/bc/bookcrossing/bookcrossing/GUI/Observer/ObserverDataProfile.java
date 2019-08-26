package com.bc.bookcrossing.bookcrossing.GUI.Observer;

import com.bc.bookcrossing.bookcrossing.Structures.UserInformations;

public interface ObserverDataProfile extends ObserverForUiInformation {
    public void callbackProfile(UserInformations userInformations);
}
