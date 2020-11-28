package com.kulkarni.mimoh.smartstudy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Space;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Quiz_Activity extends AppCompatActivity implements View.OnClickListener{

    private TextView TVquestion,TVtimer,TVscore;
    TextView TVtestname;
    private Button BTNanswer1,BTNanswer2,BTNanswer3,BTNanswer4,BTNnext;
    private ImageView IVquestion,IVanswer1,IVanswer2,IVanswer3,IVanswer4;
    private ProgressBar progressBar;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning,tosave;
    long time = 60000;
    private int count_que,score;
    private LinearLayout LLresult;
    private ScrollView SVresult,SVquestions;
    private FloatingActionButton FBexit;
    private String sub_id,selected_ans,test_id,chapter_id;
    String title;
    private ArrayList<String> question,ans_a,ans_b,ans_c,ans_d,ans_correct;//que_id;
//    private HashMap<String,String> hashMap;
    private SecurePreferences securePreferences;
    private HashMap<String,Bitmap> imageHashMap;
    private AlertDialog.Builder builder,builder1;
    private static final int timeout = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        count_que = score = 0;
        sub_id = selected_ans = test_id = "0";
        chapter_id = "";
        tosave = true;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sub_id = bundle.getString("sub_id");
            test_id = bundle.getString("test_id");
            chapter_id = bundle.getString("chap_id");
        }
        else {
            Intent intent = new Intent(Quiz_Activity.this,Homepage_Activity.class);
            intent.putExtra("test",true);
            startActivity(intent);
        }

//        Toast.makeText(this,sub_id,Toast.LENGTH_LONG).show();
//        Toast.makeText(this,test_id,Toast.LENGTH_LONG).show();
//        Toast.makeText(this,chapter_id  ,Toast.LENGTH_LONG).show();

//        hashMap = LocalDatabase.getInstance(this).getUserData();
        securePreferences = new SecurePreferences(Quiz_Activity.this);

        SharedPreferences myans = this.getSharedPreferences("myans", MODE_PRIVATE);
        SharedPreferences.Editor editor = myans.edit();
        editor.clear();
        editor.apply();

        TVtimer = findViewById(R.id.TVtimer);
        TVquestion = findViewById(R.id.TVquestion);
        TVtestname = findViewById(R.id.TVtestname);
        BTNanswer1 = findViewById(R.id.BTNanswer1);
        BTNanswer2 = findViewById(R.id.BTNanswer2);
        BTNanswer3 = findViewById(R.id.BTNanswer3);
        BTNanswer4 = findViewById(R.id.BTNanswer4);
        BTNnext = findViewById(R.id.BTNnext);
        progressBar = findViewById(R.id.progressbar);
        SVquestions = findViewById(R.id.SVquestion);
        LLresult = findViewById(R.id.LLresult);
        SVresult = findViewById(R.id.SVResult);
        TVscore = findViewById(R.id.TVscore);
        FBexit = findViewById(R.id.FBexit);
        IVquestion = findViewById(R.id.IVquestion);
        IVanswer1 = findViewById(R.id.IVanswer1);
        IVanswer2 = findViewById(R.id.IVanswer2);
        IVanswer3 = findViewById(R.id.IVanswer3);
        IVanswer4 = findViewById(R.id.IVanswer4);

        IVquestion.setVisibility(View.GONE);
        IVanswer1.setVisibility(View.GONE);
        IVanswer2.setVisibility(View.GONE);
        IVanswer3.setVisibility(View.GONE);
        IVanswer4.setVisibility(View.GONE);

        IVquestion.setBackgroundResource(R.drawable.no_image);
        IVanswer1.setBackgroundResource(R.drawable.no_image);
        IVanswer2.setBackgroundResource(R.drawable.no_image);
        IVanswer3.setBackgroundResource(R.drawable.no_image);
        IVanswer4.setBackgroundResource(R.drawable.no_image);

        BTNanswer1.setBackgroundResource(R.drawable.ansbuttonclickfalse);
        BTNanswer2.setBackgroundResource(R.drawable.ansbuttonclickfalse);
        BTNanswer3.setBackgroundResource(R.drawable.ansbuttonclickfalse);
        BTNanswer4.setBackgroundResource(R.drawable.ansbuttonclickfalse);

