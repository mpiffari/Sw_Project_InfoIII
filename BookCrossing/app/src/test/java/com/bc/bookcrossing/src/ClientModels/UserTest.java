package com.bc.bookcrossing.src.ClientModels;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void getUsername() {
        User u = new User("A","pwd");
        u.setUsername("B");

        assertEquals(u.getUsername(),"B");
        assertNotEquals(u.getUsername(),"A");
    }

    @Test
    public void setUsername() {
        User u = new User("A","pwd");
        u.setUsername("B");

        assertEquals(u.getUsername(),"B");
        assertNotEquals(u.getUsername(),"A");
    }

    @Test
    public void getFirstName1() {
        User u = new User("A","pwd");

        assertEquals(u.getFirstName(),null);
    }

    @Test
    public void getFirstName2() {
        User u = new User("A","pwd");
        u.setFirstName("aa");

        assertEquals(u.getFirstName(),"aa");
        assertNotEquals(u.getFirstName(),"A");
    }

    @Test
    public void getLastName1() {
        User u = new User("A","pwd");

        assertEquals(u.getFirstName(),null);
        assertNotEquals(u.getFirstName(),"A");
    }

    @Test
    public void getLastName2() {
        User u = new User("A","pwd");
        u.setLastName("aa");

        assertEquals(u.getLastName(),"aa");
        assertNotEquals(u.getLastName(),"A");
    }

    @Test
    public void getDateOfBirth1() {
        User u = new User("A","pwd");
        assertEquals(u.getDateOfBirth(),null);
        assertEquals(u.getLastName(),null);
    }

    @Test
    public void getDateOfBirth2() {
        User u = new User("A","pwd");
        u.setDateOfBirth("1111");

        assertEquals(u.getDateOfBirth(),"1111");
        assertNotEquals(u.getDateOfBirth(),"1_1_");
    }

    @Test
    public void setPassword1() {
        User u = new User("A","pwd");

        assertEquals(u.getPassword(),"pwd");
        assertNotEquals(u.getPassword(),"1");
    }

    @Test
    public void setPassword() {
        User u = new User("a", "aa", "aaa", "1", null, 1.5,1.5, 1);

        assertEquals(u.getPassword(),null);
        assertNotEquals(u.getPassword(),"1");
    }

    @Test
    public void getLatitude() {
        User u1 = new User("a", "aa", "aaa", "1", null, 1.5,1.5, 1);
        User u2 = new User("a","Pwd");

        assertNotEquals(u1.getLatitude(),10);
        assertEquals(u2.getLatitude(),null);
    }

    @Test
    public void getLongitude() {
        User u1 = new User("a", "aa", "aaa", "1", null, 1.5,1.5, 1);
        User u2 = new User("a","Pwd");

        assertNotEquals(u1.getLongitude(),10);
        assertEquals(u2.getLongitude(),null);
    }

    @Test
    public void setLatitude() {
        User u = new User("a","Pwd");
        u.setLatitude(5.0);
        assertNotEquals(u.getLatitude(),10);
    }

    @Test
    public void setLongitude() {
        User u = new User("a","Pwd");
        u.setLongitude(5.0);
        assertNotEquals(u.getLongitude(),10);
    }

    @Test
    public void getAction1() {
        User u1 = new User("a", "aa", "aaa", "1", null, 1.5,1.5, 1);
        User u2 = new User("a","Pwd");

        assertEquals(u1.getAction().intValue(),1);
        assertNotEquals(u1.getAction().intValue(),10);
        assertEquals(u2.getAction(),null);
    }

    @Test
    public void getAction2() {
        User u = new User("a","Pwd");
        assertEquals(u.getAction(),null);

        u.setAction(6);
        assertEquals(u.getAction().intValue(),6);
        assertNotEquals(u.getAction().intValue(),10);
    }
}