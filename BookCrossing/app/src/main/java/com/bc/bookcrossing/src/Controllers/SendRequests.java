package com.bc.bookcrossing.src.Controllers;

/**
 *
 * Interfaccia che offre un metodo per inviare una richeista verso il server.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public interface SendRequests {
    boolean send(String data);
}
