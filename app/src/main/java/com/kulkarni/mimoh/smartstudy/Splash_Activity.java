package com.kulkarni.mimoh.smartstudy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Splash_Activity extends AppCompatActivity {

    ImageView IVsplash;
//    private Boolean successLogin;
    final private static String register_url = "https://smartstudym3.000webhostapp.com/smart_study/login_register.php";
    private static final String NO_CONNECTION_MESSAGE = "Looks like there is no connection. Please check you are connected to the network and restart the app.";
    private static final String NO_CONNECTION_TITLE = "No Connection";
    private static final int timeout = 10000,timeout_intent = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IVsplash = new ImageView(this);
        setContentView(IVsplash);
        IVsplash.setBackgroundResource(R.drawable.splashscreen);
//        successLogin = false;

        if (!isNetworkConnected()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(NO_CONNECTION_TITLE);
            builder.setMessage(NO_CONNECTION_MESSAGE);
            builder.setCancelable(false);
            builder.setIcon(R.drawable.action_logout);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    finishAffinity();
                    System.exit(0);
                }
            });
            builder.create().show();
        }

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(Splash_Activity.this,response,Toast.LENGTH_LONG).show();
                if (!response.equals("error")){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray =  jsonObject.getJSONArray("resultarray");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                        SecurePreferences securePreferences = new SecurePreferences(Splash_Activity.this);
                        securePreferences.edit().putString("mobileno",jsonObject1.getString("mobileno")).apply();
                        securePreferences.edit().putString("instiid",jsonObject1.getString("instiid")).apply();
                        securePreferences.edit().putString("instiname",jsonObject1.getString("instiname")).apply();
                        securePreferences.edit().putString("name",jsonObject1.getString("name")).apply();
                        securePreferences.edit().putString("class",jsonObject1.getString("class")).apply();
                        securePreferences.edit().putString("batch",jsonObject1.getString("batch")).apply();
                        securePreferences.edit().putString("medium",jsonObject1.getString("medium")).apply();
                        securePreferences.edit().putString("rollno",jsonObject1.getString("rollno")).apply();
                        securePreferences.edit().putString("bdate",jsonObject1.getString("bdate")).apply();
                        securePreferences.edit().putString("email",jsonObject1.getString("email")).apply();
                        securePreferences.edit().putString("gender",jsonObject1.getString("gender")).apply();
                        securePreferences.edit().putString("address",jsonObject1.getString("address")).apply();


                        if(!securePreferences.getString("mobileno","empty").equals("empty") &&
                                !securePreferences.getString("instiid","empty").equals("empty") &&
                                !securePreferences.getString("instiname","empty").equals("empty") &&
                                !securePreferences.getString("name","empty").equals("empty") &&
                                !securePreferences.getString("class","empty").equals("empty") &&
                                !securePreferences.getString("batch","empty").equals("empty") &&
                                !securePreferences.getString("medium","empty").equals("empty") &&
                                !securePreferences.getString("rollno","empty").equals("empty") &&
                                !securePreferences.getString("bdate","empty").equals("empty") &&
                                !securePreferences.getString("email","empty").equals("empty") &&
                                !securePreferences.getString("gender","empty").equals("empty") &&
                                !securePreferences.getString("address","empty").equals("empty")){

                            Toast.makeText(Splash_Activity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Splash_Activity.this, Homepage_Activity.class);
                            intent.putExtra("rememberMe", true);
                            startActivity(intent);

                        }
                        else Toast.makeText(Splash_Activity.this, "Login Failed.Contact Superior", Toast.LENGTH_SHORT).show();

//                        HashMap<String,String> hashMap = new HashMap<>();
//                        hashMap.put("mobileno",jsonObject1.getString("mobileno"));
//                        hashMap.put("instiid",jsonObject1.getString("instiid"));
//                        hashMap.put("instiname",jsonObject1.getString("instiname"));
//                        hashMap.put("name",jsonObject1.getString("name"));
//                        hashMap.put("class",jsonObject1.getString("class"));
//                        hashMap.put("batch",jsonObject1.getString("batch"));
//                        hashMap.put("medium",jsonObject1.getString("medium"));
//                        hashMap.put("rollno",jsonObject1.getString("rollno"));
//                        hashMap.put("bdate",jsonObject1.getString("bdate"));
//                        hashMap.put("email",jsonObject1.getString("email"));
//                        hashMap.put("gender",jsonObject1.getString("gender"));
//                        hashMap.put("address",jsonObject1.getString("address"));
//
//                        LocalDatabase.getInstance(Splash_Activity.this).deleteUser();
//                        successLogin = LocalDatabase.getInstance(Splash_Activity.this).addUser(hashMap);
//                        if (successLogin) {
//                            Toast.makeText(Splash_Activity.this, "Login Success", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(Splash_Activity.this, Homepage_Activity.class);
//                            intent.putExtra("rememberMe", true);
//                            startActivity(intent);
//                        }
//                        else Toast.makeText(Splash_Activity.this, "Login Failed.Contact Superior", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // Actions to do after 2 seconds
                            startActivity(new Intent(Splash_Activity.this,Login_Activity.class));
                        }
                    }, timeout_intent);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                String message = "oops";
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
                Toast.makeText(Splash_Activity.this,message,Toast.LENGTH_LONG).show();
                startActivity(new Intent(Splash_Activity.this,Login_Activity.class));
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("mobileno","");
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
                map.put("mac_address",new Register_Fragment().getMacAddr());
                map.put("islogin","");
                map.put("type","remember");
                return map;
            }
        };
        stringRequest1.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest1);


////        trial start
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                // Actions to do after 2 seconds
//                startActivity(new Intent(Splash_Activity.this,Login_Activity.class));
//            }
//        }, timeout_intent);
////        trial end
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        finish();
        finishAffinity();
        System.exit(0);
        super.onBackPressed();
    }
}
