package com.bc.bookcrossing.bookcrossing.Structures;

import org.junit.Test;

import static org.junit.Assert.*;

public class BookTest {

    @Test
    public void testSetISBN() {
        Book b = new Book();
        b.setISBN("A");

        assertEquals(b.getISBN(),"A");
        assertNotEquals(b.getISBN(),"dddd");
    }

    @Test
    public void testGetISBN() {
        Book b = new Book();
        b.setISBN("A");

        assertEquals(b.getISBN(),"A");
        assertNotEquals(b.getISBN(),"dddd");
    }

    @Test
    public void testSetBCID() {
        Book b = new Book();
        b.setBCID("A");

        assertEquals(b.getBCID(),"A");
        assertNotEquals(b.getBCID(),"dddd");
    }

    @Test
    public void testGetBCID() {
        Book b = new Book();
        b.setBCID("A");

        assertEquals(b.getBCID(),"A");
        assertNotEquals(b.getBCID(),"dddd");
    }

    @Test
    public void testSetAuthor() {
        Book b = new Book();
        b.setAuthor("A");

        assertEquals(b.getAuthor(),"A");
        assertNotEquals(b.getAuthor(),"dddd");
    }

    @Test
    public void testGetAuthor() {
        Book b = new Book();
        b.setAuthor("A");

        assertEquals(b.getAuthor(),"A");
        assertNotEquals(b.getAuthor(),"dddd");
    }

    @Test
    public void testSetTitle() {
        Book b = new Book();
        b.setTitle("A");

        assertEquals(b.getTitle(),"A");
        assertNotEquals(b.getTitle(),"dddd");
    }

    @Test
    public void testGetTitle() {
        Book b = new Book();
        b.setTitle("A");

        assertEquals(b.getTitle(),"A");
        assertNotEquals(b.getTitle(),"dddd");
    }

    @Test
    public void testSetYearOfPubblication() {
        Book b = new Book();
        b.setYearOfPubblication(1);

        assertEquals(b.getYearOfPubblication(),1);
        assertNotEquals(b.getYearOfPubblication(),2);
    }

    @Test
    public void testGetYearOfPubblication() {
        Book b = new Book();
        b.setYearOfPubblication(1);

        assertEquals(b.getYearOfPubblication(),1);
        assertNotEquals(b.getYearOfPubblication(),2);
    }

    @Test
    public void testSetEditionNumber() {
        Book b = new Book();
        b.setEditionNumber(1);

        assertEquals(b.getEditionNumber(),1);
        assertNotEquals(b.getEditionNumber(),2);
    }

    @Test
    public void testGetEditionNumber() {
        Book b = new Book();
        b.setEditionNumber(1);

        assertEquals(b.getEditionNumber(),1);
        assertNotEquals(b.getEditionNumber(),2);
    }

    @Test
    public void testSetType() {
        Book b = new Book();
        b.setType("1");

        assertEquals(b.getType(),"1");
        assertNotEquals(b.getType(),"2");
    }

    @Test
    public void testGetType() {
        Book b = new Book();
        b.setType("1");

        assertEquals(b.getType(),"1");
        assertNotEquals(b.getType(),"2");
    }

    @Test
    public void testEncode() {
        Book b = new Book();
        b.setTitle("A");

        assertEquals(b.encode(),"TITLE:A;AUTHOR:null;YEAR:0;EDITION:0;TYPE:null;USER:null;ISBN:null;STATE:0;BCID:");
        assertNotEquals(b.encode(),"TITLE:A;AUTHOR:;YEAR:45;EDITION: ;TYPE:;USER:;ISBN:;STATE:;BCID:;");
    }
}