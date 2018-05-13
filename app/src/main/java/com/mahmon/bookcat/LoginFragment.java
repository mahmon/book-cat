package com.mahmon.bookcat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create fragViewLogin instance
        View fragViewLogin = inflater.inflate(R.layout.fragment_login, container, false);
        // Create button instance and link to xml
        Button btnLogin = fragViewLogin.findViewById(R.id.btn_login);
        // Create textView instance and link to xml
        TextView clickToSignUp = fragViewLogin.findViewById(R.id.click_to_sign_up);
        // Attach listener to the button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
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

    // Method to create new fragment and replace in the fragment container
    public void gotoRegisterScreen() {
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new RegisterFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}