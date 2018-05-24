package com.mahmon.bookcat.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import static com.mahmon.bookcat.Constants.AUTHORS;
import static com.mahmon.bookcat.Constants.BOOK_NODE;
import static com.mahmon.bookcat.Constants.ISBN_KEY;
import static com.mahmon.bookcat.Constants.TITLE;
import static com.mahmon.bookcat.Constants.USER;

public class EditBookFragment extends Fragment {

    // Fragment context
    private Context mContext;
    // Current book values
    private String currentTitle;
    private String currentAuthor;
    // Firebase database
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    // Signed in user ID
    private String userUid;
    // String to isbn data
    private String isbn;
    // View elements
    private ImageView bookCover;
    private EditText bookTitle;
    private EditText bookAuthor;
    private TextView bookIsbn;
    private Button btnCancel;
    private Button btnSaveUpdates;

    @Nullable
    @Override
    public View onCreateView(   @NonNull LayoutInflater inflater,
                                @Nullable ViewGroup container,
                                @Nullable Bundle savedInstanceState) {
        // Create fragViewBook instance
        View fragViewBook = inflater.inflate(R.layout.fragment_edit_book,
                container, false);
        // Get the context
        mContext = getContext();
        // Get the passed bundle data
        Bundle bookData = getArguments();
        // Store data in local variables
        isbn = bookData.getString(ISBN_KEY);
        userUid = bookData.getString(USER);
        // Link to view items
        bookCover = fragViewBook.findViewById(R.id.book_cover);
        bookTitle = fragViewBook.findViewById(R.id.book_title);
        bookAuthor = fragViewBook.findViewById(R.id.book_author);
        bookIsbn = fragViewBook.findViewById(R.id.book_isbn);
        btnCancel = fragViewBook.findViewById(R.id.btn_return_to_library);
        btnSaveUpdates = fragViewBook.findViewById(R.id.btn_save_updates);
        // Get Firebase instance and database ref to book node
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference()
                .child(Constants.USERS_NODE)
                .child(userUid)
                .child(BOOK_NODE);
        // Get book from database, instantiate database listener
        mDatabaseRef.child(isbn).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get book from database snapshot
                Book book = dataSnapshot.getValue(Book.class);
                // Use book to set 'hints' in edit text boxes for book displayed
                bookTitle.setHint(book.getTitle());
                bookAuthor.setHint(book.getAuthor());
                String displayISBNWithlabel = "ISBN: " + book.getIsbn();
                bookIsbn.setText(displayISBNWithlabel);
                // Use book to load cover into image view
                String imageUrl = book.getCoverImageURL();
                Picasso.with(mContext)
                        .load(imageUrl)
                        .fit().centerCrop()
                        .into(bookCover);
                // Use book to set current values;
                // used to overwrite book values if no values entered,
                // allows for updating only title to only author
                currentTitle = book.getTitle();
                currentAuthor = book.getAuthor();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Display the error message
                Toast.makeText(mContext,
                        databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // Set listener for cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cancel the updates, reload the book view
                reloadBookView(isbn, userUid);
            }
        });
        // Set listener for button save updates
        btnSaveUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get input from Edit text boxes
                String editedTitle = bookTitle.getText().toString().trim();
                String editedAuthor = bookAuthor.getText().toString().trim();
                // Check title entered, if not use current title
                if (TextUtils.isEmpty(editedTitle)) {
                    editedTitle = currentTitle;
                }
                // Check author entered, if not use current author
                if (TextUtils.isEmpty(editedAuthor)) {
                    editedAuthor = currentAuthor;
                }
                // Call update book method
                updateBook(editedTitle, editedAuthor);
            }
        });
        // Return the fragment view to the activity
        return fragViewBook;
    }

    // Method to create new fragment and replace in the fragment container
    private void reloadBookView(String isbn, String userUid) {
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

    // Method called to update book
    private void updateBook(String editedTitle, String editedAuthor) {
        // Overwrite values with passed data
        mDatabaseRef.child(isbn).child(TITLE).setValue(editedTitle);
        mDatabaseRef.child(isbn).child(AUTHORS).setValue(editedAuthor);
        Toast.makeText(mContext, "Book Updated", Toast.LENGTH_SHORT).show();
        // Call the reload book view method
        reloadBookView(isbn, userUid);
    }

}
