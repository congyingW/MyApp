package com.myapp.bean;

public class Event {
    private String name;
    private String start;
    private String deadline;
    private String time;
    private Integer repeat;
    private String note;
    private String check;
    private String tag;

    public Event(String name, String start, String time, String deadline,
                 Integer repeat, String note, String check, String tag) {
        this.name = name;
        this.start = start;
        this.time = time;
        this.deadline = deadline;
        this.repeat = repeat;
        this.note = note;
        this.check = check;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String end) {
        this.deadline = end;
    }

    public Integer getRepeat() {
        return repeat;
    }

    public void setRepeat(Integer repeat) {
        this.repeat = repeat;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getCheck() {
        return check;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
