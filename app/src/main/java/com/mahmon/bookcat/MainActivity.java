package com.mahmon.bookcat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the view to activity_main.xml
        setContentView(R.layout.activity_main);
        // Create a fragment manager instance
        LoginFragment loginFragment = new LoginFragment();
        // Load the loginFragment into fragment_container
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, loginFragment).commit();

        /* TEST WRITE TO DATABASE */
        // TEST FIREBASE DATA
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        // Write to database
        myRef.setValue("Hello, World!");
        /* REMOVE THIS SECTION LATER */

    }


}