//        que_id = new ArrayList<>();
        question = new ArrayList<>();
        ans_a = new ArrayList<>();
        ans_b = new ArrayList<>();
        ans_c = new ArrayList<>();
        ans_d = new ArrayList<>();
        ans_correct = new ArrayList<>();
        imageHashMap = new HashMap<>();

        get_questions(String.valueOf(test_id));

        progressBar.setProgress(0);
        progressBar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

        BTNanswer1.setOnClickListener(this);
        BTNanswer2.setOnClickListener(this);
        BTNanswer3.setOnClickListener(this);
        BTNanswer4.setOnClickListener(this);
        BTNnext.setOnClickListener(this);
        FBexit.setOnClickListener(this);

        title = "Test";
        TVtestname.setText(title);
        SVresult.setVisibility(View.GONE);
        FBexit.setVisibility(View.GONE);

//        get_questions("1");

    }

    private void get_questions(final String test_id1){

////        trial start
////        que_id.add("1");
//        question.add("Select your state");
//        ans_a.add("Maharashtra");
//        ans_b.add("Karnataka");
//        ans_c.add("Madhya Pradesh");
//        ans_d.add("none of these");
//        ans_correct.add("1");
//
//
////        que_id.add("2");
//        question.add("Select your country");
//        ans_a.add("Pakistan");
//        ans_b.add("Maharashtra");
//        ans_c.add("United Kingdom");
//        ans_d.add("India");
//        ans_correct.add("4");
//
//
////        que_id.add("3");
//        question.add("Plants can breath...");
//        ans_a.add("True");
//        ans_b.add("False");
//        ans_c.add("None of the above");
//        ans_d.add("");
//        ans_correct.add("1");
//
//
////        que_id.add("4");
//        question.add("Which is smallest type of matter");
//        ans_a.add("Atom");
//        ans_b.add("Tissue");
//        ans_c.add("Matter");
//        ans_d.add("none of these");
//        ans_correct.add("1");
//
////        que_id.add("5");
//        question.add("Which is biggest type of matter");
//        ans_a.add("Atom");
//        ans_b.add("Tissue");
//        ans_c.add("Atmosphere");
//        ans_d.add("none of these");
//        ans_correct.add("3");
//
//        progressBar.setMax(5);
//        imageHashMap.clear();
//
////        trial end

        builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Oops!!!");
        builder1.setMessage("No questions found.Contact your teacher.");
        builder1.setCancelable(false);
        builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tosave = false;
                Intent intent = new Intent(Quiz_Activity.this,Homepage_Activity.class);
                intent.putExtra("test",true);
                startActivity(intent);
            }
        });

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!!!");
        if (!test_id1.equals("0")) builder.setMessage("Do not leave this page until test is complete. You can give test only once");
        else builder.setMessage("Do not leave this page until quiz is complete.Else marks will be counted as '0'(zero)");
        builder.setCancelable(false);
        builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TVquestion.setText(question.get(count_que));
                BTNanswer1.setText(ans_a.get(count_que));
                BTNanswer2.setText(ans_b.get(count_que));
                BTNanswer3.setText(ans_c.get(count_que));
                BTNanswer4.setText(ans_d.get(count_que));
                if (imageHashMap.containsKey("q0")){
                    IVquestion.setVisibility(View.VISIBLE);
                    IVquestion.setImageBitmap(imageHashMap.get("q0"));
                }
                if (imageHashMap.containsKey("a0")){
                    IVquestion.setVisibility(View.VISIBLE);
                    IVquestion.setImageBitmap(imageHashMap.get("a0"));
                }
                if (imageHashMap.containsKey("b0")){
                    IVquestion.setVisibility(View.VISIBLE);
                    IVquestion.setImageBitmap(imageHashMap.get("b0"));
                }
                if (imageHashMap.containsKey("c0")){
                    IVquestion.setVisibility(View.VISIBLE);
                    IVquestion.setImageBitmap(imageHashMap.get("c0"));
                }
                if (imageHashMap.containsKey("d0")){
                    IVquestion.setVisibility(View.VISIBLE);
                    IVquestion.setImageBitmap(imageHashMap.get("d0"));
                }
                startTimer();
            }
        });

