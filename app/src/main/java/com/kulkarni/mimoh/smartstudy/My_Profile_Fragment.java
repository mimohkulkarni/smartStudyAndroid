package com.kulkarni.mimoh.smartstudy;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class My_Profile_Fragment extends Fragment implements View.OnClickListener{

    TextView TVmobileno,TVname;
    private boolean isSelected;
    private EditText ETdisplay_class,ETdisplay_batch,ETdisplay_rollno,ETdisplay_bdate,ETdisplay_email,ETdisplay_gender,ETdisplay_address;
    Button BTNmobile_change,BTNpassword_change;
    private Button BTNprofile_change;
//    private HashMap<String,String> hashMap;
    SecurePreferences securePreferences;
    private Calendar mycalendar = Calendar.getInstance();
    private static final int timeout = 10000;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_profile, container, false);

        TVmobileno = view.findViewById(R.id.TVmobileno);
        TVname = view.findViewById(R.id.TVname);
        ETdisplay_class = view.findViewById(R.id.ETdisplay_class);
        ETdisplay_batch = view.findViewById(R.id.ETdisplay_batch);
        ETdisplay_rollno = view.findViewById(R.id.ETdisplay_rollno);
        ETdisplay_bdate = view.findViewById(R.id.ETdisplay_bdate);
        ETdisplay_email = view.findViewById(R.id.ETdisplay_email);
        ETdisplay_gender = view.findViewById(R.id.ETdisplay_gender);
        ETdisplay_address = view.findViewById(R.id.ETdisplay_address);
        BTNmobile_change = view.findViewById(R.id.BTNmobile_change);
        BTNpassword_change = view.findViewById(R.id.BTNpassword_change);
        BTNprofile_change = view.findViewById(R.id.BTNprofile_change);

        isSelected = false;
//        hashMap = LocalDatabase.getInstance(getContext()).getUserData();
        securePreferences = new SecurePreferences(getContext());

        TVmobileno.setText(securePreferences.getString("mobileno", null));
        TVname.setText(securePreferences.getString("name", null));
        ETdisplay_rollno.setText(securePreferences.getString("rollno", null));
        ETdisplay_bdate.setText(securePreferences.getString("bdate", null));
        ETdisplay_email.setText(securePreferences.getString("email", null));
        ETdisplay_gender.setText(securePreferences.getString("gender", null));
        ETdisplay_class.setText(securePreferences.getString("class", null));
        ETdisplay_batch.setText(securePreferences.getString("batch", null));
        ETdisplay_address.setText(securePreferences.getString("address", null));

        ETdisplay_class.setEnabled(false);
        ETdisplay_batch.setEnabled(false);
        ETdisplay_rollno.setEnabled(false);
        ETdisplay_bdate.setEnabled(false);
        ETdisplay_email.setEnabled(false);
        ETdisplay_gender.setEnabled(false);
        ETdisplay_address.setEnabled(false);

        BTNmobile_change.setOnClickListener(this);
        BTNpassword_change.setOnClickListener(this);
        BTNprofile_change.setOnClickListener(this);

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mycalendar.set(Calendar.YEAR,year);
                mycalendar.set(Calendar.MONTH,month);
                mycalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String myFormat = "dd-MMM-yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
                ETdisplay_bdate.setText(simpleDateFormat.format(mycalendar.getTime()));
            }
        };

        ETdisplay_bdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                ETdisplay_bdate.setText(null);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()),dateSetListener,mycalendar.get(Calendar.YEAR),
                        mycalendar.get(Calendar.MONTH),mycalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(date.getTime());
                datePickerDialog.show();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.BTNmobile_change:
                Toast.makeText(getContext(),"Contact your superiors to change mobile number",Toast.LENGTH_LONG).show();
                break;

            case R.id.BTNpassword_change:
                Intent intent_pw = new Intent(getContext(),Login_Activity.class);
                intent_pw.putExtra("pw_fragment",true);
                intent_pw.putExtra("mobileno",securePreferences.getString("mobileno", null));
