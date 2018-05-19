package com.mahmon.bookcat.model;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mahmon.bookcat.Constants;

import org.json.JSONException;
import org.json.JSONObject;

// Class for handling requests to GoogleBooks
public class GoogleApiRequest {

    // Interface used to get results out of getJSONObjectResult below
    public interface VolleyCallback {
        void onSuccessResponse(JSONObject result) throws JSONException;
    }

    // Instance variable of this class
    private static GoogleApiRequest mInstance;
    // RequestQueue object
    private RequestQueue mRequestQueue;
    // Context variable
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

    // Method call for getting Google Books Api JSON result
    public void getGoogleBookAsJSONObject(String isbnEntered, final VolleyCallback callback) {
        // Build the request string
        String url = Constants.GOOGLE_URL_BASE + isbnEntered;
        // Create string request and add listeners
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Pass the result to interface above
                        try {
                            callback.onSuccessResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Print volley error message
                        String e = error.toString();
                        Toast.makeText(mContext, e, Toast.LENGTH_LONG).show();
                    }
                });
        // Call addToRequestQueue below, passing in the request
        addToRequestQueue(jsonObjectRequest);
    }

    // Method to instantiate a request queue
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // Instantiate the queue and pass in the application context
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        // Return the queue
        return mRequestQueue;
    }

    // Method to add a request to the request queue
    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

}