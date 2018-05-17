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

    // Fragement context
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
        bookIsbn = fragViewBook.findViewById(R.id.book_isbn);
        jsonText = fragViewBook.findViewById(R.id.json_text);
        btnGotoCatalogue = fragViewBook.findViewById(R.id.btn_goto_catalogue);
        // Set the isbn text
        bookIsbn.setText(isbn);

        /* TEST JSON */
        // Formulate the request and handle the response.
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:1861976127";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        jsonText.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });
        // Get a RequestQueue
        RequestQueue queue = GoogleApiRequest
                .getInstance(mContext.getApplicationContext())
                .getRequestQueue();
        // Add a request to the RequestQueue.
        GoogleApiRequest.getInstance(mContext)
                .addToRequestQueue(stringRequest);


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
