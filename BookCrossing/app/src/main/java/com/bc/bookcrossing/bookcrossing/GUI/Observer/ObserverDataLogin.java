package com.bc.bookcrossing.bookcrossing.GUI.Observer;

import android.support.annotation.Nullable;
import com.bc.bookcrossing.bookcrossing.Structures.LoginStatus;

public interface ObserverDataLogin extends ObserverForUiInformation {
    void callbackLogin(final boolean result,@Nullable LoginStatus s);
}
