package com.bc.bookcrossing.src.View.Observer;

/**
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public interface ObserverDataBookReservation extends ObserverForUiInformation {
    public void notifyReservation(boolean result);
}
