package com.bc.bookcrossing.src.ClientModels;

import java.util.Date;

/**
 *
 * Next iteration.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public class UserInformations {

    private String name;
    private String lastName;
    private Date DOB;
    private String[] contacts;

    public UserInformations(String name, String lastName, Date DOB, String[] contacts) {
        this.name = name;
        this.lastName = lastName;
        this.DOB = DOB;
        this.contacts = contacts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public String[] getContacts() {
        return contacts;
    }

    public void setContacts(String[] contacts) {
        this.contacts = contacts;
    }

}
