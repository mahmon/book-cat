package com.mahmon.bookcat.ui;

import android.content.Context;
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
import com.mahmon.bookcat.model.Book;
import com.mahmon.bookcat.model.GoogleApiRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddBookFragment extends Fragment {

    // Context
    Context mContext;
    // Firebase authorisation
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String userUid;
    // Firebase database
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    // View elements
    private EditText bookIsbn;
    private Button btnLookUpBook;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Create fragViewLogin instance
        View fragViewAddBook = inflater
                .inflate(R.layout.fragment_add_book, container, false);
        // Get context
        mContext = getContext();
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
        btnLookUpBook = fragViewAddBook.findViewById(R.id.btn_look_up_book);
        // Attach listener to the button
        btnLookUpBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get email and password from EditText boxes
                final String isbn = bookIsbn.getText().toString().trim();
                // Check a value is entered
                if (TextUtils.isEmpty(isbn)) {
                    // If not prompt user
                    Toast.makeText(mContext,
                            "Enter ISBN", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check only digits are entered
                if (!TextUtils.isDigitsOnly(isbn)) {
                    // If not prompt user
                    Toast.makeText(mContext,
                            "Enter numbers only", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check only digits are entered
                if (isbn.length() != 10 && isbn.length() != 13) {
                    // If not prompt user
                    Toast.makeText(mContext,
                            "ISBNs must be 10 or 13 digits long", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if book already in users books, if not look it up with google
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
                lookUpBook(isbn);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // Look up and create book from GoogleApiRequest
    private void lookUpBook(final String isbn) {
        // Pass isbn to getGoogleBookAsJSONObject from GoogleApiRequest class
        GoogleApiRequest.getInstance(mContext)
                .getGoogleBookAsJSONObject(isbn, new GoogleApiRequest.VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) throws JSONException {

                // TEST : This Works! Complete JSON file prints to toast message
                //String s = result.toString();
                //Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();

                // BELOW HERE IS NOT WORKING, returns blank, I want the title from JSON
                JSONArray books = result.getJSONArray("items");
                JSONObject book = books.getJSONObject(0);
                JSONObject info = book.getJSONObject("volumeInfo");
                String title = info.getString("title");
                String msg = "Title: " + title;
                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();

            }
        });
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
