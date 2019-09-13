package com.bc.bookcrossing.src.View.Observer;

import com.bc.bookcrossing.src.ClientModels.UserInformations;

/**
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public interface ObserverDataProfile extends ObserverForUiInformation {
    void notifyProfile(UserInformations userInformations);
}
