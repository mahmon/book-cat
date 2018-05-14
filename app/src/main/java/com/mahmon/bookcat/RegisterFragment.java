package com.mahmon.bookcat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterFragment extends Fragment {

    // Firebase authorisation instance
    private FirebaseAuth mAuth;
    // View elements
    private EditText userName;
    private EditText userEmail;
    private EditText userPassword;
    private EditText confirmPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create fragViewRegister instance
        View fragViewRegister = inflater.inflate(R.layout.fragment_register, container, false);
        // Initialise Firebase authorisation instance
        mAuth = FirebaseAuth.getInstance();
        // Sign out any logged in user
        mAuth.signOut();
        // Check if user is signed in already
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Call isLoggedIn passing in current user
        isLoggedIn(currentUser);
        // Link to view elements
        userName = fragViewRegister.findViewById(R.id.user_name);
        userEmail = fragViewRegister.findViewById(R.id.user_email);
        userPassword = fragViewRegister.findViewById(R.id.user_password);
        confirmPassword = fragViewRegister.findViewById(R.id.user_pass_conf);
        Button btnRegisterUser = fragViewRegister.findViewById(R.id.btn_register);
        // Attach listener to the button
        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call registerUser method defined below
                registerUser();
            }
        });
        // Return the fragment view to the activity
        return fragViewRegister;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /* COMMENT THIS METHOD
     * set it to open new screen and display user name
     * add password checking logic
     *
     *
     */
    // Method to register user with Firebase authentication
    public void registerUser() {
        final String name = userName.getText().toString().trim();
        final String email = userEmail.getText().toString().trim();
        final String password = userPassword.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "Enter name!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create and register user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            user.updateProfile(profileUpdates);
                            // Sign in success
                            Toast.makeText(getContext(), "Sign up success", Toast.LENGTH_LONG).show();
                            isLoggedIn(user);
                        } else {
                            // Sign in fails
                            Toast.makeText(getContext(), "Sign up failure", Toast.LENGTH_SHORT).show();
                            // TODO expand error handling for failed sign up
                        }
                    }
                });
    }

    // Method to create new fragment and replace in the fragment container
    public void gotoWelcomeScreen() {
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new WelcomeFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Method to

    private void isLoggedIn(FirebaseUser user) {
        // If the user is logged in
        if (user != null) {
            // Launch the welcome fragment
            gotoWelcomeScreen();
        }
    }

}

