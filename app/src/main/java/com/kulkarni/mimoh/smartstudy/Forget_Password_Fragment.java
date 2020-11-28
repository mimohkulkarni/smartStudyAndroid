package com.kulkarni.mimoh.smartstudy;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Forget_Password_Fragment extends Fragment {
    private EditText ETotp,ETnewpass,ETnewpass1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    static int otp1,otp2;
    private static String finalotp;
    private String message,mobileno;
    static String sms_url = "https://smartstudym3.000webhostapp.com/smart_study/sms.php";
//    HashMap<String,String> hashMap;
    private TextInputLayout TILpw1,TILpw2;
    private ImageButton IBsubmit;
    private static final int timeout = 10000;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);

        Intent intent_login = new Intent(getContext(),Login_Activity.class);
        intent_login.putExtra("pw_fragment",false);

//        hashMap = LocalDatabase.getInstance(getContext()).getUserData();

        Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle == null){
            bundle = this.getArguments();
            if(bundle != null){
                mobileno = bundle.getString("mobileno");
            }
        }
        else {
            mobileno = bundle.getString("mobileno");
        }

        ETotp = view.findViewById(R.id.ETotp);
        ETnewpass = view.findViewById(R.id.ETnewpassword);
        ETnewpass1 = view.findViewById(R.id.ETnewpassword1);
        TILpw1 = view.findViewById(R.id.TILpw1);
        TILpw2 = view.findViewById(R.id.TILpw2);
        IBsubmit = view.findViewById(R.id.IBsubmit);

        TILpw1.setVisibility(View.GONE);
        TILpw2.setVisibility(View.GONE);
        IBsubmit.setVisibility(View.GONE);

        checkAndRequestPermissions();

        otp1 = new Random().nextInt(400 - 100) + 100;
        otp2 = new Random().nextInt(700 - 400) + 400;
        //int Result = new Random().nextInt(High-Low) + Low;

        finalotp = String.valueOf(otp1) + String.valueOf(otp2);

////        trial start
//        finalotp = "123456";
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setMessage("Use 123456 as trial otp");
//        builder.setTitle("Message");
//        builder.create().show();
////        trial end


        message = finalotp + " is your OTP for resetting password of Smart Study account.";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, sms_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(),"OTP has been sent to your registered mobile number.",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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
                map.put("mobileno",mobileno);
                map.put("instiid","");
                map.put("message",message);
                map.put("newpass","");
                map.put("type","otp");
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


        ETotp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ETotp.setError(null);
                if (s.length() == 6){
                    ETotp.setEnabled(false);
                    if (ETotp.getText().toString().equals(finalotp)){
                        TILpw1.setVisibility(View.VISIBLE);
                        TILpw2.setVisibility(View.VISIBLE);
                        IBsubmit.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(getContext(),"Wrong OTP",Toast.LENGTH_SHORT).show();
                        ETotp.setText("");
                    }
                }
                else ETotp.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        IBsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ETnewpass.getText().toString().isEmpty()){
                    ETnewpass.setError("Please enter new password");
                    return;
                }
                if (ETnewpass1.getText().toString().isEmpty()){
                    ETnewpass1.setError("Please enter new password again");
                    return;
                }
                if (ETnewpass.getText().toString().equals(ETnewpass1.getText().toString())) {


////                    trial start
//                    Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getContext(), Login_Activity.class));
////                    trial end

                    message = "Your password was successfully changed";
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);

                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, sms_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.equals("okk")) {
                                Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(), Login_Activity.class));
                            }
                            else {
                                Toast.makeText(getContext(), "Password not changed. Contact admin", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(), Login_Activity.class));
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
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
                            map.put("mobileno",mobileno);
                            map.put("instiid","");
                            map.put("message",message);
                            map.put("newpass",ETnewpass.getText().toString());
                            map.put("type","msg");
                            return map;
                        }
                    };
                    stringRequest1.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest1);
                }
            }
        });

        return view;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                String finalmessage = String.valueOf(Integer.parseInt(message.replaceAll("[\\D]", "")));
                if (finalmessage.isEmpty()) finalmessage = "";
                ETotp.setText(finalmessage);
            }
        }
    };

    private void checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance( getContext()).unregisterReceiver(receiver);
    }
}
