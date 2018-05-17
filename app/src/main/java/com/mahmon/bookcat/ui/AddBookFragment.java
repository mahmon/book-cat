package com.mahmon.bookcat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mahmon.bookcat.Constants;
import com.mahmon.bookcat.R;

public class AddBookFragment extends Fragment {

    // Nodes for database access
    private static final String USERSNODE = "Users";
    private static final String BOOKNODE = "Books";
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Create fragViewLogin instance
        View fragViewAddBook = inflater
                .inflate(R.layout.fragment_add_book, container, false);
        // Initialise Firebase authorisation instance
        mAuth = FirebaseAuth.getInstance();
        // Get current user UID
        currentUser = mAuth.getCurrentUser();
        userUid = currentUser.getUid();
        // Get Firebase instance and database ref
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference().child(Constants.USERS_NODE);
        // Link to view elements
        bookIsbn = fragViewAddBook.findViewById(R.id.book_isbn);
        btnSaveBook = fragViewAddBook.findViewById(R.id.btn_save_book);
        // Attach listener to the button
        btnSaveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get email and password from EditText boxes
                final String isbn = bookIsbn.getText().toString().trim();
                // Check a value is entered
                if (TextUtils.isEmpty(isbn)) {
                    // If not prompt user
                    Toast.makeText(getContext(),
                            "Enter ISBN", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check only digits are entered
                if (!TextUtils.isDigitsOnly(isbn)) {
                    // If not prompt user
                    Toast.makeText(getContext(),
                            "Enter numbers only", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check only digits are entered
                if (isbn.length() != 10 && isbn.length() != 13) {
                    // If not prompt user
                    Toast.makeText(getContext(),
                            "ISBNs must be 10 or 13 digits long", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if book exists already
                checkIfBookExists(isbn);
            }
        });
        return fragViewAddBook;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // Query database to check if book exists
    public void checkIfBookExists(final String isbn) {
        // Create query on Book Node
        Query query = mDatabaseRef.child(userUid).child(Constants.BOOK_NODE).orderByValue();
        // Attach listener to query
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // Called first time query is run
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterate through the children of the BookNode dataSnapShot
                for (DataSnapshot innerSnap: dataSnapshot.getChildren()) {
                    // Check for existing ISBN value matching the input value
                    if (innerSnap.child(Constants.ISBN_KEY).getValue(String.class).equals(isbn)) {
                        String errorMsg = isbn + " is already in your collection";
                        // Prompt the user that they already have this isbn in their collection
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                // Else run the saveBook method
                saveBook(isbn);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // Save ISBN number to users database
    private void saveBook(String isbn) {
        // Push the ISBN to the database ('push' generates a unique key for each book)
        mDatabaseRef.child(userUid).child(BOOKNODE).push().child("ISBN").setValue(isbn);
        // Write confirmation message
        Toast.makeText(getContext(), "Book saved", Toast.LENGTH_LONG).show();
        // Goto book page
        gotoBook(isbn);
    }

    // Method to create new fragment and replace in the fragment container
    public void gotoBook(String isbn) {
        // Create new BookFragment
        BookFragment bookFragment = new BookFragment();
        // Create new data bundle
        Bundle bookData = new Bundle();
        // Store the isbn value in the data bundle
        bookData.putString(Constants.ISBN_KEY, isbn);
        // Add the bundle to the fragment
        bookFragment.setArguments(bookData);
        // Create fragment transaction object
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        // Put the bookFragment into the fragment_container
        fragmentTransaction.replace(R.id.fragment_container, bookFragment);
        // Don't add the fragment to the back stack (avoids issues with back button)
        fragmentTransaction.addToBackStack(null);
        // Commit the transaction
        fragmentTransaction.commit();
    }
}
