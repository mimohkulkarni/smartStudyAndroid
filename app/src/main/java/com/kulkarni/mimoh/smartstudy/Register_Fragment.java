package com.kulkarni.mimoh.smartstudy;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class Register_Fragment extends Fragment implements View.OnClickListener {
    private TextInputLayout TIL1,TIL2,TIL3,TIL4,TIL5,TIL6,TIL7,TIL8,TIL9,TIL10,TIL11,TIL12,TIL13;
    private TextView TVlogin,TVcredits,TVpersonal_info,TVtution_info;
    private EditText ETusername,ETpasswordfirst,ETpasswordsecond,ETfullname,ETclass,ETbatch,ETbdate,ETroll_no,ETemail,ETaddress;
    private Spinner spinner_gender,spinner_medium,spinner_institute;
    private Button register;
    private String registerusername,passwordfirst,fullname,class_name,batch_no,bdate,roll_no,email,address,gender,mac_Address,
            medium,insti_id;
    String passwordsecond;
    private int count;
    private Calendar mycalendar = Calendar.getInstance();
    ArrayAdapter arrayAdapter_name;
    private static final String register_url = "https://smartstudym3.000webhostapp.com/smart_study/login_register.php";
    private static List<String> list_insti_id,list_insti_name;
    private static final int timeout = 10000;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        TIL1 = view.findViewById(R.id.TIL1);
        TIL2 = view.findViewById(R.id.TIL2);
        TIL3 = view.findViewById(R.id.TIL3);
        TIL4 = view.findViewById(R.id.TIL4);
        TIL5 = view.findViewById(R.id.TIL5);
        TIL6 = view.findViewById(R.id.TIL6);
        TIL7 = view.findViewById(R.id.TIL7);
        TIL8 = view.findViewById(R.id.TIL8);
        TIL9 = view.findViewById(R.id.TIL9);
        TIL10 = view.findViewById(R.id.TIL10);
        TIL11 = view.findViewById(R.id.TIL11);
        TIL12 = view.findViewById(R.id.TIL12);
        TIL13 = view.findViewById(R.id.TIL13);

        TVlogin = view.findViewById(R.id.TVlogin);
        TVcredits = view.findViewById(R.id.TVcredits);
        TVpersonal_info = view.findViewById(R.id.TVpersonal_info);
        TVtution_info = view.findViewById(R.id.TVtution_info);
        ETusername = view.findViewById(R.id.ETregisterusername);
        ETpasswordfirst = view.findViewById(R.id.ETpasswordfirst);
        ETpasswordsecond = view.findViewById(R.id.ETpasswordsecond);
        ETfullname = view.findViewById(R.id.ETfullname);
        ETclass = view.findViewById(R.id.ETclass);
        ETbatch = view.findViewById(R.id.ETbatch);
        ETbdate = view.findViewById(R.id.ETbdate);
        ETroll_no = view.findViewById(R.id.ETroll_no);
        ETemail = view.findViewById(R.id.ETemail);
        ETaddress = view.findViewById(R.id.ETaddress);
        register = view.findViewById(R.id.btnregister);
        spinner_gender = view.findViewById(R.id.spinner_gender);
        spinner_medium = view.findViewById(R.id.spinner_medium);
        spinner_institute = view.findViewById(R.id.spinner_institute);

        list_insti_id = new ArrayList<>();
        list_insti_name = new ArrayList<>();
        list_insti_id.add("0");
        list_insti_name.add("Select Institute");
        spinner_institute.setSelection(0);

        arrayAdapter_name = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list_insti_name);
        arrayAdapter_name.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_institute.setAdapter(arrayAdapter_name);

        TVlogin.setOnClickListener(this);
        register.setOnClickListener(this);
        ETusername.setError(null);
        ETpasswordfirst.setError(null);
        ETpasswordsecond.setError(null);
        ETfullname.setError(null);
        ETclass.setError(null);
        ETbatch.setError(null);
        ETemail.setError(null);
        ETroll_no.setError(null);
        ETaddress.setError(null);
        spinner_gender.setSelection(0);
        spinner_medium.setSelection(0);

        TVpersonal_info.setVisibility(View.GONE);
        TIL4.setVisibility(View.GONE);
        TIL5.setVisibility(View.GONE);
        TIL6.setVisibility(View.GONE);
        TIL7.setVisibility(View.GONE);
        TIL8.setVisibility(View.GONE);
        TVtution_info.setVisibility(View.GONE);
        TIL9.setVisibility(View.GONE);
        TIL10.setVisibility(View.GONE);
        TIL11.setVisibility(View.GONE);
        TIL12.setVisibility(View.GONE);
        TIL13.setVisibility(View.GONE);

        count = 0;

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mycalendar.set(Calendar.YEAR,year);
                mycalendar.set(Calendar.MONTH,month);
                mycalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String myFormat = "yyyy/MM/dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
                ETbdate.setText(simpleDateFormat.format(mycalendar.getTime()));
            }
        };

        ETbdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                ETbdate.setText(null);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()),dateSetListener,mycalendar.get(Calendar.YEAR),
                        mycalendar.get(Calendar.MONTH),mycalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(date.getTime());
                datePickerDialog.show();
            }
        });

        mac_Address = getMacAddr();

        return view;
    }

    public String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnregister:
                registerusername = ETusername.getText().toString();
                passwordfirst = ETpasswordfirst.getText().toString();
                passwordsecond = ETpasswordsecond.getText().toString();
                fullname = ETfullname.getText().toString();
                class_name = ETclass.getText().toString();
                batch_no = ETbatch.getText().toString();
                medium = spinner_medium.getSelectedItem().toString();
                bdate = ETbdate.getText().toString();
                roll_no = ETroll_no.getText().toString();
                email = ETemail.getText().toString();
                address = ETaddress.getText().toString();
                gender = spinner_gender.getSelectedItem().toString();
                insti_id = list_insti_id.get(spinner_institute.getSelectedItemPosition());
                if (count == 0) {
                    if (registerusername.isEmpty()) {
                        ETusername.setError("Enter Mobile No");
                        break;
                    }
                    if (passwordfirst.isEmpty()) {
                        ETpasswordfirst.setError("Enter Password");
                        break;
                    }
                    if (passwordsecond.isEmpty()) {
                        ETpasswordsecond.setError("Enter Password");
                        break;
                    }
                    if (!passwordfirst.equals(passwordsecond)) {
                        Toast.makeText(getContext(), "Passwords does not match", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (registerusername.length() == 10 && passwordfirst.length() >= 3) {
                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Please Wait...");
                        progressDialog.show();
                        progressDialog.setCancelable(false);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, register_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                if (!response.equals("oops")) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONArray jsonArray =  jsonObject.getJSONArray("resultarray");
                                        for(int i=0; i<jsonArray.length();i++){
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            list_insti_id.add(jsonObject1.getString("insti_id"));
                                            list_insti_name.add(jsonObject1.getString("insti_name"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    count = 1;
                                    register.setText(R.string.next);
                                    TIL1.setVisibility(View.GONE);
                                    TIL2.setVisibility(View.GONE);
                                    TIL3.setVisibility(View.GONE);
                                    TVlogin.setVisibility(View.GONE);
                                    TVcredits.setVisibility(View.GONE);
                                    TVpersonal_info.setVisibility(View.VISIBLE);
                                    TIL4.setVisibility(View.VISIBLE);
                                    TIL5.setVisibility(View.VISIBLE);
                                    TIL6.setVisibility(View.VISIBLE);
                                }
                                else Toast.makeText(getContext(),"Mobile number already registered",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> map = new HashMap<>();
                                map.put("mobileno",registerusername);
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
                                map.put("type","check");
                                return map;
                            }
                        };
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

////                        trial start
//                        list_insti_id.add("1");
//                        list_insti_name.add("Trial academy 1");
//                        list_insti_id.add("2");
//                        list_insti_name.add("Trial academy 2");
//                        list_insti_id.add("3");
//                        list_insti_name.add("Trial academy 3");
//
//                        count = 1;
//                        register.setText(R.string.next);
//                        TIL1.setVisibility(View.GONE);
//                        TIL2.setVisibility(View.GONE);
//                        TIL3.setVisibility(View.GONE);
//                        TVlogin.setVisibility(View.GONE);
//                        TVcredits.setVisibility(View.GONE);
//                        TVpersonal_info.setVisibility(View.VISIBLE);
//                        TIL4.setVisibility(View.VISIBLE);
//                        TIL5.setVisibility(View.VISIBLE);
//                        TIL6.setVisibility(View.VISIBLE);
////                        trial end

                    } else {
                        Toast.makeText(getContext(), "Enter valid mobile no and password minimum of 6 characters", Toast.LENGTH_LONG).show();
                    }
                    break;
                }

                if (count == 1){
                    spinner_gender.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.gender));
                    if (fullname.isEmpty()) {
                        ETfullname.setError("Enter Your Name");
                        break;
                    }
                    if (bdate.isEmpty()) {
                        ETbdate.setError("Select Birth date");
                        break;
                    }
                    if (gender.equals("Select Gender")) {
                        spinner_gender.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.gender_error));
                        break;
                    }
                    TIL4.setVisibility(View.GONE);
                    TIL5.setVisibility(View.GONE);
                    TIL6.setVisibility(View.GONE);
                    TIL7.setVisibility(View.VISIBLE);
                    TIL8.setVisibility(View.VISIBLE);
                    count = 2;
                    break;
                }

                if (count == 2){
                    if (email.isEmpty()) {
                        ETemail.setError("Enter Class Name");
                        break;
                    }
                    if (address.isEmpty()) {
                        ETaddress.setError("Enter Batch No");
                        break;
                    }
                    TVpersonal_info.setVisibility(View.GONE);
                    TIL7.setVisibility(View.GONE);
                    TIL8.setVisibility(View.GONE);
                    TVtution_info.setVisibility(View.VISIBLE);
                    TIL9.setVisibility(View.VISIBLE);
                    TIL10.setVisibility(View.VISIBLE);
                    TIL11.setVisibility(View.VISIBLE);
                    count = 3;
                    break;
                }

                if (count == 3){

                    spinner_institute.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.gender));

                    if (class_name.isEmpty()) {
                        ETclass.setError("Enter Class Name");
                        break;
                    }
                    if (batch_no.isEmpty()) {
                        ETbatch.setError("Enter Batch No");
                        break;
                    }
                    if (insti_id.equals("0")) {
                        spinner_institute.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.gender_error));
                        break;
                    }
                    TIL9.setVisibility(View.GONE);
                    TIL10.setVisibility(View.GONE);
                    TIL11.setVisibility(View.GONE);
                    TIL12.setVisibility(View.VISIBLE);
                    TIL13.setVisibility(View.VISIBLE);
                    count = 4;
                    register.setText(R.string.confirm);
                    break;
                }
                if (count == 4){
                    spinner_medium.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.gender));
                    if (medium.equals("Select Medium")) {
                        spinner_medium.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.gender_error));
                        break;
                    }
                    if (roll_no.isEmpty()) {
                        ETbatch.setError("Enter Batch No");
                        break;
                    }
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.show();
                    progressDialog.setCancelable(true);

                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, register_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                            if (response.equals("done")){
                                count = 0;
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Request Submitted");
                                builder.setMessage("Registration successful.Pending confirmation.");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Login_Fragment nextFrag= new Login_Fragment();
                                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.fragment_container, nextFrag)
                                                .addToBackStack(null)
                                                .commit();
                                    }
                                });
                                builder.create().show();
                            }
                            else Toast.makeText(getContext(),"Error,something happened",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<>();
                            map.put("mobileno",registerusername);
                            map.put("password",passwordfirst);
                            map.put("instiid",String.valueOf(insti_id));
                            map.put("name",fullname);
                            map.put("class",class_name);
                            map.put("medium",medium);
                            map.put("batch",batch_no);
                            map.put("roll_no",roll_no);
                            map.put("bdate",bdate);
                            map.put("email",email);
                            map.put("gender",gender);
                            map.put("address",address);
                            map.put("mac_address",mac_Address);
                            map.put("islogin","0");
                            map.put("type","register");
                            return map;
                        }
                    };
                    stringRequest1.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest1);
                }

////                trial start
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("Request Submitted");
//                builder.setMessage("Registration successful.Pending confirmation.");
//                builder.setCancelable(false);
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(getContext(),Login_Activity.class));
//                        }
//                });
//                builder.create().show();
////                trial end

                break;

            case R.id.TVlogin:
                Login_Fragment nextFrag= new Login_Fragment();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag)
                        .addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                break;
        }
    }
}
