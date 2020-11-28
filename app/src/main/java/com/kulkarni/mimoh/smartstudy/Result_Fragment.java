package com.kulkarni.mimoh.smartstudy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Space;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Result_Fragment extends Fragment {

    private Spinner spinner_testSelect,spinner_subject,spinner_period;
    private ArrayList<String> list_subject,list_subject_id;
    ArrayAdapter arrayAdapter;
    private String test_type,limit,subject;
    private int load_Check = 0;
    private LayoutInflater layoutInflater;
    private TextView TVnothing;
    private LinearLayout LLheadings,LLparentResult;
    private SecurePreferences securePreferences;
//    private HashMap<String,String> hashMap;
    private final static String result_url = "https://smartstudym3.000webhostapp.com/smart_study/results.php";
    private final static String sub_chap_url = "https://smartstudym3.000webhostapp.com/smart_study/test.php";
    private static final int timeout = 10000;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        spinner_testSelect = view.findViewById(R.id.spinner_testSelect);
        spinner_subject = view.findViewById(R.id.spinner_subject);
        spinner_period = view.findViewById(R.id.spinner_period);
        LLheadings = view.findViewById(R.id.LLheadings);
        LLparentResult = view.findViewById(R.id.LLparentResult);
//        hashMap = LocalDatabase.getInstance(getContext()).getUserData();
        securePreferences = new SecurePreferences(getContext());
        list_subject_id = new ArrayList<>();
        list_subject = new ArrayList<>();
        layoutInflater = (LayoutInflater) Objects.requireNonNull(getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TVnothing = view.findViewById(R.id.TVnothing);

        TVnothing.setVisibility(View.GONE);
        LLheadings.setVisibility(View.GONE);
        list_subject.add("All");
        list_subject_id.add("0");

        test_type = limit = subject = "0";

        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list_subject);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_subject.setAdapter(arrayAdapter);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting Subjects");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, sub_chap_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    LLheadings.setVisibility(View.VISIBLE);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("resultarray");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        list_subject_id.add(jsonObject1.getString("sub_id"));
                        list_subject.add(jsonObject1.getString("sub_name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("insti_id",securePreferences.getString("instiid", null));
                map.put("class",securePreferences.getString("class", null));
                map.put("medium",securePreferences.getString("medium", null));
                map.put("sub_id","");
                map.put("chap_id","");
                map.put("test_id","");
                map.put("sub_name","");
                map.put("multiple_test","");
                map.put("type","get_sub");
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

////        trial start
//        LLheadings.setVisibility(View.VISIBLE);
//        list_subject_id.add("1");
//        list_subject.add("Trial subject 1");
//
//        list_subject_id.add("2");
//        list_subject.add("Trial subject 2");
//
//        list_subject_id.add("3");
//        list_subject.add("Trial subject 3");
////        trial end


        spinner_subject.setSelection(0);
        spinner_testSelect.setSelection(0);
        spinner_period.setSelection(0);

        spinner_listners();
        load_results(test_type,limit,subject);

        return view;

    }

    private void spinner_listners() {

        spinner_testSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (load_Check > 0) {
                    switch (position) {
                        case 0:
                            test_type = "0";
                            break;

                        case 1:
                            test_type = "1";
                            break;

                        case 2:
                            test_type = "2";
                            break;
                    }
                    load_Check = 0;
                    load_results(test_type, limit, subject);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_period.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (load_Check > 0) {
                    switch (position) {
                        case 0:
                            limit = "0";
                            break;

                        case 1:
                            limit = "1";
                            break;

                        case 2:
                            limit = "2";
                            break;
                    }
                    load_Check = 0;
                    load_results(test_type, limit, subject);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (load_Check > 0) {
                    if (position == 0) {
                        subject = "0";
                    } else {
                        subject = list_subject_id.get(position);
                    }
                    load_Check = 0;
                    load_results(test_type, limit, subject);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void load_results(final String test_type, final String limit, final String subject){

        LLparentResult.removeAllViews();
        LLparentResult.removeAllViewsInLayout();

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching Your Result...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, result_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (load_Check == 0) {
//                    Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                    load_Check = 1;
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("resultarray");
                        if (jsonArray.length() == 0) TVnothing.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);

                            LinearLayout parent = new LinearLayout(getContext());
                            parent.setLayoutParams(layoutParams);
                            parent.setOrientation(LinearLayout.VERTICAL);

                            LinearLayout child = new LinearLayout(getContext());
                            child.setLayoutParams(layoutParams);
                            child.setOrientation(LinearLayout.HORIZONTAL);
                            child.setWeightSum(3);

                            LinearLayout.LayoutParams TVparams1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                            TVparams1.weight = (float) 1.1;

                            LinearLayout.LayoutParams TVparams2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                            TVparams2.weight = (float) 0.8;

                            TextView textView_one = new TextView(getContext());
                            textView_one.setLayoutParams(TVparams1);
                            textView_one.setTextSize(20);
                            textView_one.setGravity(Gravity.CENTER);
                            textView_one.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView_one.setText(jsonObject1.getString("sub_name"));
//                        textView_one.setText(response);

                            TextView textView_two = new TextView(getContext());
                            textView_two.setLayoutParams(TVparams1);
                            textView_two.setTextSize(20);
                            textView_two.setGravity(Gravity.CENTER);
                            textView_two.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView_two.setText(jsonObject1.getString("marks"));

                            TextView textView_three = new TextView(getContext());
                            textView_three.setLayoutParams(TVparams2);
                            textView_three.setTextSize(20);
                            textView_three.setGravity(Gravity.CENTER);
                            textView_three.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView_three.setText(R.string.details);
                            textView_three.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    View view = layoutInflater.inflate(R.layout.result_details_layout, null);

                                    TextView TVresult_subject, TVresult_testname, TVresult_marks, TVresult_testtime, TVresult_testchapters;
                                    TVresult_subject = view.findViewById(R.id.TVresult_subject);
                                    TVresult_testname = view.findViewById(R.id.TVresult_testname);
                                    TVresult_marks = view.findViewById(R.id.TVresult_marks);
                                    TVresult_testtime = view.findViewById(R.id.TVresult_testtime);
                                    TVresult_testchapters = view.findViewById(R.id.TVresult_testchapters);

                                    try {
                                        TVresult_subject.setText(jsonObject1.getString("sub_name"));
                                        TVresult_testname.setText(jsonObject1.getString("test_name"));
                                        String marks = jsonObject1.getString("marks") + "/" + jsonObject1.getString("max_marks");
                                        TVresult_marks.setText(marks);
                                        TVresult_testtime.setText(jsonObject1.getString("test_date_time"));
                                        JSONArray jsonArray1 = jsonObject1.getJSONArray("test_chapters");
                                        StringBuilder chapters = new StringBuilder();
                                        String comma = "";
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                                            chapters.append(comma).append(jsonObject2.getString("chapter_name"));
                                            comma = ", ";
                                        }
                                        TVresult_testchapters.setText(chapters.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    builder.setView(view);
                                    builder.create().show();
                                }
                            });

                            Space space = new Space(getContext());
                            LinearLayout.LayoutParams space_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    5);
                            space.setLayoutParams(space_params);

                            child.addView(textView_one);
                            child.addView(textView_two);
                            child.addView(textView_three);

                            parent.addView(child);
                            parent.addView(space);

                            LLparentResult.addView(parent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
                String message = "Something Happened";
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
                map.put("mobileno",securePreferences.getString("mobileno", null));
                map.put("test_id","");
                map.put("sub_id","");
                map.put("chapter_id","");
                map.put("marks","");
                map.put("max_marks","");
                map.put("limit",limit);
                map.put("test_type",test_type);
                map.put("subject",subject);
                map.put("type","get_result");
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

////        trial start
//        trialShowData();
////        trial end
    }

//    private void trialShowData(){
//        for (int i=1;i<=5;i++){
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//
//            LinearLayout parent = new LinearLayout(getContext());
//            parent.setLayoutParams(layoutParams);
//            parent.setOrientation(LinearLayout.VERTICAL);
//
//            LinearLayout child = new LinearLayout(getContext());
//            child.setLayoutParams(layoutParams);
//            child.setOrientation(LinearLayout.HORIZONTAL);
//            child.setWeightSum(3);
//
//            LinearLayout.LayoutParams TVparams1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
//            TVparams1.weight = (float) 1.1;
//
//            LinearLayout.LayoutParams TVparams2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
//            TVparams2.weight = (float) 0.8;
//
//            TextView textView_one = new TextView(getContext());
//            textView_one.setLayoutParams(TVparams1);
//            textView_one.setTextSize(20);
//            textView_one.setGravity(Gravity.CENTER);
//            textView_one.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            textView_one.setText("Trial subject" + String.valueOf(i));
//
//            TextView textView_two = new TextView(getContext());
//            textView_two.setLayoutParams(TVparams1);
//            textView_two.setTextSize(20);
//            textView_two.setGravity(Gravity.CENTER);
//            textView_two.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            textView_two.setText(String.valueOf(i));
//
//            TextView textView_three = new TextView(getContext());
//            textView_three.setLayoutParams(TVparams2);
//            textView_three.setTextSize(20);
//            textView_three.setGravity(Gravity.CENTER);
//            textView_three.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            textView_three.setText(R.string.details);
//            final String finalI = String.valueOf(i);
//            textView_three.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                    View view = layoutInflater.inflate(R.layout.result_details_layout,null);
//
//                    TextView TVresult_subject,TVresult_testname,TVresult_marks,TVresult_testtime,TVresult_testchapters;
//                    TVresult_subject = view.findViewById(R.id.TVresult_subject);
//                    TVresult_testname = view.findViewById(R.id.TVresult_testname);
//                    TVresult_marks = view.findViewById(R.id.TVresult_marks);
//                    TVresult_testtime = view.findViewById(R.id.TVresult_testtime);
//                    TVresult_testchapters = view.findViewById(R.id.TVresult_testchapters);
//
//                        TVresult_subject.setText("Trial subject " + finalI);
//                        TVresult_testname.setText("Trail test " + finalI);
//                        String marks = String.valueOf(finalI) + "/" +"5";
//                        TVresult_marks.setText(marks);
//                        TVresult_testtime.setText("01/01/2018 10.00.00 AM");
//                        String chapters = "Trial chapter 1,Trail chapter 2,Trial chapter 3";
//                        TVresult_testchapters.setText(chapters);
//
//                    builder.setView(view);
//                    builder.create().show();
//                }
//            });
//
//            Space space = new Space(getContext());
//            LinearLayout.LayoutParams space_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    5);
//            space.setLayoutParams(space_params);
//
//            child.addView(textView_one);
//            child.addView(textView_two);
//            child.addView(textView_three);
//
//            parent.addView(child);
//            parent.addView(space);
//
//            LLparentResult.addView(parent);
//        }
//    }
}