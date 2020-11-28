package com.kulkarni.mimoh.smartstudy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Login_Activity extends AppCompatActivity {

    private Login_Fragment login_fragment = new Login_Fragment();
    private TextView TVlogin_title;
    private Boolean pw_fragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

////        trial start
//        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//        builder1.setTitle("Trial Application");
//        builder1.setMessage("This is trial application.Kindly use username as '1234567890' and password as '123'");
//        builder1.setCancelable(false);
//        builder1.setIcon(R.drawable.action_logout);
//        builder1.setPositiveButton("OK", null);
//        builder1.create().show();
////        trial end

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pw_fragment = bundle.getBoolean("pw_fragment");
        }

        if(pw_fragment){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, new Forget_Password_Fragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, login_fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        TVlogin_title = findViewById(R.id.login_title);

    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        if (login_fragment.isVisible()) TVlogin_title.setText(R.string.account_signup);
        else TVlogin_title.setText(R.string.account_login);
    }

    @Override
    public void onBackPressed() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!login_fragment.isVisible()){
            transaction.replace(R.id.fragment_container, login_fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else {
            finish();
            finishAffinity();
            System.exit(0);
        }
    }
}
