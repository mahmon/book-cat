package com.mahmon.bookcat;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, loginFragment).commit();
    }

    public void gotoRegisterScreen(View view) {
        RegisterFragment registerFragment = new RegisterFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, registerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
