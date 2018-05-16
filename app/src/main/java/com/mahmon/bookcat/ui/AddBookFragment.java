package com.mahmon.bookcat.ui;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mahmon.bookcat.R;

public class AddBookFragment extends Fragment {

    private static final String bookNode = "Books";

    // Firebase authorisation
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String userUid;
    // Firebase database
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    // View elements
    private EditText bookIsbn;
    private Button btnSaveBook;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create fragViewLogin instance
        View fragViewAddBook = inflater.inflate(R.layout.fragment_add_book, container, false);
        // Initialise Firebase authorisation instance
        mAuth = FirebaseAuth.getInstance();
        // Get current user UID
        currentUser = mAuth.getCurrentUser();
        userUid = currentUser.getUid();
        // Get Firebase instance and database ref
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference(userUid);
        // Link to view elements
        bookIsbn = fragViewAddBook.findViewById(R.id.book_isbn);
        btnSaveBook = fragViewAddBook.findViewById(R.id.btn_save_book);
        // Attach listener to the button
        btnSaveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get email and password from EditText boxes
                final String isbn = bookIsbn.getText().toString().trim();
                // Check isbn entered
                if (TextUtils.isEmpty(isbn)) {
                    // If not prompt user
                    Toast.makeText(getContext(), "Enter ISBN", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Call login method when email and password entered
                saveBook(isbn);
            }
        });
        return fragViewAddBook;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void saveBook(String isbn) {
        Toast.makeText(getContext(), userUid, Toast.LENGTH_LONG).show();
        // Write a message to the database
        mDatabaseRef.child(bookNode).setValue(isbn);
    }

}
