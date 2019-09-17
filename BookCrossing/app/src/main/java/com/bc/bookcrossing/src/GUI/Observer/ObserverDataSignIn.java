package com.bc.bookcrossing.src.GUI.Observer;

import com.bc.bookcrossing.src.ClientModels.Enums.SignInStatus;

import java.util.List;

/**
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public interface ObserverDataSignIn extends ObserverForUiInformation {
    void notifySignIn(List<SignInStatus> status);
}
