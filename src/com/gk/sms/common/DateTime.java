package com.gk.sms.common;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class DateTime {

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public int getDay() {
        return dd;
    }

    public int getMonth() {
        return mm;
    }

    public int getYear() {
        return yyyy;
    }

    public int getHour() {
        return hh;
    }

    public int getMinute() {
        return mi;
    }

    public int getSecond() {
        return sec;
    }

    public Timestamp getTimestamp() {
        return ts;
    }

    public void setTimestamp(Timestamp ts) {
        if (ts != null) {
            this.ts = ts;
            cal.setTime(new Date(ts.getTime()));
        }
    }

    public void refresh() {
        ts = new Timestamp(cal.getTime().getTime());
        extractInfo();
    }

    public void setDay(int day) {
        cal.set(5, day);
    }

    public void setMonth(int month) {
        cal.set(2, month - 1);
    }

    public void setYear(int year) {
        cal.set(1, year);
    }

    public void setHour(int hour) {
        cal.set(11, hour);
    }

    public void setMinute(int min) {
        cal.set(12, min);
    }

    public void setSecond(int sec) {
        cal.set(13, sec);
    }

    public DateTime() {
        cal = Calendar.getInstance();
        ts = new Timestamp(cal.getTime().getTime());
        extractInfo();
    }

    public DateTime(Timestamp ts) {
        this.ts = ts;
        cal = Calendar.getInstance();
        cal.setTime(new Date(ts.getTime()));
        extractInfo();
    }

    public DateTime(Calendar cal) {
        this.cal = cal;
        ts = new Timestamp(cal.getTime().getTime());
        extractInfo();
    }

    public static DateTime getInstance() {
        return new DateTime();
    }

    public static DateTime getInstance(Timestamp ts) {
        return new DateTime(ts);
    }

    public DateTime getNextDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(ts.getTime()));
        Calendar _tmp = calendar;
        int iDay = calendar.get(5);
        calendar.set(5, iDay + 1);
        return new DateTime(calendar);
    }

    public static DateTime getNextDay(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day + 1);
        return new DateTime(calendar);
    }

    public DateTime getPreviousDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(ts.getTime()));
        Calendar _tmp = calendar;
        int iDay = calendar.get(5);
        calendar.set(5, iDay - 1);
        return new DateTime(calendar);
    }

    public static DateTime getPreviousDay(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day - 1);
        return new DateTime(calendar);
    }

    private void extractInfo() {
        int d = cal.get(7);
        switch (d) {
            case 2: // '\002'
                dayOfWeek = "Thu Hai";
                break;

            case 3: // '\003'
                dayOfWeek = "Thu Ba";
                break;

            case 4: // '\004'
                dayOfWeek = "Thu Tu";
                break;

            case 5: // '\005'
                dayOfWeek = "Thu Nam";
                break;

            case 6: // '\006'
                dayOfWeek = "Thu Sau";
                break;

            case 7: // '\007'
                dayOfWeek = "Thu Bay";
                break;

            case 1: // '\001'
                dayOfWeek = "Chu Nhat";
                break;

            default:
                dayOfWeek = "";
                break;
        }
        dd = cal.get(5);
        mm = cal.get(2) + 1;
        yyyy = cal.get(1);
        hh = cal.get(11);
        mi = cal.get(12);
        sec = cal.get(13);
    }

    public static long getMinuteBetween2Date(Timestamp ts1, Timestamp ts2) {
        long d1 = ts1.getTime();
        long d2 = ts2.getTime();
        long d3 = d2 - d1;
        return d3 / 1000L / 60L;
    }

    public static int whichDate(Timestamp dt) {
        Calendar bCal = Calendar.getInstance();
        Calendar tCal = Calendar.getInstance();
        Calendar nCal = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        Timestamp crTime = new Timestamp(System.currentTimeMillis());
        bCal.set(cal.get(1), cal.get(2), cal.get(5) - 1, 12, 0, 0);
        tCal.set(cal.get(1), cal.get(2), cal.get(5), 12, 0, 0);
        nCal.set(cal.get(1), cal.get(2), cal.get(5) + 1, 12, 0, 0);
        if (crTime.getTime() <= tCal.getTime().getTime()) {
            if (dt.getTime() <= bCal.getTime().getTime() - 0x5265c00L) {
                return -2;
            }
            if (dt.getTime() < bCal.getTime().getTime()) {
                return -1;
            }
            return dt.getTime() >= tCal.getTime().getTime() ? 1 : 0;
        }
        if (dt.getTime() > nCal.getTime().getTime()) {
            return 1;
        }
        if (dt.getTime() > tCal.getTime().getTime()) {
            return 0;
        }
        return dt.getTime() <= bCal.getTime().getTime() ? -2 : -1;
    }
    //***************************************
    private Timestamp ts;
    private Calendar cal;
    private int dd;
    private int mm;
    private int yyyy;
    private int hh;
    private int mi;
    private int sec;
    private String dayOfWeek;
}
