package com.bc.bookcrossing.src.View.Delegate;

import com.bc.bookcrossing.src.View.Observer.ObserverForUiInformation;
import com.bc.bookcrossing.src.ClientModels.Book;

import java.util.Date;

/**
 *
 * Classe che offre interfaccie in ingresso e in uscita per gestire la comunicazione
 * da/verso i fragment/server.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public interface DelegateSendData {

   boolean sendDataLogin(String username, String password);

   boolean sendDataBookRegistrationAuto(String title, String author, String yearOfPubb, String edition,
                                        String bookTypeDesc, String ISBN);
   boolean sendDataBookRegistrationManual(String title, String author, String yearOfPubb, String edition,
                                          String bookTypeDesc);
   boolean sendDataBookSearch(String title, String author);
   boolean sendDataBookReservation(Book bookForReservation);

   boolean sendDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts,
                          String password, int actionArea);
   boolean sendDataProfileInformations(String username, String password);
   boolean sendDataPickUp(String BCID);
   boolean sendDataTakenBooks();

   void register(ObserverForUiInformation observerForUiInformation);
   boolean unRegister(ObserverForUiInformation observerForUiInformation);
}
