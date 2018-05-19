package com.mahmon.bookcat.ui;

import android.content.Context;
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

public class BookFragment extends Fragment {

    // Fragment context
    private Context mContext;
    // View elements
    private ImageView bookCover;
    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookIsbn;
    private Button btnGotoCatalogue;
    // Firebase database
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    // Signed in user ID
    private String userUid;
    // String to isbn data
    private String isbn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create fragViewLogin instance
        View fragViewBook = inflater.inflate(R.layout.fragment_book, container, false);
        // Get the context
        mContext = getContext();
        // Get the passed arguments
        Bundle bookData = getArguments();
        isbn = bookData.getString(Constants.ISBN_KEY);
        userUid = bookData.getString(Constants.USER);
        // Link to view items
        bookCover = fragViewBook.findViewById(R.id.book_cover);
        bookTitle = fragViewBook.findViewById(R.id.book_title);
        bookAuthor = fragViewBook.findViewById(R.id.book_author);
        bookIsbn = fragViewBook.findViewById(R.id.book_isbn);
        btnGotoCatalogue = fragViewBook.findViewById(R.id.btn_goto_catalogue);
        // Get Firebase instance and database ref to book
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference()
                .child(Constants.USERS_NODE)
                .child(userUid)
                .child(BOOK_NODE)
                .child(isbn);
        // Get book from database
        // Instantiate database listener
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get book from database snapshot
                Book book = dataSnapshot.getValue(Book.class);
                // Use book to set text views for single book display
                bookTitle.setText(book.getTitle());
                bookAuthor.setText(book.getAuthor());
                String displayISBN = "ISBN: " + book.getIsbn();
                bookIsbn.setText(displayISBN);


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
        // Set listener for button
        btnGotoCatalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call goto catalogue method
                gotoCatalogue();
            }
        });
        // Return the fragment view to the activity
        return fragViewBook;
    }

    // Switch fragments back to catalogue view
    public void gotoCatalogue() {
        // Create fragment transaction object
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        // Put the CatalogueFragment into the fragment_container
        fragmentTransaction.replace(R.id.fragment_container, new CatalogueFragment());
        // Don't add the fragment to the back stack (avois issues with back button)
        fragmentTransaction.addToBackStack(null);
        // Commit the transaction
        fragmentTransaction.commit();
    }

}
