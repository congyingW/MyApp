package com.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView tvDate, tvTime,tvWeek;
    Button btnCal, btnToday, btnSearch, btnAdd, btnDown, btnAll, btnEvent, btnCompleted;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();   // 隐藏状态栏
            getWindow().setStatusBarColor(Color.parseColor("#216DBF")); // 修改标签栏颜色
        }
        initViews();
        showTime();
        showDate();
    }
    private void initViews(){
        tvTime = findViewById(R.id.tvTime);
        tvWeek = findViewById(R.id.tvWeek);
        tvDate = findViewById(R.id.tvDate);

        btnCal = findViewById(R.id.btn_cal);
        btnToday = findViewById(R.id.btn_today);
        btnSearch = findViewById(R.id.btn_search);
        btnAdd = findViewById(R.id.btn_add);
        btnDown = findViewById(R.id.btn_down);
        btnAll = findViewById(R.id.btn_all);
        btnEvent = findViewById(R.id.btn_event);
        btnCompleted = findViewById(R.id.btn_completed);

        listView = findViewById(R.id.list_main);
    }

    private void showTime(){
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        tvTime.setText(sdfTime.format(new Date(System.currentTimeMillis())));
    }
    private String getWeek(){
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        switch (i){
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
            default:
                return "";
        }
    }
    private void showDate(){
        tvWeek.setText(getWeek());
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd  yyyy", Locale.CHINA);
        tvDate.setText(sdf.format(new Date(System.currentTimeMillis())));
    }

    /**
     * jump to other page
     * @param btn
     * @param activity
     */
    private void jump(Button btn, Class<?> activity){
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, activity);
            startActivity(intent);
        });
    }
}