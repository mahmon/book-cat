package com.mahmon.bookcat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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
        // Get current user from firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
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

    // Method to register user with Firebase authentication
    public void registerUser() {
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
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

                            //String user_id = mAuth.getCurrentUser().getUid();

                            // Sign in success
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                        } else {
                            // Sign in fails
                            Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}

