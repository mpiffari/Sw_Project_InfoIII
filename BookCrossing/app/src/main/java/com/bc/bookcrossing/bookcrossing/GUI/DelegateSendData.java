package com.bc.bookcrossing.bookcrossing.GUI;

import com.bc.bookcrossing.bookcrossing.GUI.Observer.ObserverForUiInformation;
import com.bc.bookcrossing.bookcrossing.Structures.Book;

import java.util.Date;

public interface DelegateSendData {

   boolean sendDataLogin(String username, String password);
   boolean sendDataSignIn(String name, String lastName, String username, Date DOB, String[] contacts, String password, int actionArea); //TODO : LAT e LONG
   boolean sendDataPickUp(String BCID);
   boolean sendDataTakenBooks();
   boolean sendDataBookRegistrationAuto(String title, String author, String yearOfPubb, String edition, String bookTypeDesc, String ISBN);
   boolean sendDataBookRegistrationManual(String title, String author, String yearOfPubb, String edition, String bookTypeDesc);
   boolean sendDataProfileInformations(String username, String password);
   boolean sendDataBookSearch(String title, String author);
   boolean sendDataBookReservation(Book bookForReservation);

   void register(ObserverForUiInformation observerForUiInformation);
   boolean unRegister(ObserverForUiInformation observerForUiInformation);


}
