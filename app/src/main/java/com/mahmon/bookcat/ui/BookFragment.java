package com.mahmon.bookcat.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahmon.bookcat.Constants;
import com.mahmon.bookcat.R;

public class BookFragment extends Fragment {

    // View elements
    private TextView bookTitle;
    private String isbn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create fragViewLogin instance
        View fragViewBook = inflater.inflate(R.layout.fragment_book, container, false);
        // Get the passed arguments
        Bundle bookData = getArguments();
        isbn = bookData.getString(Constants.ISBN_KEY);
        // Link to view items
        bookTitle = fragViewBook.findViewById(R.id.book_title);
        bookTitle.setText(isbn);
        // Return the fragment view to the activity
        return fragViewBook;
    }

}
