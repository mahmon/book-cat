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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mahmon.bookcat.R;
import com.mahmon.bookcat.model.Book;
import com.mahmon.bookcat.model.BookAdapter;

import java.util.ArrayList;
import java.util.List;

public class CatalogueFragment extends Fragment {

    // Fragment context
    private Context mContext;
    // Recycler view variables
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private BookAdapter bookAdapter;
    // Book Array
    private List<Book> booksList;
    // Firebase authorisation instance
    private FirebaseAuth mAuth;
    // View elements
    private TextView userName;
    private Button btnSignOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create fragViewLogin instance
        View fragViewWelcome = inflater.inflate(R.layout.fragment_catalogue, container, false);
        // Get context
        mContext = getContext();
        // Instantiate books array
        booksList = new ArrayList<>();
        // Create the adapter
        bookAdapter = new BookAdapter(mContext, booksList);
        // Link to xml recycler_view
        recyclerView = fragViewWelcome.findViewById(R.id.recycler_view);
        // Create 2 column grid for layout
        recyclerViewLayoutManager = new GridLayoutManager(mContext, 3);
        // Set the layout manager
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        // Set the adapter
        recyclerView.setAdapter(bookAdapter);

        /* DUMMY DATA */
        prepareBookData();

        // Initialise Firebase authorisation instance
        mAuth = FirebaseAuth.getInstance();
        // Get signed in user details
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String name = currentUser.getDisplayName();
        // Link to view elements
        userName = fragViewWelcome.findViewById(R.id.logged_in_user_name);
        btnSignOut = fragViewWelcome.findViewById(R.id.btn_sign_out);
        // Set user name
        userName.setText("Signed in as: " + name);
        // Attach listener to the button
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call sign out method
                signOut();
            }
        });
        // Return the fragment view to the activity
        return fragViewWelcome;
    }

    // Sign out method
    public void signOut() {
        mAuth.signOut();
        gotoLoginScreen();
    }

    // Method to create new fragment and replace in the fragment container
    public void gotoLoginScreen() {
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new LoginFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /* Create dummy book data to test recycler view */
    private void prepareBookData() {
        Book book1 = new Book("1234567890", "Great Expextations", "Charles Dickens");
        booksList.add(book1);
        Book book2 = new Book("1234567890", "Great Expextations", "Charles Dickens");
        booksList.add(book2);
        Book book3 = new Book("1234567890", "Great Expextations", "Charles Dickens");
        booksList.add(book3);
        Book book4 = new Book("1234567890", "Great Expextations", "Charles Dickens");
        booksList.add(book4);
        Book book5 = new Book("1234567890", "Great Expextations", "Charles Dickens");
        booksList.add(book5);
        Book book6 = new Book("1234567890", "Great Expextations", "Charles Dickens");
        booksList.add(book6);
        Book book7 = new Book("1234567890", "Great Expextations", "Charles Dickens");
        booksList.add(book7);
        Book book8 = new Book("1234567890", "Great Expextations", "Charles Dickens");
        booksList.add(book8);
        // Update the adapter
        bookAdapter.notifyDataSetChanged();
    }

}
