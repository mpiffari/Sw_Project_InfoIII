package com.bc.bookcrossing.bookcrossing;

import com.bc.bookcrossing.bookcrossing.Structures.Book;
import com.bc.bookcrossing.bookcrossing.Structures.LoginStatus;

import java.util.ArrayList;

public class Globals {
    //TODO: make a server request to retrieve BookTypes available
    public final static String[] types = {"","Action","Adventure","Thriller","Fantasy","Horror","Fairy story","Other..."};
    public final static String reqType = "requestType:";

    public static boolean isLoggedIn = false;
    public static String usernameLoggedIn = null;
    public static String notifications = "";
}
