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

public class LoginFragment extends Fragment {

    Button btn_goto_register_screen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_goto_register_screen = view.findViewById(R.id.btn_goto_register_screen);
        btn_goto_register_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRegisterScreen();
            }
        });
    }

    public void gotoRegisterScreen() {
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new RegisterFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}