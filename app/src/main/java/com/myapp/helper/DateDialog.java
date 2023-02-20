package com.myapp.helper;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateDialog {
    private Context context;

    public DateDialog(Context context) {
        this.context = context;
    }

    public String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    public DatePickerDialog showDateDialog(EditText editText1){
        Calendar calendar = Calendar.getInstance();
        return new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
            String m = String.valueOf(month+1);
            String d = String.valueOf(dayOfMonth);
            if (month+1<10){
                m = "0"+m;
            }
            if (dayOfMonth<10){
                d = "0"+d;
            }
            CharSequence csDate = year + "-" + m + "-" + d;
            editText1.setText(csDate);
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    public TimePickerDialog showTimeDialog(EditText editText){
        Calendar calendar = Calendar.getInstance();
        return new TimePickerDialog(context, (view, hourOfDay, minute) -> {
            String h = String.valueOf(hourOfDay);
            String m = String.valueOf(minute);
            if (hourOfDay<10){
                h = "0" + h;
            }
            if(minute < 10){
                m = "0" + m;
            }
            CharSequence csTime  = h + ":" + m;
            editText.setText(csTime);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
    }
}
