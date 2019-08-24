package com.bc.bookcrossing.bookcrossing.Structures;

public class BookInfo {

    private String title;
    private String author;
    private String dataPickUp;

    public BookInfo(String title, String author, String dataPickUp) {
        this.title = title;
        this.author = author;
        this.dataPickUp = dataPickUp;
    }

    public String getDataPickUp() {
        return dataPickUp;
    }

    public void setDataPickUp(String dataPickUp) {
        this.dataPickUp = dataPickUp;
    }

    public String getAuthor() {

        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
