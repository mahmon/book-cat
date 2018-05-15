package com.mahmon.bookcat.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahmon.bookcat.R;

public class BookFragment extends Fragment {

    // View elements
    private TextView bookTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create fragViewLogin instance
        View fragViewBook = inflater.inflate(R.layout.fragment_book, container, false);

        bookTitle = fragViewBook.findViewById(R.id.book_title);
        bookTitle.setText("Book title will go here");

        // Return the fragment view to the activity
        return fragViewBook;
    }

}
