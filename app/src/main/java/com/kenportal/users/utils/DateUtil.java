package com.kenportal.users.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kranti on 11/06/2016.
 */
public class DateUtil {

    public static String getNoDays(String prevDate) {
        String dayDifference = "";

        //Current date
        Calendar c = Calendar.getInstance(Locale.getDefault());
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());//current date
        System.out.println(prevDate);

        //getting prev date
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            Date parsedDate = sdf.parse(prevDate);
            SimpleDateFormat print = new SimpleDateFormat("dd-MM-yyyy");
            System.out.println(print.format(parsedDate));//previous date
            dayDifference = get_count_of_days(formattedDate, print.format(parsedDate));
            //Log.d("Difference:-->", formattedDate + " - " + print.format(parsedDate) + " = " + dayDifference);
        } catch (ParseException exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }

        //Log.d("Difference:-->", formattedDate+" + "+print.format(parsedDate)+" = "+dayDifference);
        return dayDifference;
    }

    //get difference between dates
    private static String get_count_of_days(String Created_date_String, String Expire_date_String) {


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        Date Created_convertedDate = null, Expire_CovertedDate = null, todayWithZeroTime = null;
        try {
            Created_convertedDate = dateFormat.parse(Created_date_String);
            Expire_CovertedDate = dateFormat.parse(Expire_date_String);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int c_year = 0, c_month = 0, c_day = 0;

        if (Created_convertedDate.after(todayWithZeroTime)) {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(Created_convertedDate);

            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(todayWithZeroTime);

            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);
        }
        Calendar e_cal = Calendar.getInstance();
        e_cal.setTime(Expire_CovertedDate);

        int e_year = e_cal.get(Calendar.YEAR);
        int e_month = e_cal.get(Calendar.MONTH);
        int e_day = e_cal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(c_year, c_month, c_day);
        date2.clear();
        date2.set(e_year, e_month, e_day);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);


        return ("" + (int) dayCount);
    }
}
