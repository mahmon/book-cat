package com.mahmon.bookcat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterFragment extends Fragment {

    EditText ET_USER_NAME, ET_USER_EMAIL, ET_USER_PASSWORD, ET_USER_PASS_CONF;
    String user_name, user_email, user_password, user_pass_conf;
    Button btn_register;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ET_USER_NAME = view.findViewById(R.id.user_name);
        ET_USER_EMAIL = view.findViewById(R.id.user_email);
        ET_USER_PASSWORD = view.findViewById(R.id.user_password);
        ET_USER_PASS_CONF = view.findViewById(R.id.user_pass_conf);
        btn_register = view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userReg();
            }
        });
    }

    public void userReg() {
        user_name = ET_USER_NAME.getText().toString().trim();
        user_email = ET_USER_EMAIL.getText().toString().trim();
        user_password = ET_USER_PASSWORD.getText().toString().trim();
        user_pass_conf = ET_USER_PASS_CONF.getText().toString().trim();
        if(user_name !=null && !user_name.isEmpty()) {
            if(user_email !=null && !user_email.isEmpty()) {
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
                    if(user_password !=null && !user_password.isEmpty()) {
                        if (user_pass_conf !=null && !user_pass_conf.isEmpty()) {
                            if(user_password.equals(user_pass_conf)) {
                                // TODO register
                            } else {
                                Toast.makeText(getActivity(), "Passwords do not match, try again", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please confirm password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter valid email address", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please enter email address", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Please enter name", Toast.LENGTH_SHORT).show();
        }
/*
        if(user_password.equals(user_pass_conf)) {
            Toast.makeText(getActivity(), "Hooray", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Passwords do not match.", Toast.LENGTH_LONG).show();
        }
*/

        //BackgroundTask backgroundTask = new BackgroundTask(this);
        //backgroundTask.execute(method, name, user_name, user_pass);
    }

}