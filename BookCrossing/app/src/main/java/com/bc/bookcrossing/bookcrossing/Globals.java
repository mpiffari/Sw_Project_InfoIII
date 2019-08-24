package com.bc.bookcrossing.bookcrossing;

import com.bc.bookcrossing.bookcrossing.Structures.LoginStatus;

public class Globals {
    //TODO: make a server request to retrieve BookTypes available
    public final static String[] BookTypes = {"","Action","Adventure","Thriller","Fantasy","Horror","Fairy story","Other..."};
    public final static String reqType = "requestType:";
    public static String ISBN = "";

    public static boolean isLoggedIn = false;
}
