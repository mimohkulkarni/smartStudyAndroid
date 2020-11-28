package com.kulkarni.mimoh.smartstudy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Video_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItems> listItems;
//    private HashMap<String,String> hashMap;
    private  SecurePreferences securePreferences;
    private Spinner spinner_sub,spinner_chap;
    private List<String> list_sub ,list_chap,list_sub_id,list_chap_id,listItems_id;
    ArrayAdapter<String> arrayAdapter_sub,arrayAdapter_chap;
    private String sub_id,chap_id,type;
    private static final String notes_url = "https://smartstudym3.000webhostapp.com/smart_study/notes.php";
    private static final String sub_chap_url = "https://smartstudym3.000webhostapp.com/smart_study/test.php";
    private static final int timeout = 10000;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_important_notes, container, false);

        spinner_sub = view.findViewById(R.id.spinner_sub_pdf);
        spinner_chap = view.findViewById(R.id.spinner_chap_pdf);
        recyclerView = view.findViewById(R.id.recyclerView_pdf);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listItems = new ArrayList<>();
        list_sub = new ArrayList<>();
        list_chap = new ArrayList<>();
        list_sub_id = new ArrayList<>();
        list_chap_id = new ArrayList<>();
        listItems_id = new ArrayList<>();
        sub_id = chap_id = "0";
        type = "";

//        hashMap = LocalDatabase.getInstance(getContext()).getUserData();
        securePreferences = new SecurePreferences(getContext());

        list_sub.add("All");
        list_chap.add("All");
        list_sub_id.add("0");
        list_chap_id.add("0");
        listItems.clear();
        listItems_id.clear();

        arrayAdapter_sub = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, list_sub);
        arrayAdapter_chap = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list_chap);

        arrayAdapter_sub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter_chap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_sub.setAdapter(arrayAdapter_sub);
        spinner_chap.setAdapter(arrayAdapter_chap);
        spinner_sub.setSelection(0);
        spinner_chap.setSelection(0);

////        trial start
//        list_sub_id.add("1");
//        list_sub.add("Trial subject 1");
//        list_sub_id.add("2");
//        list_sub.add("Trial subject 2");
//        list_sub_id.add("3");
//        list_sub.add("Trial subject 3");
////        trial end

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, sub_chap_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("resultarray");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        list_sub_id.add(jsonObject1.getString("sub_id"));
                        list_sub.add(jsonObject1.getString("sub_name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("insti_id",securePreferences.getString("instiid",null));
                map.put("class",securePreferences.getString("class",null));
                map.put("medium",securePreferences.getString("medium",null));
                map.put("sub_id","");
                map.put("test_id","");
                map.put("chap_id","");
                map.put("multiple_test","");
                map.put("type","get_sub");
                return map;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest1);
        stringRequest1.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        loadRecyclerView(list_sub_id.get(0),list_chap_id.get(0));
        spinner_listners();

        return view;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null;
    }

    void loadRecyclerView(final String sub_id1, final String chap_id1){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading your notes...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, notes_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                listItems.clear();
                listItems_id.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray =  jsonObject.getJSONArray("resultarray");
                    for (int i = 0; i < jsonArray.length(); i++ ) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        ListItems item = new ListItems(jsonObject1.getString("note_name"),
                                "Uploaded On " + jsonObject1.getString("upload_time"));
                        listItems.add(item);
                        listItems_id.add(jsonObject1.getString("note_id"));
                    }
                    adapter = new MyAdapter(listItems,getContext());
                    recyclerView.setAdapter(adapter);
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
                map.put("insti_id",securePreferences.getString("instiid",null));
                map.put("class",securePreferences.getString("class",null));
                map.put("medium",securePreferences.getString("medium",null));
                map.put("is_video","0");
                map.put("note_id","");
                map.put("sub_id",sub_id1);
                map.put("chapter_id",chap_id1);
                map.put("note_name","");
                map.put("type","get_notes");
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

////        trial start
//        listItems.clear();
//        listItems_id.clear();
//
//        for (int i = 1;i<=5;i++) {
//            ListItems item = new ListItems("Trial Notes " + i, "Uploaded On 01/01/2018");
//            listItems.add(item);
//            listItems_id.add(String.valueOf(i));
//        }
//        adapter = new MyAdapter(listItems,getContext());
//        recyclerView.setAdapter(adapter);
////        trial end

    }

    void load_chap_spinner(final String sub_id1){
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, sub_chap_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("resultarray1");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        list_chap_id.add(jsonObject1.getString("chapter_id"));
                        list_chap.add(jsonObject1.getString("chapter_name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("insti_id",securePreferences.getString("instiid",null));
                map.put("class",securePreferences.getString("class",null));
                map.put("medium",securePreferences.getString("medium",null));
                map.put("sub_id",sub_id1);
                map.put("test_id","");
                map.put("chap_id","");
                map.put("multiple_test","1");
                map.put("type","get_chap");
                return map;
            }
        };
        stringRequest1.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest1);