////        trial srart
//        builder.create().show();
////        trial end


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting super easy questions for you...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        String question_url = "https://smartstudym3.000webhostapp.com/smart_study/test.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, question_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(Quiz_Activity.this,response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray =  jsonObject.getJSONArray("resultarray");
                    if (jsonArray.length() > 1) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                        que_id.add(jsonObject1.getString("id"));
                            question.add(jsonObject1.getString("question"));
                            ans_a.add(jsonObject1.getString("ans_a"));
                            ans_b.add(jsonObject1.getString("ans_b"));
                            ans_c.add(jsonObject1.getString("ans_c"));
                            ans_d.add(jsonObject1.getString("ans_d"));
                            ans_correct.add(jsonObject1.getString("ans_correct"));

                            String ans_image = jsonObject1.getString("image_info");
                            if (ans_image.substring(0, 1).equals("1"))
                                getImage(jsonObject1.getString("id") + "q", "q" + String.valueOf(i));
                            if (ans_image.substring(1, 2).equals("1"))
                                getImage(jsonObject1.getString("id") + "a", "a" + String.valueOf(i));
                            if (ans_image.substring(2, 3).equals("1"))
                                getImage(jsonObject1.getString("id") + "b", "b" + String.valueOf(i));
                            if (ans_image.substring(3, 4).equals("1"))
                                getImage(jsonObject1.getString("id") + "c", "c" + String.valueOf(i));
                            if (ans_image.substring(4).equals("1"))
                                getImage(jsonObject1.getString("id") + "d", "d" + String.valueOf(i));
                        }
                        builder.create().show();
                    }
                    else{
                        builder1.create().show();
                    }
                    progressBar.setMax(question.size());
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
                Toast.makeText(Quiz_Activity.this,message,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Quiz_Activity.this,Homepage_Activity.class);
                intent.putExtra("test",true);
                startActivity(intent);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("medium",securePreferences.getString("medium", null));
                map.put("class",securePreferences.getString("class", null));
                map.put("insti_id",securePreferences.getString("instiid", null));
                map.put("sub_id",sub_id);
                map.put("test_id",test_id1);
                map.put("chap_id",chapter_id);
                map.put("multiple_test","1");
                map.put("type","get_que");
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void getImage(String url, final String name){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading images.Please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String image_url = "https://smartstudym3.000webhostapp.com/smart_study/question_images/";
        final String final_url = image_url + url + ".png";
        ImageRequest imageRequest = new ImageRequest(final_url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                progressDialog.dismiss();
                imageHashMap.put(name,response);
            }
        },150,150, ImageView.ScaleType.CENTER_CROP
                , null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                String message;
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
                } else {
                    message = "image not fount.Contact Admin";
                }
                Toast.makeText(Quiz_Activity.this,message,Toast.LENGTH_LONG).show();
            }
        });
        imageRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(imageRequest);
    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(time,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String ms = String.format(Locale.US,"%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                                % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % TimeUnit.MINUTES.toSeconds(1));
                TVtimer.setText(ms);
                isTimerRunning = true;
            }

            @Override
            public void onFinish() {
                String ms = "00:00";
                TVtimer.setText(ms);
                SVquestions.setVisibility(View.GONE);
                SVresult.setVisibility(View.VISIBLE);
                loadResult();
                isTimerRunning = false;
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.BTNanswer1:
                BTNanswer1.setBackgroundResource(R.drawable.ansbuttonclicktrue);
                BTNanswer2.setBackgroundResource(R.drawable.ansbuttonclickfalse);
                BTNanswer3.setBackgroundResource(R.drawable.ansbuttonclickfalse);
                BTNanswer4.setBackgroundResource(R.drawable.ansbuttonclickfalse);
                selected_ans = "1";
                break;

            case R.id.BTNanswer2:
                BTNanswer2.setBackgroundResource(R.drawable.ansbuttonclicktrue);
                BTNanswer1.setBackgroundResource(R.drawable.ansbuttonclickfalse);
                BTNanswer3.setBackgroundResource(R.drawable.ansbuttonclickfalse);
                BTNanswer4.setBackgroundResource(R.drawable.ansbuttonclickfalse);
                selected_ans = "2";
                break;

            case R.id.BTNanswer3:
                BTNanswer3.setBackgroundResource(R.drawable.ansbuttonclicktrue);
                BTNanswer1.setBackgroundResource(R.drawable.ansbuttonclickfalse);
                BTNanswer2.setBackgroundResource(R.drawable.ansbuttonclickfalse);
                BTNanswer4.setBackgroundResource(R.drawable.ansbuttonclickfalse);
                selected_ans = "3";
                break;

            case R.id.BTNanswer4:
                BTNanswer4.setBackgroundResource(R.drawable.ansbuttonclicktrue);
                BTNanswer1.setBackgroundResource(R.drawable.ansbuttonclickfalse);
                BTNanswer3.setBackgroundResource(R.drawable.ansbuttonclickfalse);
                BTNanswer2.setBackgroundResource(R.drawable.ansbuttonclickfalse);
                selected_ans = "4";
                break;

            case R.id.BTNnext:
                if (count_que != 4) {
                    if (selected_ans.equals("0")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Warning");
                        builder.setMessage("Do you really want to skip this question?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                update_question(count_que, selected_ans);
                                count_que++;
                                selected_ans = "0";
                            }
                        });
                        builder.setNegativeButton("No",null);
                        builder.create().show();
                    } else {
                        update_question(count_que, selected_ans);
                        count_que++;
                        selected_ans = "0";
                    }
                    if (count_que == question.size()-1) BTNnext.setText(R.string.submit);
                }
                else {
                    if (isTimerRunning) countDownTimer.cancel();
                    SharedPreferences myans = this.getSharedPreferences("myans", MODE_PRIVATE);
                    SharedPreferences.Editor editor = myans.edit();
                    editor.putString(String.valueOf(count_que), selected_ans);
                    editor.apply();

                    SVquestions.setVisibility(View.GONE);
                    SVresult.setVisibility(View.VISIBLE);
                    loadResult();
                }
                break;

            case R.id.FBexit:
                Intent intent = new Intent(Quiz_Activity.this,Homepage_Activity.class);
                intent.putExtra("test",true);
                startActivity(intent);
                break;
        }
    }

    private void saveResult(final int score) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving data...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        final String save_url = "https://smartstudym3.000webhostapp.com/smart_study/results.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, save_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(Quiz_Activity.this,response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                if (response.equals("ok")){
                    Intent intent = new Intent(Quiz_Activity.this,Homepage_Activity.class);
                    intent.putExtra("test",true);
                    startActivity(intent);
                }
                else Toast.makeText(Quiz_Activity.this,"Error,something happened",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Quiz_Activity.this,message,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Quiz_Activity.this,Homepage_Activity.class);
                intent.putExtra("test",true);
                startActivity(intent);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String,String> hashMap = LocalDatabase.getInstance(Quiz_Activity.this).getUserData();
                Map<String,String> map = new HashMap<>();
                map.put("mobileno",securePreferences.getString("mobileno", null));
                map.put("test_id",String.valueOf(test_id));
                map.put("marks",String.valueOf(score));
                map.put("sub_id",sub_id);
                map.put("chapter_id",chapter_id);
                map.put("max_marks",String.valueOf(question.size()));
                map.put("limit","");
                map.put("test_type","");
                map.put("subject","");
                map.put("type","save");
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

