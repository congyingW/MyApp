package com.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.myapp.adapter.EventListAdapter;
import com.myapp.bean.Event;
import com.myapp.helper.DBHelper;
import com.myapp.others.NewActivity;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView tvDate, tvTime,tvWeek;
    Button btnCal, btnToday, btnSearch, btnAdd, btnDown, btnAll, btnEvent, btnCompleted;
    ListView listView;
    private DBHelper dbHelper;
    private List<Event> eventList;
    private EventListAdapter listAdapter;

    @Override
    protected void onRestart() {
        super.onRestart();
        showUncompleted();
    }

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
        dbHelper = new DBHelper(this);
        showUncompleted();

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

        jump(btnAdd, NewActivity.class);
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
     * @param btn: button
     * @param activity: activity
     */
    private void jump(Button btn, Class<?> activity){
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, activity);
            startActivity(intent);
        });
    }

    /**
     * show uncompleted to do or habits
     */
    // use list adapter to show uncompleted to do or habits
    private void showUncompleted(){
        eventList = dbHelper.getAll();
        List<Event> uncheck = new ArrayList<>();
        for (Event e: eventList){
            if (e.getCheck().equals("no") && !e.getTag().equals("event")){
                uncheck.add(e);
            }
        }
        listAdapter = new EventListAdapter(this, uncheck);
        listView.setAdapter(listAdapter);
    }

//    private void sendEvent(){
//        DateDialog dialog = new DateDialog(this);
//        String today = dialog.getCurrentDate();
//        List<Event> todayEvents = dbHelper.queryEvents("start", today);
//        if (todayEvents.size()>0){
//            int i = 0;
//            for (Event event:todayEvents){
//                if (!event.getTime().equals("") && event.getCheck().equals("no")){
//                    i+=1;
//                    alarm(i,event.getTime()+":00", event.getName());
//                }
//            }
//        }
//    }
//    // send notification
//    private void alarm(int requestCode, String time, String value){
//        createNotificationChannel();
//        LocalTime localDateTime = LocalTime.now();
//        LocalTime localDateTime1 = LocalTime.parse(time);
//        Duration duration = Duration.between(localDateTime, localDateTime1);
//        long durMillis = duration.getSeconds();
//        if (durMillis == 3){
//            Intent intent = new Intent(this, NotifyService.class);
//            intent.setAction("notify");
//            intent.putExtra("sentence", value);
//            intent.putExtra("requestCode", String.valueOf(requestCode));
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode,
//                    intent, PendingIntent.FLAG_IMMUTABLE);
//            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
//        }
//    }
//    private void createNotificationChannel() {
//        CharSequence name = getString(R.string.channel_name);
//        String description = getString(R.string.channel_description);
//        int importance = NotificationManager.IMPORTANCE_DEFAULT;
//        NotificationChannel channel = new NotificationChannel("10", name, importance);
//        channel.setDescription(description);
//        NotificationManager notificationManager = getSystemService(NotificationManager.class);
//        notificationManager.createNotificationChannel(channel);
//    }

}