////        trial start
//        list_chap_id.add("1");
//        list_chap.add("Trial chapter 1");
//
//        list_chap_id.add("2");
//        list_chap.add("Trial chapter 2");
//
//        list_chap_id.add("3");
//        list_chap.add("Trial chapter 3");
////        trial end

    }

    private void spinner_listners() {
        spinner_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    list_chap.clear();
                    list_chap_id.clear();
                    sub_id = list_sub_id.get(position);
                    list_chap.add("All");
                    list_chap_id.add("0");
                    load_chap_spinner(sub_id);
                    loadRecyclerView(sub_id,chap_id);
                    spinner_chap.setSelection(0);
                    spinner_chap.setEnabled(true);
                }
                else{
                    loadRecyclerView("0","0");
                    spinner_chap.setSelection(0);
                    spinner_chap.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_chap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    chap_id = list_chap_id.get(position);
                    loadRecyclerView(sub_id,chap_id);
                }
                else{
                    loadRecyclerView(sub_id,"0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onStop() {
        list_sub_id.clear();
        list_chap_id.clear();
        list_sub.clear();
        list_chap.clear();
        listItems.clear();
        listItems_id.clear();
        super.onStop();
    }

    public class ListItems
    {
        private String head;
        private String desc;

        private ListItems(String head, String desc) {
            this.head = head;
            this.desc = desc;
        }

        public String getHead() {
            return head;
        }

        public String getDesc() {
            return desc;
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<ListItems> listItems;
        private Context context;

        MyAdapter(List<ListItems> listItems, Context context) {
            this.listItems = listItems;
            this.context = context;
        }

        @NonNull
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_items, parent, false);
            return new MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyAdapter.ViewHolder holder, int position) {
            final int pos = position;
            ListItems listItem = listItems.get(position);

            holder.TVpdfname.setText(listItem.getHead());
            holder.TVpdfinfo.setText(listItem.getDesc());

            holder.LLRVitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isNetworkConnected()) {
                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Loading your note...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        final String halfNameUrl = "https://smartstudym3.000webhostapp.com/notes/";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, notes_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("resultarray");
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                    sub_id = jsonObject1.getString("subject_id");
                                    chap_id = jsonObject1.getString("chapter_id");
                                    type = jsonObject1.getString("note_type");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                String halfNameFile = securePreferences.getString("instiid",null) +
                                        securePreferences.getString("class",null) +
                                        securePreferences.getString("medium","oops").toLowerCase().substring(0,1)
                                        + sub_id + chap_id + "." + type;
                                if (!sub_id.equals("0") && !chap_id.equals("0") && !type.isEmpty()) {
                                    Intent intent = new Intent(context, WebView_Activity.class);
                                    intent.putExtra("url", halfNameUrl + halfNameFile);
                                    context.startActivity(intent);
                                }
                                else Toast.makeText(getContext(),"Unknown error occurred.Contact admin",Toast.LENGTH_SHORT).show();

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
                                Map<String, String> map = new HashMap<>();
                                map.put("insti_id", "");
                                map.put("class", "");
                                map.put("medium", "");
                                map.put("is_video", "0");
                                map.put("note_id", listItems_id.get(pos));
                                map.put("sub_id", "");
                                map.put("chapter_id", "");
                                map.put("note_name", "");
                                map.put("type", "get_notes_info");
                                return map;
                            }
                        };
                        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

////                        trial srart
//                        Intent intent = new Intent(context, WebView_Activity.class);
//                        intent.putExtra("url", "https://smartstudym3.000webhostapp.com/notes/free-trial.gif");
//                        context.startActivity(intent);
////                        trial end
                    }
                    else {
                        Toast.makeText(getContext(),"No internet detected",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView TVpdfname, TVpdfinfo;
            public LinearLayout LLRVitem;

            private ViewHolder(View itemView) {
                super(itemView);
                TVpdfname = itemView.findViewById(R.id.TVpdfname);
                TVpdfinfo = itemView.findViewById(R.id.TVpdfinfo);
                LLRVitem = itemView.findViewById(R.id.LLRVitem);

            }
        }
    }
}