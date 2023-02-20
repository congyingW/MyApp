package com.myapp.others;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.myapp.R;
import com.myapp.adapter.ToastAdapter;
import com.myapp.bean.Event;
import com.myapp.helper.DBHelper;
import com.myapp.helper.DateDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewActivity extends AppCompatActivity {
    TextView tvDate;
    Button btnCal, btnClear, btnSave;
    EditText etName, etStart, etTime, etEnd, etRepeat, etNote;
    Button btnStart, btnTime, btnEnd;
    private DateDialog dateDialog;
    private DBHelper dbHelper;
    private String tag = "event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();   // 隐藏状态栏
            getWindow().setStatusBarColor(Color.parseColor("#72A33E")); // 修改标签栏颜色
        }
        initViews();
        dateDialog = new DateDialog(this);
        dbHelper = new DBHelper(this);
        tvDate.setText(dateDialog.getCurrentDate());

        Intent intent = getIntent();
        String chosenDate = intent.getStringExtra("start chosen");
        etStart.setText(chosenDate);

        btnClear.setOnClickListener(v -> {
            etName.setText("");
            etStart.setText("");
            etTime.setText("");
            etEnd.setText("");
            etRepeat.setText("");
            etNote.setText("");
        });
        btnStart.setOnClickListener(v -> dateDialog.showDateDialog(etStart).show());
        btnTime.setOnClickListener(v -> dateDialog.showTimeDialog(etTime).show());
        btnEnd.setOnClickListener(v -> dateDialog.showDateDialog(etEnd).show());

        RadioGroup radioGroup = findViewById(R.id.radios);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            tag = radioButton.getText().toString();
        });
        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String start = etStart.getText().toString();
            String deadline = etEnd.getText().toString();
            String repeat = etRepeat.getText().toString();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            boolean tag1 = true;
            if (!deadline.equals("")){
                try {
                    Date startD = sdf1.parse(start);
                    Date endD = sdf1.parse(deadline);
                    assert startD != null;
                    if (!startD.before(endD)){
                        tag1 = false;
                        ToastAdapter.initToast(NewActivity.this,
                                "deadline must after start date");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (repeat.equals("")){
                repeat = "0";
            } else {
                if (deadline.equals("")){
                    ToastAdapter.initToast(NewActivity.this,
                            "deadline cannot be empty while repeat!=0");
                    tag1=false;
                }
            }
            if (name.equals("") || start.equals("")){
                ToastAdapter.initToast(NewActivity.this,
                        "name and start date cannot be empty");
                tag1 = false;
            }
            if(tag1){
                Event e1 = new Event(name, start, etTime.getText().toString(),
                        deadline, Integer.parseInt(repeat), etNote.getText().toString(),
                        "no", tag);
                // query whether event exist
                if (!dbHelper.queryEvent(e1)){
                    String tag = dbHelper.insertInto(e1);
                    ToastAdapter.initToast(NewActivity.this, "save "+tag);
                } else {
                    ToastAdapter.initToast(NewActivity.this, "You already added it!");
                }
            }
        });

    }
    private void initViews(){
        tvDate = findViewById(R.id.add_date);
        btnCal = findViewById(R.id.add_cal);
        btnClear = findViewById(R.id.add_clear);
        btnSave = findViewById(R.id.add_save);

        etName = findViewById(R.id.et_name);
        etStart = findViewById(R.id.et_start);
        etTime = findViewById(R.id.et_time);
        etEnd = findViewById(R.id.et_end);
        etRepeat = findViewById(R.id.et_repeat);
        etNote = findViewById(R.id.et_note);

        btnStart = findViewById(R.id.btn_start);
        btnTime = findViewById(R.id.btn_time);
        btnEnd = findViewById(R.id.btn_end);
    }
}