////        trial start
//        Toast.makeText(this,"Result saved",Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(Quiz_Activity.this,Homepage_Activity.class);
//        intent.putExtra("test",true);
//        startActivity(intent);
////        trial end
    }

    private void update_question(int count,String selected_ans){
        int a = progressBar.getProgress();
        progressBar.setProgress(a + 1);
        SharedPreferences myans = this.getSharedPreferences("myans", MODE_PRIVATE);
        SharedPreferences.Editor editor = myans.edit();
        editor.putString(String.valueOf(count), selected_ans);
        editor.apply();
        IVquestion.setVisibility(View.GONE);
        IVanswer1.setVisibility(View.GONE);
        IVanswer2.setVisibility(View.GONE);
        IVanswer3.setVisibility(View.GONE);
        IVanswer4.setVisibility(View.GONE);
        count++;

        TVquestion.setText(question.get(count));
        BTNanswer1.setText(ans_a.get(count));
        BTNanswer2.setText(ans_b.get(count));
        BTNanswer3.setText(ans_c.get(count));
        BTNanswer4.setText(ans_d.get(count));
        BTNanswer4.setBackgroundResource(R.drawable.ansbuttonclickfalse);
        BTNanswer1.setBackgroundResource(R.drawable.ansbuttonclickfalse);
        BTNanswer3.setBackgroundResource(R.drawable.ansbuttonclickfalse);
        BTNanswer2.setBackgroundResource(R.drawable.ansbuttonclickfalse);
        if (imageHashMap.containsKey("q" + String.valueOf(count))){
            IVquestion.setVisibility(View.VISIBLE);
            IVquestion.setImageBitmap(imageHashMap.get("q" + String.valueOf(count)));
        }
        if (imageHashMap.containsKey("a" + String.valueOf(count))){
            IVanswer1.setVisibility(View.VISIBLE);
            IVanswer1.setImageBitmap(imageHashMap.get("a" + String.valueOf(count)));
        }
        if (imageHashMap.containsKey("b" + String.valueOf(count))){
            IVanswer2.setVisibility(View.VISIBLE);
            IVanswer2.setImageBitmap(imageHashMap.get("b" + String.valueOf(count)));
        }
        if (imageHashMap.containsKey("c" + String.valueOf(count))){
            IVanswer3.setVisibility(View.VISIBLE);
            IVanswer3.setImageBitmap(imageHashMap.get("c" + String.valueOf(count)));
        }
        if (imageHashMap.containsKey("d" + String.valueOf(count))){
            IVanswer4.setVisibility(View.VISIBLE);
            IVanswer4.setImageBitmap(imageHashMap.get("d" + String.valueOf(count)));
        }
    }

    private void loadResult() {
        FBexit.setVisibility(View.VISIBLE);
        for (int i = 0; i<question.size();i++) {
            LinearLayout LLone = new LinearLayout(this);
            LLone.setOrientation(LinearLayout.VERTICAL);

            TextView TVquestiontitle = new TextView(this);
            LinearLayout.LayoutParams LPTVque = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            TVquestiontitle.setLayoutParams(LPTVque);
            TVquestiontitle.setTextSize(20);
            TVquestiontitle.setGravity(Gravity.START);
            TVquestiontitle.setText(R.string.question);

            TextView TVquestion1 = new TextView(this);
            TVquestion1.setLayoutParams(LPTVque);
            TVquestion1.setTextSize(25);
            TVquestion1.setGravity(Gravity.START);
            String finalquestion = String.valueOf(i+1) + ". " + question.get(i);
            TVquestion1.setText(finalquestion);
            TVquestion1.setBackgroundResource(R.drawable.borderlines);

            LinearLayout LLoneone = new LinearLayout(this);
            LLoneone.setOrientation(LinearLayout.HORIZONTAL);
            LLoneone.setBackground(ContextCompat.getDrawable(this,R.drawable.borderlines));

            TextView TVans_correct_title = new TextView(this);
            LinearLayout.LayoutParams LPTVans = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            TVans_correct_title.setLayoutParams(LPTVans);
            TVans_correct_title.setTextSize(20);
            TVans_correct_title.setGravity(Gravity.CENTER);
            TVans_correct_title.setText(R.string.ans_correct);

            TextView TVans_correct = new TextView(this);
            TVans_correct.setLayoutParams(LPTVans);
            TVans_correct.setTextSize(25);
            TVans_correct.setGravity(Gravity.START);
            String check = ans_correct.get(i);
            String ans = "";
            switch (check) {
                case "1":
                    ans = ans_a.get(i);
                    break;
                case "2":
                    ans = ans_b.get(i);
                    break;
                case "3":
                    ans = ans_c.get(i);
                    break;
                case "4":
                    ans = ans_d.get(i);
                    break;
            }
            TVans_correct.setText(ans);

            LinearLayout LLonetwo = new LinearLayout(this);
            LLonetwo.setOrientation(LinearLayout.HORIZONTAL);
            LLonetwo.setBackground(ContextCompat.getDrawable(this,R.drawable.borderlines));

            TextView TVans_given_title = new TextView(this);
            TVans_given_title.setLayoutParams(LPTVans);
            TVans_given_title.setTextSize(20);
            TVans_given_title.setGravity(Gravity.CENTER);
            TVans_given_title.setText(R.string.ans_given);

            TextView TVans_given = new TextView(this);
            TVans_given.setLayoutParams(LPTVans);
            TVans_given.setTextSize(25);
            TVans_given.setGravity(Gravity.START);
            SharedPreferences myans = this.getSharedPreferences("myans", MODE_PRIVATE);
            String check1 = myans.getString(String.valueOf(i), "0");
            String ans1 = "";
            switch (check1) {
                case "1":
                    ans1 = ans_a.get(i);
                    break;
                case "2":
                    ans1 = ans_b.get(i);
                    break;
                case "3":
                    ans1 = ans_c.get(i);
                    break;
                case "4":
                    ans1 = ans_d.get(i);
                    break;
                case "0":
                    ans1 = "Question did not attempted.";
                    break;
            }
            TVans_given.setText(ans1);

            Space space = new Space(this);
            LinearLayout.LayoutParams spaceparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            spaceparams.height = 80;
            space.setLayoutParams(spaceparams);

            if (ans.equals(ans1)){
                TVquestiontitle.setBackgroundColor(Color.parseColor("#b8edb8"));
                TVquestion1.setBackgroundColor(Color.parseColor("#b8edb8"));
                LLoneone.setBackgroundColor(Color.parseColor("#b8edb8"));
                LLonetwo.setBackgroundColor(Color.parseColor("#b8edb8"));
                score++;
            }
            else {
                TVquestiontitle.setBackgroundColor(Color.parseColor("#ffd1d1"));
                TVquestion1.setBackgroundColor(Color.parseColor("#ffd1d1"));
                LLoneone.setBackgroundColor(Color.parseColor("#ffd1d1"));
                LLonetwo.setBackgroundColor(Color.parseColor("#ffd1d1"));
            }

            LLone.addView(TVquestiontitle);
            LLone.addView(TVquestion1);
            LLone.addView(LLoneone);
            LLone.addView(LLonetwo);
            LLone.addView(space);

            LLoneone.addView(TVans_correct_title);
            LLoneone.addView(TVans_correct);

            LLonetwo.addView(TVans_given_title);
            LLonetwo.addView(TVans_given);
            LLresult.addView(LLone);
        }
        String score_message = "You scored " + String.valueOf(score) + " out of " + String.valueOf(question.size());
        TVscore.setText(score_message);
        float score_count = (Float.parseFloat(String.valueOf(score))/Float.parseFloat(String.valueOf(question.size()))) * 100;
        if (score_count >= 35) Snackbar.make(getWindow().getDecorView().getRootView(),"Congrats. You passed the test",
                Snackbar.LENGTH_LONG).show();
        else Snackbar.make(getWindow().getDecorView().getRootView(),"Oops. You failed the test",
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        SharedPreferences myans = this.getSharedPreferences("myans", MODE_PRIVATE);
        SharedPreferences.Editor editor = myans.edit();
        editor.clear();
        editor.apply();
        if(tosave) {
            saveResult(score);
        }
        if (isTimerRunning) countDownTimer.cancel();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            do_back();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        do_back();
    }

    private void do_back(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("Going back will unset all answers you gave.Do you still want to continue?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences myans = Quiz_Activity.this.getSharedPreferences("myans", MODE_PRIVATE);
                SharedPreferences.Editor editor = myans.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(Quiz_Activity.this,Homepage_Activity.class);
                intent.putExtra("test",true);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No",null);
        builder.create().show();
    }
}
