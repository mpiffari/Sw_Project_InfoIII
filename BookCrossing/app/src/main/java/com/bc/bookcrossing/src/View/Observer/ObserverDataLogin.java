package com.bc.bookcrossing.src.View.Observer;

import android.support.annotation.Nullable;
import com.bc.bookcrossing.src.UnitTest.Enums.LoginStatus;

/**
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public interface ObserverDataLogin extends ObserverForUiInformation {
    void notifyLogin(final boolean result, @Nullable final LoginStatus s);
}
