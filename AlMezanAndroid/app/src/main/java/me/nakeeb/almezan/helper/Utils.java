package me.nakeeb.almezan.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import me.nakeeb.almezan.model.DateItem;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class Utils {

    public static ArrayList<DateItem> getYears(long currentMillis, long oldMillis, Locale locale){

        ArrayList<DateItem> temp = new ArrayList<>();

        SimpleDateFormat formatter = new SimpleDateFormat("y", locale);
        Calendar calendar = new GregorianCalendar(locale);

        calendar.setTimeInMillis(oldMillis);
        Log.d("oldHandMillis", String.valueOf(oldMillis));
        Log.d("curHandMillis", String.valueOf(currentMillis));

        while (calendar.getTime().before(new Date(currentMillis)))
        {
            Date result = calendar.getTime();

            DateItem dateItem = new DateItem();
            dateItem.date = formatter.format(result);
            dateItem.timeInMillis = result.getTime();

            temp.add(dateItem);

            Log.d("daysFormat", result.toString());
            Log.d("daysFormatsss", String.valueOf(result.getTime()));

            calendar.add(Calendar.YEAR, 1);
        }

        calendar.setTimeInMillis(currentMillis);
        temp.add(new DateItem(calendar.getTime().getTime(), formatter.format(calendar.getTime())));


        return temp;
    }

    public static ArrayList<DateItem> getDates(long currentMillis, long oldMillis, Locale locale){

        ArrayList<DateItem> temp = new ArrayList<>();

        SimpleDateFormat formatter = new SimpleDateFormat("EEEE \n dd, MMMM y", locale);
        Calendar calendar = new GregorianCalendar(locale);

        calendar.setTimeInMillis(oldMillis);

        while (calendar.getTime().before(new Date(currentMillis)))
        {
            Date result = calendar.getTime();

            DateItem dateItem = new DateItem();
            dateItem.date = formatter.format(result);
            dateItem.timeInMillis = result.getTime();

            temp.add(dateItem);

            Log.d("daysFormat", result.toString());
            Log.d("daysFormatsss", String.valueOf(result.getTime()));

            calendar.add(Calendar.DATE, 1);
        }

        calendar.setTimeInMillis(currentMillis);
        temp.add(new DateItem(calendar.getTime().getTime(), formatter.format(calendar.getTime())));


        return temp;
    }

    public static long getDayMillis (long timeInMilliSeconds){

        long dayInMilliseconds = 0;

        SimpleDateFormat dayFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        dayFormatter.setTimeZone(TimeZone.getDefault());

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMilliSeconds);
        String dayMonthYear = dayFormatter.format(calendar.getTime());

        try {

            Date dDate = dayFormatter.parse(dayMonthYear);
            dayInMilliseconds = dDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dayInMilliseconds;

    }

    public static int getDateNo(long dateInMillis, String pattern){

        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.US);
        Calendar calendar = new GregorianCalendar(Locale.US);

        calendar.setTimeInMillis(dateInMillis);

        Date result = calendar.getTime();
        return Integer.valueOf(formatter.format(result));

    }

    public static long getMillis (long timeInMilliSeconds, String pattern){

        long dayInMilliseconds = 0;

        SimpleDateFormat dayFormatter = new SimpleDateFormat(pattern, Locale.US);
        dayFormatter.setTimeZone(TimeZone.getDefault());

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMilliSeconds);
        String dayMonthYear = dayFormatter.format(calendar.getTime());

        try {

            Date dDate = dayFormatter.parse(dayMonthYear);
            dayInMilliseconds = dDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dayInMilliseconds;

    }

    public static ArrayList<Integer> getDaysMonthsYearsNo(long currentMillis, long oldMillis){

        ArrayList<Integer> temp = new ArrayList<>();

        int days = (int) TimeUnit.MILLISECONDS.toDays(Math.abs(currentMillis - oldMillis)) + 1;

        int months = days / 30;
        int years = months / 12;

        months = months - years * 12;
        days = days - months * 30;

        temp.add(days);
        temp.add(months);
        temp.add(years);

        return temp;

    }

    public static ArrayList<Integer> getDate(long milliSeconds){

        ArrayList<Integer> temp = new ArrayList<>();

        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(milliSeconds);  //here your time in miliseconds

        temp.add(cl.get(Calendar.DAY_OF_MONTH));
        temp.add(cl.get(Calendar.MONTH));
        temp.add(cl.get(Calendar.YEAR));

        return temp;

    }

    public static float round(float value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public static int calcAge(String dob){
        int age = 0;
        Calendar calendar = Calendar.getInstance();


        Log.d("dob", dob);

        Log.d("dobDay", dob.substring(0, dob.indexOf('/')));

        Log.d("dobMonth", dob.substring(dob.indexOf('/') + 1, dob.lastIndexOf('/')));

        Log.d("dobYear", dob.substring(dob.lastIndexOf('/') + 1, dob.length()));

        int dobDay = Integer.parseInt(dob.substring(0, dob.indexOf('/')));
        int dobMonth = Integer.parseInt(dob.substring(dob.indexOf('/') + 1, dob.lastIndexOf('/')));
        int dobYear = Integer.parseInt(dob.substring(dob.lastIndexOf('/') + 1, dob.length()));

        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        int curMonth = calendar.get(Calendar.MONTH);
        int curYear = calendar.get(Calendar.YEAR);

        if (curMonth > dobMonth){
            age = curYear - dobYear;
        }
        else if (curMonth < dobMonth){
            age = curYear - dobYear - 1;
        }
        else if (curMonth == dobMonth){
            if (curDay >= dobDay){
                age = curYear - dobYear;
            }
            else {
                age = curYear - dobYear - 1;
            }
        }

        return age;
    }

    public static void logout(Context context){

        SharedPreferences.Editor editor = context.getSharedPreferences("User", MODE_PRIVATE).edit();
        editor.putBoolean("login", false);
        editor.apply();

    }
}
