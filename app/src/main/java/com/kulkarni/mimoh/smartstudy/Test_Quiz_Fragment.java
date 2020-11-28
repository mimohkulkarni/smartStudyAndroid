package com.kulkarni.mimoh.smartstudy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Test_Quiz_Fragment extends Fragment implements View.OnClickListener{

    ImageButton IBquiz,IBtest,IBresult,IBstarttest;
    ListView LVtest;
    private Select_Quiz_Fragment select_quiz_fragment = new Select_Quiz_Fragment();
    private Result_Fragment result_fragment = new Result_Fragment();
    LayoutInflater layoutInflater;
    View view1;
    private String selectedItem;
    private List<String> list_test,list_test_id;
    private static final String test_check_url = "https://smartstudym3.000webhostapp.com/smart_study/test.php";
    private AlertDialog.Builder builder;
    private static final int timeout = 10000;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_quiz, container, false);

        IBquiz = view.findViewById(R.id.IBquiz);
        IBtest = view.findViewById(R.id.IBtest);
        IBresult = view.findViewById(R.id.IBresult);

        layoutInflater = (LayoutInflater) Objects.requireNonNull(getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view1 = layoutInflater.inflate(R.layout.test_dialog,null);
        LVtest = view1.findViewById(R.id.LVtest);
        IBstarttest = view1.findViewById(R.id.IBstarttest);

        selectedItem = "";

        builder = new AlertDialog.Builder(getContext());
        builder.setView(view1);
        builder.create();

        list_test = new ArrayList<>();
        list_test_id = new ArrayList<>();

        final ListAdapter listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_single_choice, list_test);
        LVtest.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        LVtest.setAdapter(listAdapter);

        LVtest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = list_test_id.get(position);
            }
        });

        IBquiz.setOnClickListener(this);
        IBtest.setOnClickListener(this);
        IBresult.setOnClickListener(this);
        IBstarttest.setOnClickListener(this);

        return view;
    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        transaction.replace(R.id.fragment_homepage_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.IBtest:
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Fetching your pending tests...");
                progressDialog.show();
                progressDialog.setCancelable(false);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, test_check_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();
                        list_test.clear();
                        list_test_id.clear();
                        progressDialog.dismiss();
                        if (!response.equals("error")){
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("resultarray");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    list_test.add(jsonObject1.getString("sub_name"));
                                    list_test_id.add(jsonObject1.getString("test_id"));
                                }
                                builder.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        String message = "Error";
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
                        map.put("medium","");
                        map.put("class","");
                        map.put("insti_id","");
                        map.put("sub_id","");
                        map.put("test_id","");
                        map.put("chap_id","0");
                        map.put("multiple_test","0");
                        map.put("type","check_test");
                        return map;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

////                trial start
//                list_test.clear();
//                list_test_id.clear();
//
//                list_test.add("Trial subject 1");
//                list_test_id.add("1");
//
//                list_test.add("Trial subject 2");
//                list_test_id.add("2");
//
//                list_test.add("Trial subject 3");
//                list_test_id.add("3");
//
//                builder.show();
////                trial end

                break;

            case R.id.IBquiz:
                changeFragment(select_quiz_fragment);
                break;

            case R.id.IBresult:
                changeFragment(result_fragment);
                break;

            case R.id.IBstarttest:
                if (!selectedItem.isEmpty()) {
//                Toast.makeText(getContext(),selectedItem,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), Quiz_Activity.class);
                    intent.putExtra("sub_id", "0");
                    intent.putExtra("chap_id","0");
                    intent.putExtra("test_id", selectedItem);
                    Objects.requireNonNull(getContext()).startActivity(intent);
                }
                else Toast.makeText(getContext(),"Select any one of the test",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
