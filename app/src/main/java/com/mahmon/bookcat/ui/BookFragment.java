package com.mahmon.bookcat.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mahmon.bookcat.Constants;
import com.mahmon.bookcat.R;
import com.mahmon.bookcat.model.GoogleApiRequest;

public class BookFragment extends Fragment {

    // Fragment context
    private Context mContext;
    // View elements
    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookIsbn;

    private TextView jsonText;

    private Button btnGotoCatalogue;
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
        // Link to view items
        bookTitle = fragViewBook.findViewById(R.id.book_title);
        bookAuthor = fragViewBook.findViewById(R.id.book_author);
        bookIsbn = fragViewBook.findViewById(R.id.book_isbn);
        jsonText = fragViewBook.findViewById(R.id.json_text);
        btnGotoCatalogue = fragViewBook.findViewById(R.id.btn_goto_catalogue);
        // Set the view text
        bookTitle.setText("Test Title");
        bookAuthor.setText("Test Author");
        bookIsbn.setText(isbn);

        /* TEST JSON */
        String testIsbn = "1861976127";
        GoogleApiRequest.getInstance(mContext).getStringResult(testIsbn, new GoogleApiRequest.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                jsonText.setText(result);
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
