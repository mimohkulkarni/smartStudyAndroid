package com.kulkarni.mimoh.smartstudy;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Homepage_Fragment extends Fragment {

    private TextView textView;
    TextView TVinsti_name;
    private long difftime;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning;
    private ViewPager viewPager;
    private static final int timeout = 10000;
//    private final String complete_text = "Touch to start Test";
    final String test_check_url = "https://smartstudym3.000webhostapp.com/smart_study/test.php";
//    HashMap<String,String> hashMap;
    SecurePreferences securePreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        viewPager = view.findViewById(R.id.viewPAger);
        viewPager.setAdapter(new ViewpagerAdapter());

        TVinsti_name = view.findViewById(R.id.TVinsti_name);
//        hashMap = LocalDatabase.getInstance(getContext()).getUserData();
        securePreferences = new SecurePreferences(getContext());

        String institute = "Welcome to " + securePreferences.getString("instiname", "error");
        TVinsti_name.setText(institute);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new ViewPagerTimer(),4000,7000);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, test_check_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.US);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("resultarray");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Calendar calendar = Calendar.getInstance();
//                    Toast.makeText(getContext(),jsonObject1.getString("test_date_time"),Toast.LENGTH_LONG).show();
                    Date inputtime = simpleDateFormat.parse(jsonObject1.getString("test_date_time"));
                    String currtimestr = simpleDateFormat.format(calendar.getTime());
                    Date currtime = simpleDateFormat.parse(currtimestr);
                    difftime  = inputtime.getTime() - currtime.getTime();
//                    Toast.makeText(getContext(),String.valueOf(difftime),Toast.LENGTH_SHORT).show();
                    if (difftime > 10000) startTimer();
                    else textView.setText("Touch to start Test");
                } catch (Exception e) {
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
                map.put("insti_id","");
                map.put("class","");
                map.put("medium","");
                map.put("sub_id","");
                map.put("test_id","");
                map.put("chap_id","");
                map.put("multiple_test","");
                map.put("type","check_test");
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

        return view;
    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(difftime,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                difftime = millisUntilFinished;
                String hms = String.format(Locale.US,"%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(difftime),
                        TimeUnit.MILLISECONDS.toMinutes(difftime) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(difftime) % TimeUnit.MINUTES.toSeconds(1));
                String timer_message = "Next test is in " + hms + "  Start studying.";
                textView.setText(timer_message);
                isTimerRunning = true;
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                String timer_finish_message = "Test is available now";
                textView.setText(timer_finish_message);
                isTimerRunning = false;
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        if (isTimerRunning) countDownTimer.cancel();
        super.onDestroy();
    }

    public class ViewpagerAdapter extends PagerAdapter {

        LayoutInflater layoutInflater;
        int [] layouts = {R.layout.viewpager_photo_name_layout,R.layout.viewpager_timer_layout,R.drawable.homepage_image1,R.drawable.homepage_image2};

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return (view == object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) Objects.requireNonNull(getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view1 = Objects.requireNonNull(layoutInflater).inflate(R.layout.viewpager_photo_name_layout,container,false);
            View view2 = layoutInflater.inflate(R.layout.viewpager_timer_layout,container,false);
            LinearLayout timerLayout = view2.findViewById(R.id.timerlayout);

            TextView textView1 = view1.findViewById(R.id.TVuser_name);
            String user_name = "Welcome " + securePreferences.getString("name",null);
            textView1.setText(user_name);

            LinearLayout.LayoutParams TVparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            textView = new TextView(getContext());
            textView.setLayoutParams(TVparams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTextSize(30);

            timerLayout.addView(textView);

            View view3 = layoutInflater.inflate(R.layout.viewpager_images_layout,container,false);
            View view4 = layoutInflater.inflate(R.layout.viewpager_images_layout,container,false);
            ImageView imageView = view4.findViewById(R.id.homepage_imageview);
            imageView.setImageResource(R.drawable.homepage_image2);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                    transaction.replace(R.id.fragment_homepage_container, new Select_Quiz_Fragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });

            View views[] = {view1,view4,view3,view2};
            container.addView(views[position]);
            return views[position];
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((LinearLayout)object);
        }
    }

    public class ViewPagerTimer extends TimerTask {

        @Override
        public void run() {
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (viewPager.getCurrentItem()){
                        case 0:
                            viewPager.setCurrentItem(1);
                            break;

                        case 1:
                            viewPager.setCurrentItem(2);
                            break;

                        case 2:
                            viewPager.setCurrentItem(3);
                            break;

                        case 3:
                            viewPager.setCurrentItem(0);
                            break;
                    }
                }
            });
        }
    }
}