package com.bc.bookcrossing.src.Controllers;

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
   boolean generateRequestForDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts, String password, int actionArea);
   boolean generateRequestForDataPickUp(String BCID);
   boolean generateRequestForDataTakenBooks();
   boolean generateRequestForDataBookRegistrationAuto(Book book);
   boolean generateRequestForDataBookRegistrationManual(Book book);
   boolean generateRequestForDataLogin(User user);
   boolean generateRequestForDataProfileInformations(String username, String password);
   boolean generateRequestForDataBookSearch(String title, String author);
   boolean generateRequestForDataBookReservation(Book bookForReservation);
}
