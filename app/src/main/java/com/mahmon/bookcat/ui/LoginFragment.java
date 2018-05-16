package com.mahmon.bookcat.ui;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mahmon.bookcat.CatalogueActivity;
import com.mahmon.bookcat.R;

public class LoginFragment extends Fragment {

    // Firebase authorisation instance
    private FirebaseAuth mAuth;
    // View elements
    private EditText userEmail;
    private EditText userPassword;
    private Button btnLogin;
    private TextView clickToSignUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create fragViewLogin instance
        View fragViewLogin = inflater.inflate(R.layout.fragment_login, container, false);
        // Initialise Firebase authorisation instance
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        checkLoggedIn(currentUser);
        // Link to view elements
        userEmail = fragViewLogin.findViewById(R.id.user_email);
        userPassword = fragViewLogin.findViewById(R.id.user_password);
        btnLogin = fragViewLogin.findViewById(R.id.btn_login);
        clickToSignUp = fragViewLogin.findViewById(R.id.click_to_sign_up);
        // Attach listener to the button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get email and password from EditText boxes
                final String email = userEmail.getText().toString().trim();
                final String password = userPassword.getText().toString().trim();
                // Check email address entered
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
                // Call login method when email and password entered
                logIn(email, password);
            }
        });
        // Attach listener to textView
        clickToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call gotoRegisterScreen method defined below
                gotoRegisterScreen();
            }
        });
        // Return the fragment view to the activity
        return fragViewLogin;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // Action to take if user is already logged in
    private void checkLoggedIn(FirebaseUser user) {
        if (user != null) {
            // Call the goto welcome screen
            gotoCatalogueScreen();
        }
    }

    // Log in method
    private void logIn(String email, String password) {
        // Sign in with email and password
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    // When the task completes...
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If user logged in succesfully
                        if (task.isSuccessful()) {
                            // Call gotoWelcomeScreen method
                            gotoCatalogueScreen();
                        }
                    }
                // Add on failure listener, called if login fails
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Get details of the failure error
                String error = e.getLocalizedMessage();
                // If sign in fails, display a message to the user.
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to create new fragment and replace in the fragment container
    public void gotoRegisterScreen() {
        // Create fragment transaction object
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        // Put the RegisterFragment into the fragment_container
        fragmentTransaction.replace(R.id.fragment_container, new RegisterFragment());
        // Don't add the fragment to the back stack (avois issues with back button)
        fragmentTransaction.addToBackStack(null);
        // Commit the transaction
        fragmentTransaction.commit();
    }

    // Method to create new fragment and replace in the fragment container
    public void gotoCatalogueScreen() {
        // Instantiate new intent to start DisplayEventsActivity
        Intent intent = new Intent(getContext(), CatalogueActivity.class);
        // Start Activity
        startActivity(intent);
    }

}