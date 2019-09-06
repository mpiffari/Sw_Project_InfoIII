package com.bc.bookcrossing.src;

/**
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public class Globals {
    //TODO: make a server request to retrieve BookTypes available
    public final static String[] types = {"","Action","Adventure","Thriller","Fantasy","Horror","Fairy story","Other..."};
    public final static String reqType = "requestType:";

    public static boolean isLoggedIn = false;
    public static String usernameLoggedIn = null;
    public static String notifications = "";
}
