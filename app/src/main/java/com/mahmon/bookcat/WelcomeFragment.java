package com.mahmon.bookcat;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeFragment extends Fragment {

    /* DEVELOP WELCOME SCREEN UI NEXT, SET UP CRUD TO DATABASE */
    // BRANCH START

    // Firebase authorisation instance
    private FirebaseAuth mAuth;
    // View elements
    private TextView userName;
    private Button btnSignOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create fragViewLogin instance
        View fragViewWelcome = inflater.inflate(R.layout.fragment_welcome, container, false);
        // Initialise Firebase authorisation instance
        mAuth = FirebaseAuth.getInstance();
        // Get signed in user details
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String name = currentUser.getDisplayName();
        // Link to view elements
        userName = fragViewWelcome.findViewById(R.id.logged_in_user_name);
        btnSignOut = fragViewWelcome.findViewById(R.id.btn_sign_out);
        // Set user name
        userName.setText(name);
        // Attach listener to the button
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call signout method
                signOut();
            }
        });
        // Return the fragment view to the activity
        return fragViewWelcome;
    }

    // Signout method
    public void signOut() {
        mAuth.signOut();
        gotoLoginScreen();
    }

    // Method to create new fragment and replace in the fragment container
    public void gotoLoginScreen() {
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new LoginFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
