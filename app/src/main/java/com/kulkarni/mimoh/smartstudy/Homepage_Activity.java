package com.kulkarni.mimoh.smartstudy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Homepage_Activity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private TextView TVcurrpage;
    private int doubleBackToExitPressed = 1;
    private boolean rememberMe = false,is_test = false;
    private Test_Quiz_Fragment test_quiz_fragment = new Test_Quiz_Fragment();
    private Homepage_Fragment homepage_fragment = new Homepage_Fragment();
    private My_Profile_Fragment my_profile_fragment = new My_Profile_Fragment();
    private Important_Notes_Fragment important_notes_fragment = new Important_Notes_Fragment();
    private Video_Fragment video_fragment = new Video_Fragment();
//    private HashMap<String,String> hashMap;
    private SecurePreferences securePreferences;
    private static final int timeout = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TVcurrpage = findViewById(R.id.TVcurrpage);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            rememberMe = bundle.getBoolean("rememberMe");
            is_test = bundle.getBoolean("test");
        }

        mBottomNavigationView = findViewById(R.id.bottom_nav);

        final Menu menu = mBottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

//        hashMap = LocalDatabase.getInstance(getApplicationContext()).getUserData();
        securePreferences = new SecurePreferences(this);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.nav1_video:
                        change_fragment(video_fragment,0);
                        TVcurrpage.setText(R.string.imp_video);
                        break;

                    case R.id.nav1_imp_notes:
                        change_fragment(important_notes_fragment,1);
                        TVcurrpage.setText(R.string.imp_notes);
                        break;

                    case R.id.nav1_home:
                        change_fragment(homepage_fragment,2);
                        TVcurrpage.setText(R.string.home);
                        break;

                    case R.id.nav1_quiz:
                        change_fragment(test_quiz_fragment,3);
                        TVcurrpage.setText(R.string.quiz_menu);
                        break;

                    case R.id.nav1_personal_info:
                        change_fragment(my_profile_fragment,4);
                        TVcurrpage.setText(R.string.my_profile);
                        break;
                }
                return false;
            }
        });

        if (is_test){
            change_fragment(test_quiz_fragment,3);
        }
        else {
            change_fragment(homepage_fragment,2);
        }
    }

    @Override
    public void onBackPressed() {
        if (homepage_fragment.isVisible()){
            homepage_exit_logout();
        }
        else if (test_quiz_fragment.isVisible()){
            change_fragment(homepage_fragment,2);
        }
        else if (my_profile_fragment.isVisible()){
            change_fragment(homepage_fragment,2);
        }
        else {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void change_fragment(Fragment fragment,int index){
        final Menu menu = mBottomNavigationView.getMenu();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_homepage_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        MenuItem menuItem = menu.getItem(index);
        menuItem.setChecked(true);
    }

    private void homepage_exit_logout(){
        if (!rememberMe) {
            if (doubleBackToExitPressed == 2) {
                do_logout();
            }
            else {
                doubleBackToExitPressed ++;
                Toast.makeText(this, "Pressing back again will logout", Toast.LENGTH_SHORT).show();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressed = 1;
                }
            }, 2000);
        }
        else {
            if (doubleBackToExitPressed == 2) {
                finish();
                finishAffinity();
                System.exit(0);
            } else {
                doubleBackToExitPressed++;
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressed = 1;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    public void do_logout(){
        final String register_url = "https://smartstudym3.000webhostapp.com/smart_study/login_register.php";
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing you off...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        StringRequest stringRequest_homepage = new StringRequest(Request.Method.POST, register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response.equals("ok")) {
                    securePreferences.edit().delete_user();
                    startActivity(new Intent(Homepage_Activity.this, Login_Activity.class));
                    Toast.makeText(Homepage_Activity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(Homepage_Activity.this, "Logout Failed", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                String message = "Error.Contact admin";
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(Homepage_Activity.this,message,Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("mobileno",securePreferences.getString("mobileno",null));
                map.put("password","");
                map.put("instiid","");
                map.put("name","");
                map.put("class","");
                map.put("medium","");
                map.put("batch","");
                map.put("roll_no","");
                map.put("bdate","");
                map.put("email","");
                map.put("gender","");
                map.put("address","");
                map.put("mac_address","");
                map.put("islogin","");
                map.put("type","logout");
                return map;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest_homepage);
        stringRequest_homepage.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        LocalDatabase.getInstance(this).deleteUser();

////        trial start
//          Toast.makeText(Homepage_Activity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
//          startActivity(new Intent(Homepage_Activity.this, Login_Activity.class));
//          LocalDatabase.getInstance(this).deleteUser();
////          trial end
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Toast.makeText(this,"Section Under Development",Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_logout:
                do_logout();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
