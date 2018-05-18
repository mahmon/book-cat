package com.mahmon.bookcat.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

// Class for handling requests to GoogleBooks
public class GoogleApiRequest {

    // String to store google api base string
    private static final String GOOGLE_URL_BASE = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    // Interface used to get results out of createStringRequest below
    public interface VolleyCallback {
        void onSuccessResponse(String result);
    }

    // Instance variable of this class
    private static GoogleApiRequest mInstance;
    // RequestQueue object
    private RequestQueue mRequestQueue;
    // Contenxt variable
    private static Context mContext;

    // Private constructor
    private GoogleApiRequest(Context context) {
        mContext = context;
        // Instantiate a request queue
        mRequestQueue = getRequestQueue();
    }

    // Public method to get an instance of GoogleApiRequest
    public static synchronized GoogleApiRequest getInstance(Context context) {
        // Pass in a context and instantiate
        if (mInstance == null) {
            mInstance = new GoogleApiRequest(context);
        }
        // Return the instance
        return mInstance;
    }

    public void getStringResult(String isbnEntered, final VolleyCallback callback) {
        // Build the request string
        String url = GOOGLE_URL_BASE + isbnEntered;
        // Create string request and add listeners
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    // Response received, process it
                    public void onResponse(String response) {
                        // Do something with the response
                        callback.onSuccessResponse(response);
                    }
                },
                // Error received
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });
        // Call addToRequestQueue below, passing in the request
        addToRequestQueue(stringRequest);
    }

    // Method to build String request (1861976127 < this works!!)
    public void createStringRequest (String isbnEntered) {
        // Build the request string
        String url = GOOGLE_URL_BASE + isbnEntered;
        // Create string request and add listeners
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    // Response received, process it
                    public void onResponse(String response) {
                        // Do something with the response
                    }
                },
                // Error received
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });
        // Call addToRequestQueue below, passing in the request
        addToRequestQueue(stringRequest);
    }

    // Method to instantiate a request queue
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // Instantiate the queue and pass in the application context
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        // Return the queueu
        return mRequestQueue;
    }

    // Method to add a request to the request queue
    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

}