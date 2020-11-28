package com.kulkarni.mimoh.smartstudy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Login_Fragment extends Fragment implements View.OnClickListener {
    TextView TVforgot_password,TVregister,TVlogin_title;
    private EditText ETusername,ETpassword;
    Button login;
    private String username,password,islogin;
    private Boolean remember_me;
    Boolean successLogin;
    private Switch rememberMe;
    private Register_Fragment register_fragment = new Register_Fragment();
    final private static String register_url = "https://smartstudym3.000webhostapp.com/smart_study/login_register.php";
    private static final int timeout = 10000;
    private final Forget_Password_Fragment forget_password_fragment = new Forget_Password_Fragment();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        TVforgot_password = view.findViewById(R.id.TVforgot_pass);
        TVregister = view.findViewById(R.id.TVregister);
        ETusername = view.findViewById(R.id.ETusername);
        ETpassword = view.findViewById(R.id.ETpassword);
        login = view.findViewById(R.id.btnlogin);
        rememberMe = view.findViewById(R.id.remember_me);
        TVlogin_title = view.findViewById(R.id.login_title);
        rememberMe.setChecked(true);
        login.setOnClickListener(this);
        TVforgot_password.setOnClickListener(this);
        TVregister.setOnClickListener(this);
        ETusername.setError(null);
        ETpassword.setError(null);

        remember_me = successLogin = false;

////        trial start
//        ETusername.setText("1234567890");
//        ETpassword.setText("123");
////        trial end

        return view;
    }

    @Override
    public void onClick(View v) {
        c

        switch (v.getId()){
            case R.id.btnlogin:
                username = ETusername.getText().toString();
                password = ETpassword.getText().toString();
                if (username.isEmpty()){
                    ETusername.setError("Enter Username");
                    break;
                }
                if (password.isEmpty()){
                    ETpassword.setError("Enter Password");
                    break;
                }

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
                progressDialog.setCancelable(false);

                if (rememberMe.isChecked()){
                    islogin = "1";
                    remember_me = true;
                }
                else{
                    islogin = "0";
                    remember_me = false;
                }
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, register_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (!response.equals("error")){
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray =  jsonObject.getJSONArray("resultarray");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String mac_address = jsonObject1.getString("mac_address");
                                String check_mac_address = register_fragment.getMacAddr();
                                if (mac_address.equals(check_mac_address)){
                                    saveData(jsonObject1,remember_me);
                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                                    builder.setTitle("Oops");
                                    builder.setMessage("Looks like you are trying login from another device. If you wish to change device contact admin");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK",null);
                                    builder.create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            Toast.makeText(getContext(),"Incorrect combination of username & password",Toast.LENGTH_SHORT).show();
                            ETusername.setError(null);
                            ETpassword.setError(null);
                        }
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
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<>();
                        map.put("mobileno",username);
                        map.put("password",password);
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
                        map.put("islogin",islogin);
                        map.put("type","login");
                        return map;
                    }
                };
                stringRequest1.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest1);

////                trial start
//                progressDialog.dismiss();
//                if (username.equals("1234567890") && password.equals("123")){
//                    trialsaveData();
//                }
//                else{
//                    Toast.makeText(getContext(),"Incorrect combination of username & password",Toast.LENGTH_SHORT).show();
//                    ETusername.setError(null);
//                    ETpassword.setError(null);
//                }
////                trial end

                ETusername.setError(null);
                ETpassword.setError(null);
                break;

            case R.id.TVregister:
                transaction.replace(R.id.fragment_container, register_fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case R.id.TVforgot_pass:
                if (!ETusername.getText().toString().isEmpty() || ETusername.getText().toString().length() == 10) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("pw_fragment", false);
                    bundle.putString("mobileno",ETusername.getText().toString());
                    forget_password_fragment.setArguments(bundle);
                    transaction.replace(R.id.fragment_container, forget_password_fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else Toast.makeText(getContext(),"Please enter valid mobile number",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void saveData(JSONObject jsonObject1,boolean remember) throws JSONException {
        SecurePreferences securePreferences = new SecurePreferences(getContext());
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

            Toast.makeText(getContext(), "Login Success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), Homepage_Activity.class);
            intent.putExtra("rememberMe", remember);
            startActivity(intent);

        }
        else Toast.makeText(getContext(), "Login Failed.Contact Superior", Toast.LENGTH_SHORT).show();



//        HashMap<String,String> hashMap = new HashMap<>();
//        hashMap.put("mobileno",jsonObject1.getString("mobileno"));
//        hashMap.put("instiid",jsonObject1.getString("instiid"));
//        hashMap.put("instiname",jsonObject1.getString("instiname"));
//        hashMap.put("name",jsonObject1.getString("name"));
//        hashMap.put("class",jsonObject1.getString("class"));
//        hashMap.put("batch",jsonObject1.getString("batch"));
//        hashMap.put("medium",jsonObject1.getString("medium"));
//        hashMap.put("rollno",jsonObject1.getString("rollno"));
//        hashMap.put("bdate",jsonObject1.getString("bdate"));
//        hashMap.put("email",jsonObject1.getString("email"));
//        hashMap.put("gender",jsonObject1.getString("gender"));
//        hashMap.put("address",jsonObject1.getString("address"));

//        LocalDatabase.getInstance(getContext()).deleteUser();
//        successLogin = LocalDatabase.getInstance(getContext()).addUser(hashMap);
//        if (successLogin) {
//            Toast.makeText(getContext(), "Login Success", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getContext(), Homepage_Activity.class);
//            intent.putExtra("rememberMe", remember);
//            startActivity(intent);
//        }
//        else Toast.makeText(getContext(), "Login Failed.Contact Superior", Toast.LENGTH_SHORT).show();
    }

//    private void trialsaveData() {
//        HashMap<String,String> hashMap = new HashMap<>();
//        hashMap.put("mobileno","1234567890");
//        hashMap.put("instiid","2");
//        hashMap.put("instiname","Trial Academy");
//        hashMap.put("name","Trial Name");
//        hashMap.put("class","10");
//        hashMap.put("batch","Morning");
//        hashMap.put("medium","Semi-English");
//        hashMap.put("rollno","59");
//        hashMap.put("bdate","01-01-2000");
//        hashMap.put("email","trial@trial.com");
//        hashMap.put("gender","Male");
//        hashMap.put("address","Trial Address");
//
//        LocalDatabase.getInstance(getContext()).deleteUser();
//        successLogin = LocalDatabase.getInstance(getContext()).addUser(hashMap);
//        if (successLogin) {
//            Toast.makeText(getContext(), "Login Success", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getContext(), Homepage_Activity.class);
//            intent.putExtra("rememberMe", true);
//            startActivity(intent);
//        }
//        else Toast.makeText(getContext(), "Login Error.Contact Admin", Toast.LENGTH_SHORT).show();
//    }
}
