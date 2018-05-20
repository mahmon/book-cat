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

import com.google.android.gms.tasks.OnSuccessListener;
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

import static com.mahmon.bookcat.Constants.BOOK_NODE;

public class AddBookFragment extends Fragment {

    // Context
    private Context mContext;
    // Firebase authorisation
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String userUid;
    // Firebase database
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    // View elements
    private EditText bookIsbn;
    private Button btnAddBook;

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
        bookIsbn = fragViewAddBook.findViewById(R.id.book_title);
        btnAddBook = fragViewAddBook.findViewById(R.id.btn_add_book);
        // Attach listener to the button
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get isbn from EditText boxes
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
                // Check only 10 or 13 digits are entered
                if (isbn.length() != 10 && isbn.length() != 13) {
                    // If not prompt user
                    Toast.makeText(mContext,
                            "ISBNs must be 10 or 13 digits long", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if book already in users library, if not look it up on google books api
                checkIfBookInUsersLibrary(isbn);
            }
        });
        return fragViewAddBook;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // Query user library to check if book already entered
    public void checkIfBookInUsersLibrary(final String isbn) {
        // Create query on Book Node
        Query query = mDatabaseRef
                .child(userUid)
                .child(Constants.BOOK_NODE)
                .orderByValue();
        // Attach listener to the query
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ( dataSnapshot.hasChild(isbn) ) {
                    String error = isbn + " is already in your library";
                    Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
                    return;
                }
                // Else run the lookUpBook method
                lookUpBook(isbn);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // Look up and create book from GoogleApiRequest
    private void lookUpBook(final String isbn) {
        // Pass isbn to getGoogleBookAsJSONObject method from GoogleApiRequest class
        GoogleApiRequest.getInstance(mContext)
                .getGoogleBookAsJSONObject(isbn, new GoogleApiRequest.VolleyCallback() {
            @Override
            // JSONObject returned - a googleBook JSON file
            public void onSuccessResponse(JSONObject googleBookJSON) throws JSONException {
                // Create links to the data in JSONObject
                JSONArray books = googleBookJSON.getJSONArray("items");
                JSONObject book = books.getJSONObject(0);
                JSONObject bookInfo = book.getJSONObject("volumeInfo");
                // Store the data in methods as strings
                String title = bookInfo.getString("title");
                String author = bookInfo.getJSONArray("authors").getString(0);
                String coverImageURL = bookInfo.getJSONObject("imageLinks").getString("thumbnail");
                // Call method to save book to user library
                saveBookToUserLibrary(isbn, title, author, coverImageURL);
            }


        });
    }

    // Method to save book to user library
    private void saveBookToUserLibrary(final String isbn,
                                       String title,
                                       String author,
                                       String coverImageURL) {
        // Create new book in user library with string data
        Book userBook = new Book(isbn, title, author, coverImageURL);
        // Save the book to the database under users unique id
        mDatabaseRef.child(userUid).child(BOOK_NODE).child(isbn)
                .setValue(userBook)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        gotoBook(isbn, userUid);
                    }
                });
        Toast.makeText(mContext, "Book saved to your library", Toast.LENGTH_SHORT).show();
    }

    // Method to create new fragment and replace in the fragment container
    private void gotoBook(String isbn, String userUid) {
        // Create new BookFragment
        BookFragment bookFragment = new BookFragment();
        // Create new data bundle
        Bundle bookData = new Bundle();
        // Store the isbn value and userUid in the data bundle
        bookData.putString(Constants.ISBN_KEY, isbn);
        bookData.putString(Constants.USER, userUid);
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
