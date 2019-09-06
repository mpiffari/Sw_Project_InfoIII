package com.bc.bookcrossing.src.View.Observer;

/**
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public interface ObserverDataBookPickUp extends ObserverForUiInformation {
    void notifyPickUp(short bookStatus);
}
