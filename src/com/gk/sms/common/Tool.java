/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gk.sms.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

public class Tool {

    private static final Random RANDOM = new SecureRandom();

    public static void debug(String input) {
        System.out.println(input);
    }

    public static void debug(int input) {
        System.out.println(input);
    }

    public static int randomInt(int max) {
        Random rn = new Random();
        int result = rn.nextInt(max);
        return result;
    }

    public static String loadUrl(String urlStr) {
        String t = "";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(20000);
            try (InputStream in = http.getInputStream()) {
                t = convertStreamToString(in);
            }
            http.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static String convertStreamToString(InputStream is) throws IOException {
        /*
         * To convert the InputStream to String we use the
         * Reader.read(char[] buffer) method. We iterate until the
         * Reader return -1 which means there's no more data to
         * read. We use the StringWriter class to produce the string.
         */
        if (is != null) {
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    private boolean checkOptionIsNumber(String sOption) {
        boolean flag = true;
        try {
            for (int i = 0; i < sOption.length(); i++) {
                char[] arr = sOption.toCharArray();
                try {
                    int iCheck = Integer.parseInt(String.valueOf(arr[i]));
                } catch (NumberFormatException ex) {
                    flag = false;
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    public static String generateRandomPassword(int leng) {
        // Pick from some letters that won't be easily mistaken for each
        // other. So, for example, omit o O and 0, 1 l and L.
        String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";

        String pw = "";
        for (int i = 0; i < leng; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            pw += letters.substring(index, index + 1);
        }
        return pw;
    }

    public static String getLogMessage(Exception ex) {
        StackTraceElement[] trace = ex.getStackTrace();
        String str = DateProc.createTimestamp() + "||" + ex.getMessage();
        for (StackTraceElement trace1 : trace) {
            str += trace1 + "\n";
        }
        return str;
    }

    public static boolean getBoolean(String status) {
        return status != null && status.equals("1");
    }

    public static boolean checkNull(final String input) {
        return input == null || input.equals("") || input.equalsIgnoreCase("null");
    }

    public static boolean checkNull(final Object input) {
        return input == null;
    }

    public static String getString(String input) {
        if (input != null) {
            input = input.trim();
        } else {
            input = "";
        }
        return input;
    }

    public static String getString(String input, String defaultval) {
        if (input != null) {
            input = input.trim();
        } else {
            input = defaultval;
        }
        return input;
    }

    public static int getInt(final String input) {
        int tem = 0;
        try {
            tem = Integer.parseInt(input.trim());
        } catch (NumberFormatException | NullPointerException e) {
            tem = 0;
        }
        return tem;
    }

    public static int getInt(final String input, final int defaultVal) {
        int tem = 0;
        try {
            tem = Integer.parseInt(input.trim());
        } catch (NumberFormatException | NullPointerException e) {
            tem = defaultVal;
        }
        return tem;
    }

    public static long getLong(final String input) {
        long tem = 0;
        try {
            tem = Long.parseLong(input);
        } catch (NumberFormatException | NullPointerException e) {
            tem = 0;
        }
        return tem;
    }

    public static String list2String(final ArrayList<String> list, final String space) {
        String str = "";
        if (!checkNull(list)) {
            for (String one : list) {
                str += one;
                if (!checkNull(space)) {
                    str += space;
                }
            }
            if (!checkNull(space)) {
                if (str.endsWith(space)) {
                    str = str.substring(0, str.length() - space.length());
                }
            }
        }

        return str;
    }

    public static String array2String(final String[] list, final String space) {
        String str = "";
        if (!checkNull(list)) {
            for (String one : list) {
                str += one;
                if (!checkNull(space)) {
                    str += space;
                }
            }
            if (!checkNull(space)) {
                if (str.endsWith(space)) {
                    str = str.substring(0, str.length() - space.length());
                }
            }
        }

        return str;
    }

    public static String stringToHTMLString(final String string) {
        StringBuilder sb = new StringBuilder(string.length());
        // true if last char was blank
        boolean lastWasBlankChar = false;
        int len = string.length();
        char c;

        for (int i = 0; i < len; i++) {
            c = string.charAt(i);
            if (c == ' ') {
                // blank gets extra work,
                // this solves the problem you get if you replace all
                // blanks with &nbsp;, if you do that you loss
                // word breaking
                if (lastWasBlankChar) {
                    lastWasBlankChar = false;
                    sb.append("&nbsp;");
                } else {
                    lastWasBlankChar = true;
                    sb.append(' ');
                }
            } else {
                lastWasBlankChar = false;
                //
                // HTML Special Chars
                if (c == '"') {
                    sb.append("&quot;");
                } else if (c == '&') {
                    sb.append("&amp;");
                } else if (c == '<') {
                    sb.append("&lt;");
                } else if (c == '>') {
                    sb.append("&gt;");
                } else if (c == '\n') // Handle Newline
                {
                    sb.append("&lt;br/&gt;");
                } else {
                    int ci = 0xffff & c;
                    if (ci < 160) // nothing special only 7 Bit
                    {
                        sb.append(c);
                    } else {
                        // Not 7 Bit use the unicode system
                        sb.append("&#");
                        sb.append(Integer.toString(ci));
                        sb.append(';');
                    }
                }
            }
        }
        return sb.toString();
    }

    public static String convert2NoSign(String org) {
        if (org == null) {
            org = "";
            return org;
        }
        char arrChar[] = org.toCharArray();
        char result[] = new char[arrChar.length];
        for (int i = 0; i < arrChar.length; i++) {
            switch (arrChar[i]) {
                case '\u00E1':
                case '\u00E0':
                case '\u1EA3':
                case '\u00E3':
                case '\u1EA1':
                case '\u0103':
                case '\u1EAF':
                case '\u1EB1':
                case '\u1EB3':
                case '\u1EB5':
                case '\u1EB7':
                case '\u00E2':
                case '\u1EA5':
                case '\u1EA7':
                case '\u1EA9':
                case '\u1EAB':
                case '\u1EAD':
                case '\u0203':
                case '\u01CE': {
                    result[i] = 'a';
                    break;
                }
                case '\u00E9':
                case '\u00E8':
                case '\u1EBB':
                case '\u1EBD':
                case '\u1EB9':
                case '\u00EA':
                case '\u1EBF':
                case '\u1EC1':
                case '\u1EC3':
                case '\u1EC5':
                case '\u1EC7':
                case '\u0207': {
                    result[i] = 'e';
                    break;
                }
                case '\u00ED':
                case '\u00EC':
                case '\u1EC9':
                case '\u0129':
                case '\u1ECB': {
                    result[i] = 'i';
                    break;
                }
                case '\u00F3':
                case '\u00F2':
                case '\u1ECF':
                case '\u00F5':
                case '\u1ECD':
                case '\u00F4':
                case '\u1ED1':
                case '\u1ED3':
                case '\u1ED5':
                case '\u1ED7':
                case '\u1ED9':
                case '\u01A1':
                case '\u1EDB':
                case '\u1EDD':
                case '\u1EDF':
                case '\u1EE1':
                case '\u1EE3':
                case '\u020F': {
                    result[i] = 'o';
                    break;
                }
                case '\u00FA':
                case '\u00F9':
                case '\u1EE7':
                case '\u0169':
                case '\u1EE5':
                case '\u01B0':
                case '\u1EE9':
                case '\u1EEB':
                case '\u1EED':
                case '\u1EEF':
                case '\u1EF1': {
                    result[i] = 'u';
                    break;
                }
                case '\u00FD':
                case '\u1EF3':
                case '\u1EF7':
                case '\u1EF9':
                case '\u1EF5': {
                    result[i] = 'y';
                    break;
                }
                case '\u0111': {
                    result[i] = 'd';
                    break;
                }
                case '\u00C1':
                case '\u00C0':
                case '\u1EA2':
                case '\u00C3':
                case '\u1EA0':
                case '\u0102':
                case '\u1EAE':
                case '\u1EB0':
                case '\u1EB2':
                case '\u1EB4':
                case '\u1EB6':
                case '\u00C2':
                case '\u1EA4':
                case '\u1EA6':
                case '\u1EA8':
                case '\u1EAA':
                case '\u1EAC':
                case '\u0202':
                case '\u01CD': {
                    result[i] = 'A';
                    break;
                }
                case '\u00C9':
                case '\u00C8':
                case '\u1EBA':
                case '\u1EBC':
                case '\u1EB8':
                case '\u00CA':
                case '\u1EBE':
                case '\u1EC0':
                case '\u1EC2':
                case '\u1EC4':
                case '\u1EC6':
                case '\u0206': {
                    result[i] = 'E';
                    break;
                }
                case '\u00CD':
                case '\u00CC':
                case '\u1EC8':
                case '\u0128':
                case '\u1ECA': {
                    result[i] = 'I';
                    break;
                }
                case '\u00D3':
                case '\u00D2':
                case '\u1ECE':
                case '\u00D5':
                case '\u1ECC':
                case '\u00D4':
                case '\u1ED0':
                case '\u1ED2':
                case '\u1ED4':
                case '\u1ED6':
                case '\u1ED8':
                case '\u01A0':
                case '\u1EDA':
                case '\u1EDC':
                case '\u1EDE':
                case '\u1EE0':
                case '\u1EE2':
                case '\u020E': {
                    result[i] = 'O';
                    break;
                }
                case '\u00DA':
                case '\u00D9':
                case '\u1EE6':
                case '\u0168':
                case '\u1EE4':
                case '\u01AF':
                case '\u1EE8':
                case '\u1EEA':
                case '\u1EEC':
                case '\u1EEE':
                case '\u1EF0': {
                    result[i] = 'U';
                    break;
                }

                case '\u00DD':
                case '\u1EF2':
                case '\u1EF6':
                case '\u1EF8':
                case '\u1EF4': {
                    result[i] = 'Y';
                    break;
                }
                case '\u0110':
                case '\u00D0':
                case '\u0089': {
                    result[i] = 'D';
                    break;
                }
                case (char) 160: {
                    result[i] = ' ';
                    break;
                }
                default:
                    result[i] = arrChar[i];
            }
        }
        return new String(result);
    }

    public static String replace(String sStr, String oldStr, String newStr) {
        sStr = (sStr == null ? "" : sStr);
        String strVar = sStr;
        String tmpStr = "";
        String finalStr = "";
        int stpos = 0, endpos = 0, strLen = 0;
        while (true) {
            strLen = strVar.length();
            stpos = 0;
            endpos = strVar.indexOf(oldStr, stpos);
            if (endpos == -1) {
                break;
            }
            tmpStr = strVar.substring(stpos, endpos);
            tmpStr = tmpStr.concat(newStr);
            strVar = strVar.substring(endpos + oldStr.length() > sStr.length() ? endpos : endpos + oldStr.length(), strLen);
            finalStr = finalStr.concat(tmpStr);
            stpos = endpos;
        }
        finalStr = finalStr.concat(strVar);
        return finalStr;
    }

    public static boolean validIP(String ipRequest, String ipAllow) {
        boolean flag = false;
        try {
            if (ipAllow != null) {
                String[] allIP = ipAllow.split(",");
                for (String one : allIP) {
                    if (ipRequest.equals(one)) {
                        flag = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
        }
        return flag;
    }
}
