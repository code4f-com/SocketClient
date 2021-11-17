package com.gk.sms.common;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;

public class DateProc {

    public DateProc() {
    }

    public static Time String2Time(String strTime, String format) {
        Time result = null;
        try {
            DateFormat formatter = new SimpleDateFormat(format);
            result = new java.sql.Time(formatter.parse(strTime).getTime());
        } catch (Exception e) {
            Tool.debug(e.getMessage());
        }
        return result;
    }

    public static Timestamp String2Timestamp(String strInputDate) {
        String strDate = strInputDate;
        int i, nYear, nMonth, nDay;
        String strSub = null;
        i = strDate.indexOf("/");
        if (i < 0) {
            return createTimestamp();
        }
        strSub = strDate.substring(0, i);
        nDay = (new Integer(strSub.trim()));
        strDate = strDate.substring(i + 1);
        i = strDate.indexOf("/");
        if (i < 0) {
            return createTimestamp();
        }
        strSub = strDate.substring(0, i);
        nMonth = (new Integer(strSub.trim())) - 1; // Month begin from 0 value
        strDate = strDate.substring(i + 1);
        if (strDate.length() < 4) {
            if (strDate.substring(0, 1).equals("9")) {
                strDate = "19" + strDate.trim();
            } else {
                strDate = "20" + strDate.trim();
            }
        }
        nYear = (new Integer(strDate));
        Calendar calendar = Calendar.getInstance();
        calendar.set(nYear, nMonth, nDay);
        return new Timestamp((calendar.getTime()).getTime());
    }

