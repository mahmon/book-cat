package com.mahmon.bookcat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class RegisterFragment extends Fragment {

    EditText ET_USER_NAME, ET_USER_EMAIL, ET_USER_PASSWORD, ET_USER_PASS_CONF;
    String user_name, user_email, user_password, user_pass_conf;

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
        ET_USER_PASS_CONF = view.findViewById(R.id.user_password_confirm);
    }

    public void userReg(View view) {
        user_name = ET_USER_NAME.getText().toString();
        user_email = ET_USER_EMAIL.getText().toString();
        user_password = ET_USER_PASSWORD.getText().toString();
        user_pass_conf = ET_USER_PASS_CONF.getText().toString();
        String method = "register";
        //BackgroundTask backgroundTask = new BackgroundTask(this);
        //backgroundTask.execute(method, name, user_name, user_pass);
        //finish();
    }


}