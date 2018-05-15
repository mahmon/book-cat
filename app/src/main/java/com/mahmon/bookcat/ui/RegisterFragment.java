package com.mahmon.bookcat.ui;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mahmon.bookcat.R;
import com.mahmon.bookcat.ui.LoginFragment;

public class RegisterFragment extends Fragment {

    // Firebase authorisation instance
    private FirebaseAuth mAuth;
    // View elements
    private EditText userName;
    private EditText userEmail;
    private EditText userPassword;
    private EditText userPassConf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create fragViewRegister instance
        View fragViewRegister = inflater.inflate(R.layout.fragment_register, container, false);
        // Initialise Firebase authorisation instance
        mAuth = FirebaseAuth.getInstance();
        // Sign out any logged in user
        mAuth.signOut();
        // Link to view elements
        userName = fragViewRegister.findViewById(R.id.user_name);
        userEmail = fragViewRegister.findViewById(R.id.user_email);
        userPassword = fragViewRegister.findViewById(R.id.user_password);
        userPassConf = fragViewRegister.findViewById(R.id.user_pass_conf);
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

    // Method to register user with Firebase authentication
    public void registerUser() {
        // Get name, email and password from EditTExt boxes
        final String name = userName.getText().toString().trim();
        final String email = userEmail.getText().toString().trim();
        final String password = userPassword.getText().toString().trim();
        final String passConf = userPassConf.getText().toString().trim();
        // Check name entered
        if (TextUtils.isEmpty(name)) {
            // If not prompt user
            Toast.makeText(getContext(), "Enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check email entered
        if (TextUtils.isEmpty(email)) {
            // If not prompt user
            Toast.makeText(getContext(), "Enter email address", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check password entered
        if (TextUtils.isEmpty(password)) {
            // If not prompt user
            Toast.makeText(getContext(), "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check password is at least 6 characters long
        if (password.length() < 6) {
            // If not prompt user
            Toast.makeText(getContext(), "Password too short, enter minimum 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check both passwords entered match
        if (!password.equals(passConf)) {
            // If not prompt user
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create and register user with email address and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    // When the task completes...
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If user succesfully registered
                        if (task.isSuccessful()) {
                            // Get the current user (just created and logged in automatically)
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            // Update the user profile
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    // Set their displayt name
                                    .setDisplayName(name)
                                    .build();
                            // Run the updates
                            user.updateProfile(profileUpdates);
                            // Sign in success
                            Toast.makeText(getContext(), "Sign up success, please login", Toast.LENGTH_LONG).show();
                            // Call the isLoggedIn method
                            newUserRegistered(user);
                        }
                    }
                // Add on failure listener, called if sign up fails
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Get details of the failure error
                String error = e.getLocalizedMessage();
                // If sign up fails, display a message to the user.
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to react if user is logged in
    private void newUserRegistered(FirebaseUser user) {
        // If the user is logged in
        if (user != null) {
            // Launch the welcomeFagment
            mAuth.signOut();
            gotoLoginScreen();
        }
    }

    // Method to create new fragment and replace in the fragment container
    public void gotoLoginScreen() {
        // Create fragment transaction object
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        // Put the CatalogueFragment into the fragment_container
        fragmentTransaction.replace(R.id.fragment_container, new LoginFragment());
        // Don't add the fragment to the back stack (avoids issues with back button)
        fragmentTransaction.addToBackStack(null);
        // Commit the transaction
        fragmentTransaction.commit();
    }

}

