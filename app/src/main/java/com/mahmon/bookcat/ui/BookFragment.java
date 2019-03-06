package com.mahmon.bookcat.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmon.bookcat.Constants;
import com.mahmon.bookcat.R;
import com.mahmon.bookcat.model.Book;
import com.squareup.picasso.Picasso;

import static com.mahmon.bookcat.Constants.BOOK_NODE;
import static com.mahmon.bookcat.Constants.ISBN_KEY;
import static com.mahmon.bookcat.Constants.USER;

public class BookFragment extends Fragment {

    // Fragment context
    private Context mContext;
    // View elements
    private ImageView bookCover;
    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookIsbn;
    private Button btnGotoLibrary;
    private Button btnUpdateBook;
    private Button btnDeleteBook;

    // Firebase database variables
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private String userUid;
    // String for isbn data
    private String isbn;

    @Nullable
    @Override
    public View onCreateView(   @NonNull LayoutInflater inflater,
                                @Nullable ViewGroup container,
                                @Nullable Bundle savedInstanceState) {
        // Create fragViewBook instance
        View fragViewBook = inflater.inflate(R.layout.fragment_book,
                container, false);
        // Get the context
        mContext = getContext();
        // Get the passed bundle data
        Bundle bookData = getArguments();
        // Store data in local string variables
        isbn = bookData.getString(ISBN_KEY);
        userUid = bookData.getString(USER);
        // Link to view items
        bookCover = fragViewBook.findViewById(R.id.book_cover);
        bookTitle = fragViewBook.findViewById(R.id.book_title);
        bookAuthor = fragViewBook.findViewById(R.id.book_author);
        bookIsbn = fragViewBook.findViewById(R.id.book_isbn);
        btnGotoLibrary = fragViewBook.findViewById(R.id.btn_goto_library);
        btnUpdateBook = fragViewBook.findViewById(R.id.btn_update_book);
        btnDeleteBook = fragViewBook.findViewById(R.id.btn_delete_book);
        // Get Firebase instance and database ref to book node
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference()
                .child(Constants.USERS_NODE)
                .child(userUid)
                .child(BOOK_NODE);
        // Get book from database, instantiate database listener
        mDatabaseRef.child(isbn)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get book from database snapshot
                Book book = dataSnapshot.getValue(Book.class);
                // Use book to set text views for single book display
                bookTitle.setText(book.getTitle());
                bookAuthor.setText(book.getAuthor());
                String displayISBNWithLabel = "ISBN: " + book.getIsbn();
                bookIsbn.setText(displayISBNWithLabel);
                // Use book to load cover into image view
                String imageUrl = book.getCoverImageURL();
                Picasso.with(mContext)
                        .load(imageUrl)
                        .fit().centerCrop()
                        .into(bookCover);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Display the error message
                Toast.makeText(mContext,
                        databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // Set listener for button goto library
        btnGotoLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call goto catalogue method
                gotoLibrary();
            }
        });
        // Set listener for button goto edit book
        btnUpdateBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call goto catalogue method
                gotoEditBook(isbn);
            }
        });
        // Set listener for button delete book
        btnDeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call delete book method
                deleteBook(isbn);
            }
        });
        // Return the fragment view to the activity
        return fragViewBook;
    }

    // Switch fragments back to library fragment
    public void gotoLibrary() {
        // Create fragment transaction object
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        // Put the LibraryFragment into the fragment_container
        fragmentTransaction.replace(R.id.fragment_container, new LibraryFragment());
        // Don't add the fragment to the back stack (avois issues with back button)
        fragmentTransaction.addToBackStack(null);
        // Commit the transaction
        fragmentTransaction.commit();
    }

    // Switch fragments to the edit book fragment
    public void gotoEditBook(String isbn) {
        // Create new BookFragment
        EditBookFragment editBookFragment = new EditBookFragment();
        // Create new data bundle
        Bundle bookData = new Bundle();
        // Store the isbn value and userUid in the data bundle
        bookData.putString(Constants.ISBN_KEY, isbn);
        bookData.putString(USER, userUid);
        // Add the bundle to the fragment
        editBookFragment.setArguments(bookData);
        // Create fragment transaction object
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        // Put the bookFragment into the fragment_container
        fragmentTransaction.replace(R.id.fragment_container, editBookFragment);
        // Don't add the fragment to the back stack (avoids issues with back button)
        fragmentTransaction.addToBackStack(null);
        // Commit the transaction
        fragmentTransaction.commit();
    }

    // Method called to delete book
    private void deleteBook(final String isbnToDelete) {
        mDatabaseRef.child(isbnToDelete).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Prompt user that book deleted
                String deleteMsg = isbnToDelete + " has been removed from your library.";
                Toast.makeText(mContext, deleteMsg, Toast.LENGTH_SHORT).show();
                // Go to the library
                gotoLibrary();
            }
        });
    }

}
