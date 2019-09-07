package com.bc.bookcrossing.src.ClientModels;

/**
 *
 * Eccezione custom utilizzata in caso in cui lo scan del codice ISBN non ritorni un risultato.
 * Ringraziamento a: RajinderPal
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public class NoScanResultException extends Exception {
    public NoScanResultException(String msg) { super(msg); }
}