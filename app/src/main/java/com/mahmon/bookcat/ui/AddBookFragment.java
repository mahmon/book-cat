package com.mahmon.bookcat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mahmon.bookcat.R;

public class AddBookFragment extends Fragment {

    // View elements
    private EditText bookIsbn;
    private EditText bookTitle;
    private EditText bookAuthor;
    private Button btnSaveBook;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create fragViewLogin instance
        View fragViewAddBook = inflater.inflate(R.layout.fragment_add_book, container, false);
        // Link to view elements
        bookIsbn = fragViewAddBook.findViewById(R.id.book_isbn);
        bookTitle = fragViewAddBook.findViewById(R.id.book_title);
        bookAuthor = fragViewAddBook.findViewById(R.id.book_author);
        btnSaveBook = fragViewAddBook.findViewById(R.id.btn_save_book);

        // Attach listener to the button
        btnSaveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get email and password from EditText boxes
                final String isbn = bookIsbn.getText().toString().trim();
                final String title = bookTitle.getText().toString().trim();
                final String author = bookAuthor.getText().toString().trim();
                // Check isbn entered
                if (TextUtils.isEmpty(isbn)) {
                    // If not prompt user
                    Toast.makeText(getContext(), "Enter ISBN", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check title entered
                if (TextUtils.isEmpty(title)) {
                    // If not prompt user
                    Toast.makeText(getContext(), "Enter title", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check author entered
                if (TextUtils.isEmpty(author)) {
                    // If not prompt user
                    Toast.makeText(getContext(), "Enter author", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Call login method when email and password entered
                saveBook();
            }
        });
        return fragViewAddBook;
    }

    private void saveBook() {
        // TODO
        Toast.makeText(getContext(), "SAVE BOOK METHOD", Toast.LENGTH_LONG).show();
    }
}
