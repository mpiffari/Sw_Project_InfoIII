package com.bc.bookcrossing.src.ClientModels;

//import dataManager.UserData;

import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;

/**
 *
 * Utente che può partecipare alla community di sharing del book crossing.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public class User {

    private @NonNull String username;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String password;
    private Double latitude;
    private Double longitude;
    private Integer action;

    /**
     *
     * Init completo dell'utente, con tutte le informazioni che lo caratterizzano.
     *
     * @param username
     * @param firstName
     * @param lastName
     * @param dateOfBirth
     * @param password
     * @param latitude
     * @param longitude
     * @param action
     */
    public User(String username, String firstName, String lastName, String dateOfBirth, String password, Double latitude,
                Double longitude, Integer action) {

        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;
        this.action = action;
    }

    /**
     * Init semplificato dell'utente: essendo che sul database ogni user ha come chiave l'username,
     * esso è sufficiente per poter accedere a tute le informazioni, tramite una query.
     * @param username
     */
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    /**
     * Metodo toString utilizzato per stampe di debug
     * @return Debug description di questo utente.
     */
    @Override
    public String toString() {
        return  "USER:" + username + ";" +
                "PASSWORD:" + password;
    }
}

