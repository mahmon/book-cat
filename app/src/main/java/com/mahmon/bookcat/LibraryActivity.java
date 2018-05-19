package com.mahmon.bookcat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mahmon.bookcat.ui.LibraryFragment;

public class LibraryActivity extends AppCompatActivity {

    // Firebase authorisation instance
    private FirebaseAuth mAuth;
    // View elements
    private Toolbar toolbarTop;
    private TextView userName;
    private TextView signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the view to activity_main.xml
        setContentView(R.layout.activity_library);
        // Create a fragment manager instance
        LibraryFragment libraryFragment = new LibraryFragment();
        // Load the loginFragment into fragment_container
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, libraryFragment).commit();
        // Assign view elements
        toolbarTop = findViewById(R.id.tool_bar_top);
        userName = findViewById(R.id.logged_in_user_name);
        signOut = findViewById(R.id.sign_out);
        // Set up top toolbar
        setSupportActionBar(toolbarTop);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Initialise Firebase authorisation instance
        mAuth = FirebaseAuth.getInstance();
        // Get signed in user details
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String name = currentUser.getDisplayName();
        // Set user name
        String message = "Signed in as: " + name;
        userName.setText(message);
        // Add listener for sign out
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    // Sign out method
    public void signOut() {
        mAuth.signOut();
        gotoLoginScreen();
    }

    // Method to create new fragment and replace in the fragment container
    public void gotoLoginScreen() {
        // Instantiate new intent to start DisplayEventsActivity
        Intent intent = new Intent(this, MainActivity.class);
        // Start Activity
        startActivity(intent);
    }

}
