package com.bc.bookcrossing.src.GUI.DataDispatcher;

import com.bc.bookcrossing.src.GUI.Observer.ObserverForUiInformation;
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
   void register(ObserverForUiInformation observerForUiInformation);
   boolean unRegister(ObserverForUiInformation observerForUiInformation);

   // ===================== ITERAZIONE 1 ===================================
   boolean sendDataBookRegistrationManual(String title, String author, String yearOfPubb, String edition,
                                          String bookTypeDesc);
   boolean sendDataBookSearch(String title, String author);

   // ===================== ITERAZIONE 2 ===================================
   boolean sendDataLogin(String username, String password);
   boolean sendDataBookRegistrationAuto(String title, String author, String yearOfPubb, String edition,
                                        String bookTypeDesc, String ISBN);
   boolean sendDataBookReservation(Book bookForReservation);

   // ===================== PROSSIMA ITERAZIONE ===================================
   boolean sendDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts,
                          String password, int actionArea);
   boolean sendDataProfileInformations(String username, String password);
   boolean sendDataPickUp(String BCID);
   boolean sendDataTakenBooks();
}
