package com.mahmon.bookcat.ui;

import android.content.Context;
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
import com.mahmon.bookcat.Constants;
import com.mahmon.bookcat.R;
import com.mahmon.bookcat.model.Book;
import com.mahmon.bookcat.model.BookAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.mahmon.bookcat.Constants.BOOK_NODE;
import static com.mahmon.bookcat.Constants.USERS_NODE;

public class LibraryFragment extends Fragment implements BookAdapter.OnItemClickListener {

    // Fragment context
    private Context mContext;
    // Recycler view variables
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private BookAdapter bookAdapter;
    // No books text view
    private TextView noBooksPrompt;
    // Firebase authorisation instance
    private FirebaseAuth mAuth;
    private String userUid;
    // Variables for Firebase connections
    private DatabaseReference mDatabaseRef;
    // Listener variable
    private ValueEventListener mDBListener;
    // Book Array
    private List<Book> booksList;
    // Bottom button
    private Button btnAddBook;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create fragViewLogin instance
        View fragViewCatalogue = inflater.inflate(R.layout.fragment_library, container, false);
        // Get context
        mContext = getContext();
        // Instantiate books array
        booksList = new ArrayList<>();
        // Create the adapter
        bookAdapter = new BookAdapter(mContext, booksList);
        // Link to xml recycler_view
        recyclerView = fragViewCatalogue.findViewById(R.id.recycler_view);
        // Link to no Books text prompt
        noBooksPrompt = fragViewCatalogue.findViewById(R.id.no_books_prompt);
        // Set invisible by default
        noBooksPrompt.setVisibility(View.GONE);
        // Create 2 column grid for layout
        recyclerViewLayoutManager = new GridLayoutManager(mContext, 3);
        // Set the layout manager
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        // Set the adapter
        recyclerView.setAdapter(bookAdapter);
        // Attach Click listener to BookAdapter
        bookAdapter.setOnItemClickListener(LibraryFragment.this);
        // Initialise Firebase authorisation instance
        mAuth = FirebaseAuth.getInstance();
        // Get signed in user details
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userUid = currentUser.getUid();
        // Get Firebase database reference for users node
        mDatabaseRef = FirebaseDatabase.getInstance()
                .getReference(USERS_NODE)
                .child(userUid)
                .child(BOOK_NODE);
        // Instantiate database listener
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            // Method called on activity load and on any data changes
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear local list every time to prevent duplicate entry
                booksList.clear();
                // For loop to iterate through database
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // Store each Book in a local Book Object
                    Book book = postSnapshot.getValue(Book.class);
                    // Get book node key from database and set it to local event
                    book.setIsbn(postSnapshot.getKey());
                    // Add the local Event to local list
                    booksList.add(book);
                }
                // If there are no books in the list show the no books prompt
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
        // Link to the button
        btnAddBook = fragViewCatalogue.findViewById(R.id.btn_add_book);
        // Add listener for button
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddBook();
            }
        });
        // Return the fragment view to the container
        return fragViewCatalogue;
    }

    @Override
    public void onItemClick(int position) {
        final Book clickedBook = booksList.get(position);
        gotoBook(clickedBook.getIsbn(), userUid);
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