//                LocalDatabase.getInstance(getContext()).deleteUser();
                securePreferences.edit().delete_user();
                startActivity(intent_pw);
                break;

            case R.id.BTNprofile_change:
                if (!isSelected) {
                    ETdisplay_class.setEnabled(true);
                    ETdisplay_batch.setEnabled(true);
                    ETdisplay_rollno.setEnabled(true);
                    ETdisplay_bdate.setEnabled(true);
                    ETdisplay_email.setEnabled(true);
                    ETdisplay_gender.setEnabled(true);
                    ETdisplay_address.setEnabled(true);
                    BTNprofile_change.setText(R.string.done);
                    isSelected = true;
                }
                else {
                    isSelected = false;
                    final String str_class = ETdisplay_class.getText().toString();
                    final String str_batch = ETdisplay_batch.getText().toString();
                    final String str_rollno = ETdisplay_rollno.getText().toString();
                    final String bdate = ETdisplay_bdate.getText().toString();
                    Date old_date = new Date();
                    try {
                        old_date = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).parse(bdate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
                    final String str_bdate = newFormat.format(old_date);


                    final String str_email = ETdisplay_email.getText().toString();
                    final String str_gender = ETdisplay_gender.getText().toString();
                    final String str_address = ETdisplay_address.getText().toString();

                    if (!str_class.isEmpty() && !str_batch.isEmpty() && !str_rollno.isEmpty() && !str_bdate.isEmpty() && !str_email.isEmpty()
                            && !str_gender.isEmpty() && !str_address.isEmpty()) {
                        final String update_url = "https://smartstudym3.000webhostapp.com/smart_study/login_register.php";

                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Updating data...");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, update_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                                Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                if (response.equals("ok")) {
                                    ETdisplay_class.setEnabled(false);
                                    ETdisplay_batch.setEnabled(false);
                                    ETdisplay_rollno.setEnabled(false);
                                    ETdisplay_bdate.setEnabled(false);
                                    ETdisplay_email.setEnabled(false);
                                    ETdisplay_gender.setEnabled(false);
                                    ETdisplay_address.setEnabled(false);
                                    BTNprofile_change.setText(R.string.change);

                                    securePreferences.edit().update_values(securePreferences.getString("mobileno", null),
                                            str_class,str_batch,str_rollno,str_bdate,str_email,str_gender,str_address);

//                                    LocalDatabase.getInstance(getContext()).updateAllValues(securePreferences.getString("mobileno", null),
//                                            str_class,str_batch,str_rollno,str_bdate,str_email,str_gender,str_address);

//                                    HashMap<String,String> hashMap1 = LocalDatabase.getInstance(getContext()).getUserData();

                                    ETdisplay_rollno.setText(securePreferences.getString("rollno", null));
                                    ETdisplay_bdate.setText(securePreferences.getString("bdate", null));
                                    ETdisplay_email.setText(securePreferences.getString("email", null));
                                    ETdisplay_gender.setText(securePreferences.getString("gender", null));
                                    ETdisplay_class.setText(securePreferences.getString("class", null));
                                    ETdisplay_batch.setText(securePreferences.getString("batch", null));
                                    ETdisplay_address.setText(securePreferences.getString("address", null));

                                    Toast.makeText(getContext(),"Data Updated Successfully",Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getContext(), "Update not successful", Toast.LENGTH_SHORT).show();
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
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> map = new HashMap<>();
                                map.put("mobileno",securePreferences.getString("mobileno", null));
                                map.put("password","");
                                map.put("instiid","");
                                map.put("name","");
                                map.put("class",str_class);
                                map.put("medium","");
                                map.put("batch",str_batch);
                                map.put("roll_no",str_rollno);
                                map.put("bdate",str_bdate);
                                map.put("email",str_email);
                                map.put("gender",str_gender);
                                map.put("address",str_address);
                                map.put("mac_address","");
                                map.put("islogin","");
                                map.put("type","update");
                                return map;
                            }
                        };
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
                    }
                    else Toast.makeText(getContext(),"Please provide all values",Toast.LENGTH_LONG).show();
                }

////                trial start
//                Toast.makeText(getContext(),"This button has been disabled in trial version",Toast.LENGTH_LONG).show();
////                trial end

                break;
        }
    }
}