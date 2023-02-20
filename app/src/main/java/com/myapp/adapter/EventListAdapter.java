package com.myapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myapp.R;
import com.myapp.bean.Event;
import com.myapp.helper.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventListAdapter extends BaseAdapter {
    private Context context;
    private List<Event> eventList;
    private DBHelper dbHelper;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    public EventListAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        if (convertView!=null){
            TextView tvName = convertView.findViewById(R.id.e_name);
            TextView tvTime = convertView.findViewById(R.id.e_time);
            TextView tvDate = convertView.findViewById(R.id.e_date);
            View btnUncheck = convertView.findViewById(R.id.btn_uncheck);

            tvName.setText(eventList.get(position).getName());
            tvDate.setText(eventList.get(position).getStart());
            tvTime.setText(eventList.get(position).getTime());
            /* click check
             *   1. delete e and add to completeList
             *   2. update the repeat event
             * */
            dbHelper = new DBHelper(context);
            Event e = eventList.get(position);

            if (e.getCheck().equals("no")) {
                if (e.getTag().equals("event")){
                    btnUncheck.setBackgroundResource(R.drawable.ic_twotone_event_note_24);
                } else {
                    btnUncheck.setBackgroundResource(R.drawable.ic_baseline_radio_button_unchecked_24);
                    btnUncheck.setOnClickListener(v -> {
                        if (e.getRepeat()>0){
                            getNext(e);
                        }
                        // remove e from eventList and db
                        eventList.remove(e);
                        dbHelper.delete(e);
                        // change check
                        e.setCheck("yes");
                        if (!dbHelper.queryEvent(e)){
                            eventList.add(e);
                            dbHelper.insertInto(e);
                            ToastAdapter.initToast(context, "complete event");
                        }
                        this.notifyDataSetChanged();
                    });
                }
            } else if (e.getCheck().equals("yes")){
                btnUncheck.setBackgroundResource(R.drawable.ic_baseline_check_circle_outline_24);
                btnUncheck.setOnClickListener(v -> {
                    eventList.remove(e);
                    dbHelper.delete(e);
                    e.setCheck("no");
                    if (!dbHelper.queryEvent(e)){
                        eventList.add(e);
                        dbHelper.insertInto(e);
                        ToastAdapter.initToast(context, "cancel");
                    }
                    this.notifyDataSetChanged();
                });
            }
        }
        return convertView;
    }

    // 获取下一次发生的日期
    private void getNext(Event e){
        String curDate = sdf.format(new Date(System.currentTimeMillis()));
        String d;
        // decide according to which to get next date
        if (apartDays(curDate, e.getStart()) != 0){
            d = e.getStart();
        } else {
            d = curDate;
        }
        // get next date
        String dateN = nextDate(d, e.getRepeat());
        int apartDays = apartDays(dateN, e.getDeadline());
        if (apartDays >= 0){
            Event eUnchecked = new Event(e.getName(), dateN, e.getTime(), e.getDeadline(),
                    e.getRepeat(), e.getNote(), e.getCheck(), e.getTag());
            Event eChecked = new Event(e.getName(), dateN, e.getTime(), e.getDeadline(),
                    e.getRepeat(), e.getNote(), "yes", e.getTag());
            dbHelper = new DBHelper(context);
            // judge whether add
            if (!dbHelper.queryEvent(eUnchecked) && !dbHelper.queryEvent(eChecked)){
                dbHelper.insertInto(eUnchecked);   // add next date to events
                eventList.add(eUnchecked);
            }
        }
    }

    // 延后 repeat 天后的日期
    private String nextDate(String date, int repeat){
        Calendar calendar = Calendar.getInstance();
        Date d = null;
        // turn to date format
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert d != null;
        // set the date to calendar
        calendar.setTime(d);
        calendar.add(Calendar.DATE, repeat);
        return sdf.format(calendar.getTime());
    }

    // start and end apart days
    private Integer apartDays(String start, String end){
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(start);
            endDate = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert endDate != null;
        assert startDate != null;
        return (int) ((endDate.getTime() - startDate.getTime())/24/60/60/1000);
    }
}
