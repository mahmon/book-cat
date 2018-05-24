package com.mahmon.bookcat.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmon.bookcat.R;
import com.mahmon.bookcat.model.Book;
import com.mahmon.bookcat.model.BookAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.mahmon.bookcat.Constants.BOOK_NODE;
import static com.mahmon.bookcat.Constants.ISBN_KEY;
import static com.mahmon.bookcat.Constants.USER;
import static com.mahmon.bookcat.Constants.USERS_NODE;

// Class the displays recycler view and populates it with user library books
public class LibraryFragment extends Fragment implements BookAdapter.OnItemClickListener {

    // Fragment context
    private Context mContext;
    // Firebase authorisation instance
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String userUid;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    // Book Array
    private List<Book> booksList;
    // Add book button
    private Button btnAddBook;
    // Recycler view variables
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private BookAdapter bookAdapter;
    // No books in library text view
    private TextView noBooksPrompt;

    @Nullable
    @Override
    public View onCreateView(   @NonNull LayoutInflater inflater,
                                @Nullable ViewGroup container,
                                @Nullable Bundle savedInstanceState) {
        // Create fragViewLibrary instance
        View fragViewLibrary = inflater.inflate(R.layout.fragment_library,
                container, false);
        // Get context
        mContext = getContext();
        // Instantiate books list array as array list
        booksList = new ArrayList<>();
        // Instantiate the adapter
        bookAdapter = new BookAdapter(mContext, booksList);
        // Link to xml recycler_view
        recyclerView = fragViewLibrary.findViewById(R.id.recycler_view);
        // Link to no Books text prompt and set invisible by default
        noBooksPrompt = fragViewLibrary.findViewById(R.id.no_books_prompt);
        noBooksPrompt.setVisibility(View.GONE);
        // Create 3 column grid for layout
        recyclerViewLayoutManager = new GridLayoutManager(mContext, 3);
        // Set the layout manager
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        // Set the adapter
        recyclerView.setAdapter(bookAdapter);
        // Attach Click listener to BookAdapter
        bookAdapter.setOnItemClickListener(LibraryFragment.this);
        // Initialise Firebase authorisation instance
        mAuth = FirebaseAuth.getInstance();
        // TEST: Check there is a user logged in
        // (Should be always true, screen only accesible to logged in user)
        if (mAuth.getCurrentUser() != null) {
            // Get current user UID
            currentUser = mAuth.getCurrentUser();
            userUid = currentUser.getUid();
            // Print error message and take app back to login screen
        } else {
            Toast.makeText(mContext,
                    "Error: No user logged in",
                    Toast.LENGTH_LONG).show();
            // TODO: Add intent to take app back to log in screen
        }
        // Get Firebase database reference for Book node
        mDatabaseRef = FirebaseDatabase.getInstance()
                .getReference(USERS_NODE)
                .child(userUid)
                .child(BOOK_NODE);
        // Instantiate database listener
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            // Method called on activity load and on any data changes
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear local list every time to prevent duplicate entries
                booksList.clear();
                // For loop to iterate through database
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // Store each Book in a local Book Object
                    Book book = postSnapshot.getValue(Book.class);
                    // Get book isbn key from database and set it to local book
                    book.setIsbn(postSnapshot.getKey());
                    // Add the local book to local list
                    booksList.add(book);
                }
                // If there are no books in the list show the 'no books' prompt
                if (booksList.isEmpty()) {
                    noBooksPrompt.setVisibility(View.VISIBLE);
                } // Update Adapter every time
                bookAdapter.notifyDataSetChanged();
            }
            // Called if database cannot be reached
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Display the error message
                Toast.makeText(mContext,
                        databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // Link to the goto add book button
        btnAddBook = fragViewLibrary.findViewById(R.id.btn_add_book);
        // Add listener for button
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On click call the add book method
                gotoAddBook();
            }
        });
        // Return the fragment view to the container
        return fragViewLibrary;
    }

    // Method called when a list item is clicked
    @Override
    public void onItemClick(int position) {
        // Get the book clicked and make it a final instantiation
        final Book clickedBook = booksList.get(position);
        // Call the show book method, passing the ISBN and userId
        showBook(clickedBook.getIsbn(), userUid);
    }

    // Method to create new fragment and replace in the fragment container
    private void showBook(String isbn, String userUid) {
        // Create new BookFragment
        BookFragment bookFragment = new BookFragment();
        // Create new data bundle
        Bundle bookData = new Bundle();
        // Store the isbn value and userUid in the data bundle
        bookData.putString(ISBN_KEY, isbn);
        bookData.putString(USER, userUid);
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

    // Method to create new fragment and replace in the fragment container
    public void gotoAddBook() {
        // Create fragment transaction object
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        // Put the RegisterFragment into the fragment_container
        fragmentTransaction.replace(R.id.fragment_container, new AddBookFragment());
        // Don't add the fragment to the back stack (avois issues with back button)
        fragmentTransaction.addToBackStack(null);
        // Commit the transaction
        fragmentTransaction.commit();
    }

}
