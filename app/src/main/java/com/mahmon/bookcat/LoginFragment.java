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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    // Firebase authorisation instance
    private FirebaseAuth mAuth;
    // View elements
    private EditText userEmail;
    private EditText userPassword;

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
        Button btnLogin = fragViewLogin.findViewById(R.id.btn_login);
        TextView clickToSignUp = fragViewLogin.findViewById(R.id.click_to_sign_up);
        // Attach listener to the button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO add validation
                final String email = userEmail.getText().toString().trim();
                final String password = userPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getContext(), "Enter email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Call login method
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

    // Action to take if user logged in
    private void checkLoggedIn(FirebaseUser user) {
        if (user != null) {
            gotoWelcomeScreen();
        }
    }

    private void logIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            gotoWelcomeScreen();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Method to create new fragment and replace in the fragment container
    public void gotoRegisterScreen() {
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new RegisterFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Method to create new fragment and replace in the fragment container
    public void gotoWelcomeScreen() {
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new WelcomeFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}