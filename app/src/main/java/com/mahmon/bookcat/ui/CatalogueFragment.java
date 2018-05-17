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
    // Bottom button
    private Button btnAddBook;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create fragViewLogin instance
        View fragViewCatalogue = inflater.inflate(R.layout.fragment_catalogue, container, false);
        // Get context
        mContext = getContext();
        // Instantiate books array
        booksList = new ArrayList<>();
        // Create the adapter
        bookAdapter = new BookAdapter(mContext, booksList);
        // Link to xml recycler_view
        recyclerView = fragViewCatalogue.findViewById(R.id.recycler_view);
        // Create 2 column grid for layout
        recyclerViewLayoutManager = new GridLayoutManager(mContext, 3);
        // Set the layout manager
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        // Set the adapter
        recyclerView.setAdapter(bookAdapter);
        // Link to the button
        btnAddBook = fragViewCatalogue.findViewById(R.id.btn_add_book);
        // Add listener for button
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddBook();
            }
        });
        /*DUMMY DATA*/
        prepareBookData();
        // Return the fragment view to the container
        return fragViewCatalogue;
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

    /* Create dummy book data to test recycler view */
    private void prepareBookData() {
        Book book1 = new Book("9780066238500", "Great Expextations", "Charles Dickens");
        booksList.add(book1);
        Book book2 = new Book("9780385533225", "Great Expextations", "Charles Dickens");
        booksList.add(book2);
        Book book3 = new Book("9780451167712", "Great Expextations", "Charles Dickens");
        booksList.add(book3);
        Book book4 = new Book("9780385533225", "Great Expextations", "Charles Dickens");
        booksList.add(book4);
        Book book5 = new Book("9780385533225", "Great Expextations", "Charles Dickens");
        booksList.add(book5);
        Book book6 = new Book("9780385533225", "Great Expextations", "Charles Dickens");
        booksList.add(book6);
        Book book7 = new Book("9780385533225", "Great Expextations", "Charles Dickens");
        booksList.add(book7);
        Book book8 = new Book("9780385533225", "Great Expextations", "Charles Dickens");
        booksList.add(book8);
        // Update the adapter
        bookAdapter.notifyDataSetChanged();
    }

}
