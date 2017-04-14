package com.kenportal.users.urls;

import com.facebook.AccessToken;

/**
 * Created by kranti on 11/06/2016.
 */
public class ServerLinks {



    public static final String ServerUrl = "http://192.168.10.15/kenportal.2_service/Mobile.s";
    public static final String portalUrl = "http://192.168.10.15/kenportalv5.2"; //Server20 Staging Service
    public static final String dashboardServerUrl = "http://kenportalservice.com/Dashboard.s/"; //portal live dashboard service url
    public static final String FBWebViewUrl = portalUrl + "/kenportal_Fb__plugin.html";
    public static final String AttendanceWebViewUrl = portalUrl + "/DashboardStandard/attendanceReport.aspx";
    public static final String smallPhoto = portalUrl + "/EMPPHOTOS/50x66/";
    public static final String emp_pic_url = ServerUrl;
    public static final String greetng_img_url = portalUrl + "/Images/Greetings/90x121/";


    public static String latLong = "";

    public static AccessToken accessToken;


    public static String getSentencecaseString(String name) {
        String titleCaseValue = null;
        try {
            String[] words = name.split(" ");
            StringBuilder sb = new StringBuilder();
            if (words[0].length() > 0) {
                sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
                for (int i = 1; i < words.length; i++) {
                    sb.append(" ");
                    sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
                }
            }
            titleCaseValue = sb.toString();

        } catch (StringIndexOutOfBoundsException e) {
            titleCaseValue = name;
            e.printStackTrace();
        } catch (Exception e) {
            titleCaseValue = name;
            e.printStackTrace();
        }
        return titleCaseValue;
    }


}
