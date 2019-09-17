package com.bc.bookcrossing.src.requestManager;

import com.bc.bookcrossing.src.ClientModels.Book;
import com.bc.bookcrossing.src.ClientModels.User;
import java.util.Date;

/**
 *
 * Interfaccia utilizzata per spedire richieste dall'interfaccia grafica verso il server.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public interface GenerateRequests {

   // ===================== ITERAZIONE 1 ===================================
   boolean generateRequestForDataBookSearch(String title, String author);
   boolean generateRequestForDataBookRegistrationManual(Book book);

   // ===================== ITERAZIONE 2 ===================================
   boolean generateRequestForDataBookRegistrationAuto(Book book);
   boolean generateRequestForDataBookReservation(Book bookForReservation);
   boolean generateRequestForDataLogin(User user);

   // ===================== PROSSIMA ITERAZIONE ===================================
   boolean generateRequestForDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts, String password, int actionArea);
   boolean generateRequestForDataPickUp(String BCID);
   boolean generateRequestForDataTakenBooks();
   boolean generateRequestForDataProfileInformations(String username, String password);
}
