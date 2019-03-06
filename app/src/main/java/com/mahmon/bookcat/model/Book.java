package com.mahmon.bookcat.model;

// Class used to instantiate book objects
public class Book {

    // Variables for storing book details
    private String isbn;
    private String title;
    private String author;
    private String coverImageURL;

    // Empty constructor required by Firebase rules
    public Book() {
    }

    // Constructor used to instantiate book with all details
    public Book(String isbn, String title, String author, String coverImageURL) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.coverImageURL = coverImageURL;
    }

    // Getters and Setters for all the book variables
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
    // No CoverImageURL setter required as URL supplied by Google Books API

}
