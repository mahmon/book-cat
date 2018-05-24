package com.mahmon.bookcat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mahmon.bookcat.ui.LoginFragment;

// Main activity class, entry point for the application
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
    }

}