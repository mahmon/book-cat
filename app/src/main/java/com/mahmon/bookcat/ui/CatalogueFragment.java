package com.mahmon.bookcat.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mahmon.bookcat.R;
import com.mahmon.bookcat.model.Book;
import com.mahmon.bookcat.model.BookAdapter;

import java.util.ArrayList;
import java.util.List;

public class CatalogueFragment extends Fragment {

    /* NEED TO WORK ON CRUD = C first */

    // Fragment context
    private Context mContext;
    // Recycler view variables
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private BookAdapter bookAdapter;
    // Book Array
    private List<Book> booksList;
    // Bottom menu
    private android.support.v7.widget.Toolbar mToolBarBottom;


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
        // Link to bottom toolbar
        mToolBarBottom = fragViewCatalogue.findViewById(R.id.tool_bar_bottom);
        // Inflate XML menu_tool_bar_bottom to bottomToolbar
        mToolBarBottom.inflateMenu(R.menu.menu_bottom);
        // Set up bottom toolbar
        mToolBarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // User clicked add_book
                    case R.id.add_book:
                        gotoAddBook();
                        return true;
                    // User clicked save_book
                    case R.id.save_book:
                        // todo
                        return true;
                    default:
                        return false;
                }
            }
        });

        prepareBookData();

        return fragViewCatalogue;
    }

    // Goto add book fragment
    public void gotoAddBook() {
        // TODO
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