    /**
     * Lay ra thoi gian thuc kieu Double
     *
     * @return
     */
    public static double getTimer() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);
        double realTime = (double) h + (double) m / 60;
        return realTime;
    }

    /**
     * Lay Thoi Gian Hien tai Kieu TimeStime
     *
     * @return
     */
    public static Timestamp createTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String createDDMMYYYY() {
        String str = Timestamp2DDMMYYYY(new Timestamp(System.currentTimeMillis()));
        return str;
    }

    public static String createYYYYMMDDhh24miss() {
        String str = Timestamp2YYYYMMDDHH24MiSS(new Timestamp(System.currentTimeMillis()));
        return str;
    }

    /**
     * *
     * return date with format: dd/mm
     *
     * @param ts
     * @return
     */
    public static String Timestamp2DDMM(java.sql.Timestamp ts) {
        if (ts == null) {
            return "";
        } else {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(new java.util.Date(ts.getTime()));

            String strTemp = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
            if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
                strTemp = "0" + strTemp;
            }
            if (calendar.get(Calendar.MONTH) + 1 < 10) {
                return strTemp + "/0" + (calendar.get(Calendar.MONTH) + 1);
            } else {
                return strTemp + "/" + (calendar.get(Calendar.MONTH) + 1);
            }
        }
    }

    /**
     * Lay ra thoi kieu Timestamp theo ngay truyen vao
     *
     * @param date
     * @return
     */
    public static Timestamp createDateTimestamp(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new Timestamp((calendar.getTime()).getTime());
    }

    /**
     * Convert Date To Timestamp
     *
     * @param strInputDate
     * @return
     */
    public static Timestamp StringDDMMYYYYHHMMSS2Timestamp(String strInputDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
            Timestamp date = new Timestamp(dateFormat.parse(strInputDate).getTime());
            return date;
        } catch (ParseException ex) {
            return null;
        }
    }

    public static Timestamp String2Timestamp(String strInputDate, String forMat) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(forMat);
            Timestamp date = new Timestamp(dateFormat.parse(strInputDate).getTime());
            return date;
        } catch (ParseException ex) {
            return null;
        }
    }

    public static String getDateTimeString(Timestamp ts) {
        if (ts == null) {
            return "";
        }
        return Timestamp2DDMMYYYY(ts) + " " + Timestamp2HHMMSS(ts, 1);
    }

    public static String getTimeString(Timestamp ts) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date(ts.getTime()));
        return calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + calendar.get(Calendar.MINUTE) + ":"
                + calendar.get(Calendar.SECOND);
    }

    /**
     * return date with format: dd/mm/yyyy
     *
     * @param ts
     * @return
     */
    public static String Timestamp2DDMMYYYY(Timestamp ts) {
        if (ts == null) {
            return "";
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new java.util.Date(ts.getTime()));

            String strTemp = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
            if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
                strTemp = "0" + strTemp;
            }
            if (calendar.get(Calendar.MONTH) + 1 < 10) {
                return strTemp + "/0" + (calendar.get(Calendar.MONTH) + 1)
                        + "/" + calendar.get(Calendar.YEAR);
            } else {
                return strTemp + "/" + (calendar.get(Calendar.MONTH) + 1) + "/"
                        + calendar.get(Calendar.YEAR);
            }
        }
    }

    /**
     * Return String Format ddMMyyyyhh24miss
     *
     * @param ts
     * @return
     */
    public static String Timestamp2DDMMYYYYHH24MiSS(Timestamp ts) {
        if (ts == null) {
            return "";
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new java.util.Date(ts.getTime()));

            String strTemp = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
            if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
                strTemp = "0" + strTemp;
            }
            if (cal.get(Calendar.MONTH) + 1 < 10) {
                strTemp += "/0" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
            } else {
                strTemp += "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
            }
            strTemp += " ";
            if (cal.get(Calendar.HOUR_OF_DAY) < 10) {
                strTemp += "0" + cal.get(Calendar.HOUR_OF_DAY) + ":";
            } else {
                strTemp += cal.get(Calendar.HOUR_OF_DAY) + ":";
            }
            if (cal.get(Calendar.MINUTE) < 10) {
                strTemp += "0" + cal.get(Calendar.MINUTE) + ":";
            } else {
                strTemp += cal.get(Calendar.MINUTE) + ":";
            }
            if (cal.get(Calendar.SECOND) < 10) {
                strTemp += "0" + cal.get(Calendar.SECOND);
            } else {
                strTemp += cal.get(Calendar.SECOND);
            }
            return strTemp;

        }
    }

    /**
     * Return String Format ddMMyyyyhh24mi
     *
     * @param ts
     * @return
     */
    public static String Timestamp2DDMMYYYYHH24Mi(Timestamp ts) {
        if (ts == null) {
            return "";
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new java.util.Date(ts.getTime()));

            String strTemp = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
            if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
                strTemp = "0" + strTemp;
            }
            if (cal.get(Calendar.MONTH) + 1 < 10) {
                strTemp += "/0" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
            } else {
                strTemp += "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
            }
            strTemp += " ";
            if (cal.get(Calendar.HOUR_OF_DAY) < 10) {
                strTemp += "0" + cal.get(Calendar.HOUR_OF_DAY) + ":";
            } else {
                strTemp += cal.get(Calendar.HOUR_OF_DAY) + ":";
            }
            if (cal.get(Calendar.MINUTE) < 10) {
                strTemp += "0" + cal.get(Calendar.MINUTE);
            } else {
                strTemp += cal.get(Calendar.MINUTE);
            }
            return strTemp;

        }
    }

    /**
     * Return date with format: mm/dd/yyyy
     *
     * @param ts
     * @return
     */
    public static String Timestamp2MMDDYYYY(Timestamp ts) {
        if (ts == null) {
            return "";
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new java.util.Date(ts.getTime()));

            String strTemp = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
            if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
                strTemp = "0" + strTemp;
            }
            if (cal.get(Calendar.MONTH) + 1 < 10) {
                return "0" + (cal.get(Calendar.MONTH) + 1) + "/" + strTemp
                        + "/" + cal.get(Calendar.YEAR);
            } else {
                return (cal.get(Calendar.MONTH) + 1) + "/" + strTemp + "/"
                        + +cal.get(Calendar.YEAR);
            }
        }
    }

    /**
     * Return date with format: dd/mm/yy
     *
     * @param ts
     * @return
     */
    public static String Timestamp2DDMMYY(Timestamp ts) {
        int endYear;
        if (ts == null) {
            return "";
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new java.util.Date(ts.getTime()));

            String strTemp = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
            endYear = calendar.get(Calendar.YEAR) % 100;
            if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
                strTemp = "0" + strTemp;
            }
            if (calendar.get(Calendar.MONTH) + 1 < 10) {
                if (endYear < 10) {
                    return strTemp + "/0" + (calendar.get(Calendar.MONTH) + 1)
                            + "/0" + endYear;
                } else {
                    return strTemp + "/0" + (calendar.get(Calendar.MONTH) + 1)
                            + "/" + endYear;
                }
            } else {
                if (endYear < 10) {
                    return strTemp + "/" + (calendar.get(Calendar.MONTH) + 1)
                            + "/0" + endYear;
                } else {
                    return strTemp + "/" + (calendar.get(Calendar.MONTH) + 1)
                            + "/" + endYear;
                }
            }
        }
    }

    /**
     * Return date with format: d/m/yy
     *
     * @param ts
     * @return
     */
    public static String Timestamp2DMYY(Timestamp ts) {
        int endYear = 0;
        if (ts == null) {
            return "";
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new java.util.Date(ts.getTime()));
            endYear = calendar.get(Calendar.YEAR) % 100;
            String strTemp = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
            if (endYear < 10) {
                return strTemp + "/" + (calendar.get(Calendar.MONTH) + 1)
                        + "/0" + endYear;
            } else {
                return strTemp + "/" + (calendar.get(Calendar.MONTH) + 1) + "/"
                        + endYear;
            }
        }
    }

    /**
     * Return date with format: d/m/yyyy
     *
     * @param ts
     * @return
     */
    public static String Timestamp2DMYYYY(Timestamp ts) {
        if (ts == null) {
            return "";
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new java.util.Date(ts.getTime()));

            String strTemp = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
            return strTemp + "/" + (calendar.get(Calendar.MONTH) + 1) + "/"
                    + calendar.get(Calendar.YEAR);
        }
    }

    public static String Timestamp2YYYYMMDDHH24MiSS(Timestamp ts) {
        if (ts == null) {
            return "";
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new java.util.Date(ts.getTime()));
            String strTemp = cal.get(Calendar.YEAR) + "";
            int month = cal.get(Calendar.MONTH) + 1;
            if (month < 10) {
                strTemp += "0" + month;
            } else {
                strTemp += month;
            }
            int day = cal.get(Calendar.DAY_OF_MONTH);
            if (day < 10) {
                strTemp += "0" + day;
            } else {
                strTemp += day;
            }
            //--
            if (cal.get(Calendar.HOUR_OF_DAY) < 10) {
                strTemp += "0" + cal.get(Calendar.HOUR_OF_DAY);
            } else {
                strTemp += cal.get(Calendar.HOUR_OF_DAY);
            }
            if (cal.get(Calendar.MINUTE) < 10) {
                strTemp += "0" + cal.get(Calendar.MINUTE);
            } else {
                strTemp += cal.get(Calendar.MINUTE);
            }
            if (cal.get(Calendar.SECOND) < 10) {
                strTemp += "0" + cal.get(Calendar.SECOND);
            } else {
                strTemp += cal.get(Calendar.SECOND);
            }
            return strTemp;

        }
    }

    /**
     * eturn date with format: YYYY/MM/DD to sort
     *
     * @param ts
     * @return
     */
    public static String Timestamp2YYYYMMDD(Timestamp ts) {
        int endYear;
        if (ts == null) {
            return "";
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new java.util.Date(ts.getTime()));

            String strTemp = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
            endYear = calendar.get(Calendar.YEAR);
            if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
                strTemp = "0" + strTemp;
            }
            if (calendar.get(Calendar.MONTH) + 1 < 10) {
                return endYear + "/0" + (calendar.get(Calendar.MONTH) + 1)
                        + "/" + strTemp;
            } else {
                return endYear + "/" + (calendar.get(Calendar.MONTH) + 1) + "/"
                        + strTemp;
            }
        }
    }

    /**
     * Author: toantt
     *
     * @param ts Timestapm to convert
     * @param iStyle 0: 24h, otherwise 12h clock
     * @return
     */
    public static String Timestamp2HHMMSS(Timestamp ts, int iStyle) {
        if (ts == null) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date(ts.getTime()));

        String strTemp;
        if (iStyle == 0) {
            strTemp = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        } else {
            strTemp = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        }

        if (strTemp.length() < 2) {
            strTemp = "0" + strTemp;
        }
        if (calendar.get(Calendar.MINUTE) < 10) {
            strTemp += ":0" + calendar.get(Calendar.MINUTE);
        } else {
            strTemp += ":" + calendar.get(Calendar.MINUTE);
        }
        if (calendar.get(Calendar.SECOND) < 10) {
            strTemp += ":0" + calendar.get(Calendar.SECOND);
        } else {
            strTemp += ":" + calendar.get(Calendar.SECOND);
        }

        if (iStyle != 0) {
            if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                strTemp += " AM";
            } else {
                strTemp += " PM";
            }
        }
        return strTemp;
    }

    /**
     * return date time used for 24 hour clock
     *
     * @param ts
     * @return
     */
    public static String getDateTime24hString(Timestamp ts) {
        if (ts == null) {
            return "";
        }
        return Timestamp2DDMMYYYY(ts) + " " + Timestamp2HHMMSS(ts, 0);
    }

    /**
     * return date time used for 12 hour clock
     *
     * @param ts
     * @return
     */
    public static String getDateTime12hString(Timestamp ts) {
        if (ts == null) {
            return "";
        }
        return Timestamp2DDMMYYYY(ts) + " " + Timestamp2HHMMSS(ts, 1);
    }

    /**
     * return string dd/mm/yyyy from a Timestamp + a addtional day
     *
     * @param ts
     * @param iDayPlus number of day to add
     * @return
     */
    public static String TimestampPlusDay2DDMMYYYY(Timestamp ts, int iDayPlus) {
        if (ts == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date(ts.getTime()));
        int iDay = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, iDay + iDayPlus);

        Timestamp tsNew = new Timestamp((cal.getTime()).getTime());
        return Timestamp2DDMMYYYY(tsNew);
    }

    /**
     * getPreviousDate
     *
     * @param ts
     * @param previos : So ngay lui lai
     * @return
     */
    public static Timestamp getPreviousDate(Timestamp ts, int previos) {
        if (ts == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date(ts.getTime()));
        int iDay = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, iDay - previos);

        Timestamp tsNew = new Timestamp((cal.getTime()).getTime());
        return tsNew;
    }

    /**
     * So ngay tien len
     *
     * @param ts
     * @param nextdate
     * @return
     */
    public static Timestamp getNextDate(Timestamp ts, int nextdate) {
        if (ts == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date(ts.getTime()));
        int iDay = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, iDay + nextdate);

        Timestamp tsNew = new Timestamp((cal.getTime()).getTime());
        return tsNew;
    }

    /**
     *
     * @param ts
     * @return
     */
    public static int getDayOfWeek(Timestamp ts) {
        if (ts == null) {
            return -1;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date(ts.getTime()));
        int iDay = cal.get(Calendar.DAY_OF_WEEK);
        return iDay;
    }

    /**
     *
     * @param ts
     * @return
     */
    public static int getDay(Timestamp ts) {
        if (ts == null) {
            return -1;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date(ts.getTime()));
        int iDay = cal.get(Calendar.DAY_OF_MONTH);
        return iDay;
    }

    /**
     *
     * @param ts
     * @return
     */
    public static int getMonth(Timestamp ts) {
        if (ts == null) {
            return -1;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date(ts.getTime()));
        int iMonth = cal.get(Calendar.MONTH);
        return iMonth + 1;
    }

    /**
     *
     * @param ts
     * @return
     */
    public static int getYear(Timestamp ts) {
        if (ts == null) {
            return -1;
        }
        Calendar _cal = Calendar.getInstance();
        _cal.setTime(new java.util.Date(ts.getTime()));
        int iYear = _cal.get(Calendar.YEAR);
        return iYear;
    }

    /**
     *
     * @param ts
     * @return
     */
    public static int getHour(Timestamp ts) {
        if (ts == null) {
            return -1;
        }
        Calendar _cal = Calendar.getInstance();
        _cal.setTime(new java.util.Date(ts.getTime()));
        int iHour = _cal.get(Calendar.HOUR);
        return iHour;
    }

    /**
     *
     * @param ts
     * @return
     */
    public static int getMinute(Timestamp ts) {
        if (ts == null) {
            return -1;
        }
        Calendar _cal = Calendar.getInstance();
        _cal.setTime(new java.util.Date(ts.getTime()));
        int iMinute = _cal.get(Calendar.MINUTE);
        return iMinute;
    }

    /**
     * Date Format String : dd/MM/yyyy HH:mm
     *
     * @param dateStr
     * @return
     */
    public static Timestamp getTimestampInDate(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Timestamp date = new Timestamp(dateFormat.parse(dateStr, new ParsePosition(0)).getTime());
            return date;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * CompareDate With Current Date
     * <br/> Less than CurrentDate return False ELSE > return true
     *
     * @param timeComp
     * @return
     */
    public static boolean compareDate(Timestamp timeComp) {
        if (timeComp == null) {
            return false;
        }
        boolean result = true;
        Calendar _currCal = Calendar.getInstance();
        Calendar _inputCal = Calendar.getInstance();
        _inputCal.setTime(new java.util.Date(timeComp.getTime()));

        int currDay = _currCal.get(Calendar.DAY_OF_MONTH);
        int currMonth = _currCal.get(Calendar.MONTH) + 1;
        int currYear = _currCal.get(Calendar.YEAR);
        int currHour = _currCal.get(Calendar.HOUR_OF_DAY);
        int currMinute = _currCal.get(Calendar.MINUTE);

        int creDay = _inputCal.get(Calendar.DAY_OF_MONTH);
        int creMonth = _inputCal.get(Calendar.MONTH) + 1;
        int creYear = _inputCal.get(Calendar.YEAR);
        int creHour = _inputCal.get(Calendar.HOUR_OF_DAY);
        int creMinute = _inputCal.get(Calendar.MINUTE);

        if (creYear < currYear) {
            result = false;
        } else if (creYear == currYear && creMonth < currMonth) {
            result = false;
        } else if (creYear == currYear && creMonth == currMonth
                && creDay < currDay) {
            result = false;
        } else if (creYear == currYear && creMonth == currMonth
                && creDay == currDay && creHour < currHour) {
            result = false;
        } else {
            result = creYear != currYear || creMonth != currMonth || creDay != currDay || creHour != currHour || creMinute >= currMinute;
        }
        return result;
    }

    /**
     * return the dd/mm/yyyy of current month eg: 05/2002 --> 31/05/2002
     *
     * @param strMonthYear : input string mm/yyyy
     * @return
     */
    public static String getLastestDateOfMonth(String strMonthYear) {
        String strDate = strMonthYear;
        int i, nYear, nMonth, nDay;
        String strSub = null;

        i = strDate.indexOf("/");
        if (i < 0) {
            return "";
        }
        strSub = strDate.substring(0, i);
        nMonth = (new Integer(strSub)); // Month begin from 0 value
        strDate = strDate.substring(i + 1);
        nYear = (new Integer(strDate));

        boolean leapyear = false;
        if (nYear % 100 == 0) {
            if (nYear % 400 == 0) {
                leapyear = true;
            }
        } else if ((nYear % 4) == 0) {
            leapyear = true;
        }

        if (nMonth == 2) {
            if (leapyear) {
                return "29/" + nMonth + "/" + strDate;
            } else {
                return "28/" + nMonth + "/" + strDate;
            }
        } else {
            if ((nMonth == 1) || (nMonth == 3) || (nMonth == 5) || (nMonth == 7)
                    || (nMonth == 8) || (nMonth == 10) || (nMonth == 12)) {
                return "31/" + nMonth + "/" + strDate;
            } else if ((nMonth == 4) || (nMonth == 6) || (nMonth == 9)
                    || (nMonth == 11)) {
                return "30/" + nMonth + "/" + strDate;
            }
        }
        return "";
    }

    /**
     * Get Friday theo Week From T2-CN
     *
     * @param ts
     * @return
     */
    public static Timestamp getFriday(Timestamp ts) {
        if (ts == null) {
            return null;
        }

        Calendar _cal = Calendar.getInstance();
        int iDoW = getDayOfWeek(ts);
        if (iDoW == Calendar.SUNDAY) {
            iDoW = 8;
        }
        int k = Calendar.FRIDAY - iDoW;
        _cal.setTime(new java.util.Date(ts.getTime()));
        int iDay = _cal.get(Calendar.DAY_OF_MONTH);
        _cal.set(Calendar.DAY_OF_MONTH, iDay + k);
        Timestamp tsNew = new Timestamp((_cal.getTime()).getTime());
        return tsNew;
    }

    /**
     * Check Xem ngay truyen vao co phai thu 6 hay ko?
     *
     * @param ts
     * @return
     */
    public static boolean isFriday(Timestamp ts) {
        if (ts == null) {
            return false;
        }
        return getDayOfWeek(ts) == Calendar.FRIDAY;
    }

    /**
     * Lay ra 1 String theo yyyyMMddhh24miss
     *
     * @return
     */
    public static String getDateTimeForName() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        Timestamp ts = new Timestamp((calendar.getTime()).getTime());
        String str = ts.toString();
        str = str.replaceAll("-", "");
        str = str.replaceAll(":", "");
        str = str.replaceAll(" ", "");
        str = str.replaceAll("\\.", "");
        return str;
    }

    public static String getStringLongTime() {
        Date d = new Date();
        long tem = d.getTime();
        return String.valueOf(tem);
    }

}
