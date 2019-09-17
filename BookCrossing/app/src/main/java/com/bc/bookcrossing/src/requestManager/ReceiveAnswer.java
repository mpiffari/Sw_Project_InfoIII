package com.bc.bookcrossing.src.requestManager;

/**
 *
 * Interfaccia implementata dal componente di Processing e utilizzata da Communication.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public interface ReceiveAnswer {
    void processAnswer(String data);
}
