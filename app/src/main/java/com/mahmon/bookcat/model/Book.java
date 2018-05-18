package com.mahmon.bookcat.model;

public class Book {

    private String isbn;
    private String title;
    private String author;
    private String coverImageURL;

    // Empty constructor required by firebase rules
    public Book() {
    }

    public Book(String isbn, String title, String author, String coverImageURL) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.coverImageURL = coverImageURL;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverImageURL() {
        return coverImageURL;
    }

    public void setCoverImageURL(String coverImageURL) {
        this.coverImageURL = coverImageURL;
    }
}
