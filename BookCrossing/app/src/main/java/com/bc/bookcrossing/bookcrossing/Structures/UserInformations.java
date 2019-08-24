package com.bc.bookcrossing.bookcrossing.Structures;

import java.util.Date;

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
