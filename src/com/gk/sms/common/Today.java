package com.gk.sms.common;

import java.util.Calendar;

public class Today {

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

    public Today(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(5, day);
        cal.set(2, month - 1);
        cal.set(1, year);
        init(cal);
    }

    public Today() {
        Calendar cal = Calendar.getInstance();
        init(cal);
    }

    private void init(Calendar cal) {
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

    public static Today getInstance() {
        return new Today();
    }
    private int dd;
    private int mm;
    private int yyyy;
    private int hh;
    private int mi;
    private int sec;
    private String dayOfWeek;
}
