package com.mahmon.bookcat;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeFragment extends Fragment {

    // Firebase authorisation instance
    private FirebaseAuth mAuth;
    // View elements
    private TextView userName;

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
        // Set user name
        userName.setText(name);
        // Return the fragment view to the activity
        return fragViewWelcome;
    }

}
