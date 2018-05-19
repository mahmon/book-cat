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
        btnGotoLibrary = fragViewBook.findViewById(R.id.btn_goto_library);
        btnUpdateBook = fragViewBook.findViewById(R.id.btn_update_book);
        btnDeleteBook = fragViewBook.findViewById(R.id.btn_delete_book);
        // Get Firebase instance and database ref to book
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference()
                .child(Constants.USERS_NODE)
                .child(userUid)
                .child(BOOK_NODE);
        // Get book from database
        // Instantiate database listener
        mDatabaseRef.child(isbn).addListenerForSingleValueEvent(new ValueEventListener() {
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
        // Set listener for button goto library
        btnGotoLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call goto catalogue method
                gotoLibrary();
            }
        });

        // TODO set listener for update book

        // Set listener for button goto library
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

    // Method called to delete books
    private void deleteBook(final String isbnToDelete) {
        mDatabaseRef.child(isbnToDelete).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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

    // Switch fragments back to library view
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

}
