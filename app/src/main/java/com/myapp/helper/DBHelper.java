package com.myapp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.myapp.bean.Event;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private final String TABLE_NAME = "event";
    private final String NAME = "name";
    private final String START = "start";
    private final String TIME = "time";
    private final String DEADLINE = "deadline";
    private final String REPEAT = "repeat";
    private final String NOTE = "note";
    private final String CHECKED = "checked";
    private final String TAG = "tag";
    String whereClause = "name=? and start=? and time=? and deadline=? and repeat=? and note=? and checked=? and tag=?";


    public DBHelper(@Nullable Context context) {
        super(context, "journal.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table event(" +
                "name varchar(50) not null," +
                "start text not null," +
                "time text," +
                "deadline text," +
                "repeat integer," +
                "note varchar(300)," +
                "checked text not null," +
                "tag text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * insert new event to journal table
     * @param event: a class
     * @return: result
     */
    public String insertInto(Event event){
        ContentValues cv = new ContentValues();
        cv.put(NAME, event.getName());
        cv.put(START, event.getStart());
        cv.put(TIME, event.getTime());
        cv.put(DEADLINE, event.getDeadline());
        cv.put(REPEAT, event.getRepeat());
        cv.put(NOTE, event.getNote());
        cv.put(CHECKED, event.getCheck());
        cv.put(TAG, event.getTag());
        // 进行写入
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        long insert = sqlDB.insert(TABLE_NAME, null, cv);
        sqlDB.close();
        if (insert == -1){
            return "insert failure";
        }
        return "insert success";
    }

    /**
     * delete event according to all
     * @param event: a class
     * @return: result
     */
    public String delete(Event event){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int delete = sqLiteDatabase.delete(TABLE_NAME, whereClause,
                new String[]{event.getName(),
                        event.getStart(), event.getTime(),
                        event.getDeadline(), String.valueOf(event.getRepeat()),
                        event.getNote(), event.getCheck(), event.getTag()});
        sqLiteDatabase.close();
        if (delete == 0){
            return "delete failure";
        }
        return "delete success";
    }

    /**
     * query the only one event according to all
     * @param event: a event class
     * @return: exist or not
     */
    public Boolean queryEvent(Event event){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "select * from event where " + whereClause;
        Cursor cursor = sqLiteDatabase.rawQuery(sql,
                new String[]{event.getName(),
                        event.getStart(), event.getTime(),
                        event.getDeadline(), String.valueOf(event.getRepeat()),
                        event.getNote(), event.getCheck(), event.getTag()});
        if (cursor.getCount() == 0){
            cursor.close();
            sqLiteDatabase.close();
            return false;
        } else {
            // get it
            cursor.close();
            sqLiteDatabase.close();
            return true;
        }
    }

    private List<Event> getEvents(Cursor cursor) {
        List<Event> eventList = new ArrayList<>();
        int name = cursor.getColumnIndex(NAME);
        int start = cursor.getColumnIndex(START);
        int time = cursor.getColumnIndex(TIME);
        int deadline = cursor.getColumnIndex(DEADLINE);
        int repeat = cursor.getColumnIndex(REPEAT);
        int note = cursor.getColumnIndex(NOTE);
        int checked = cursor.getColumnIndex(CHECKED);
        int tag = cursor.getColumnIndex(TAG);
        while (cursor.moveToNext()){
            Event event = new Event(cursor.getString(name), cursor.getString(start),
                    cursor.getString(time),cursor.getString(deadline),
                    cursor.getInt(repeat), cursor.getString(note),
                    cursor.getString(checked), cursor.getString(tag));
            eventList.add(event);
        }
        return eventList;
    }
    //    5. query events according to name or start date or tag or check
    public List<Event> queryEvents(String key, String value){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sqlSelect = "";
        switch (key) {
            case NAME:
                sqlSelect = "select * from event where name=? order by date(start) desc";
                break;
            case START:
                sqlSelect = "select * from event where start=? order by date(start) desc";
                break;
            case TAG:
                sqlSelect = "select * from event where tag=? order by date(start) desc";
                break;
            case CHECKED:
                sqlSelect = "select * from event where checked=? order by date(start) desc";
                break;
        }
        Cursor cursor = sqLiteDatabase.rawQuery(sqlSelect, new String[]{value});
        List<Event> eventList = getEvents(cursor);
        cursor.close();
        sqLiteDatabase.close();
        return eventList;
    }
    //    6. query all events order by date
    public List<Event> getAll(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from event order by date(start)",
                null);
        List<Event> eventList = getEvents(cursor);
        cursor.close();
        sqLiteDatabase.close();
        return eventList;
    }

    public List<Event> queryCompleted(String tag){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sqlSelect = "select * from event where checked=? and tag!=? order by date(start) desc";
        Cursor cursor = sqLiteDatabase.rawQuery(sqlSelect, new String[]{"yes", tag});
        List<Event> eventList = getEvents(cursor);
        cursor.close();
        sqLiteDatabase.close();
        return eventList;
    }
}
