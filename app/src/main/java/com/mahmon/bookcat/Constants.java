package com.mahmon.bookcat;

// Class used to provide app wide constants
public class Constants {

    // String to store google api base string
    public static final String GOOGLE_URL_BASE =
            "https://www.googleapis.com/books/v1/volumes?q=isbn:";
    // Nodes for Firebase database access
    public static final String USERS_NODE = "Users";
    public static final String USER = "User";
    public static final String NAME_KEY = "Name";
    public static final String BOOK_NODE = "Books";
    // Key value used to bundle ISBN value between classes
    public static final String ISBN_KEY = "ISBN";
    // Key values used in Google Books API
    public static final String ITEMS = "items";
    public static final String VOLUME_INFO = "volumeInfo";
    public static final String TITLE = "title";
    public static final String AUTHORS = "authors";
    public static final String IMAGE_LINKS = "imageLinks";
    public static final String THUMBNAIL = "thumbnail";

}
