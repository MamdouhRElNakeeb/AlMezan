package me.nakeeb.almezan.helper;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;

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

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class Utils {

    public static void changeLocale(Context context, String locale) {
        Resources res = context.getResources();
        Configuration conf = res.getConfiguration();
        conf.locale = new Locale(locale);
        res.updateConfiguration(conf, res.getDisplayMetrics());
    }

    public static long currentMillis(){

        long x = System.currentTimeMillis();

        return x;
    }

    public static ArrayList<DateItem> getYears(long currentMillis, long oldMillis, Locale locale){

        ArrayList<DateItem> temp = new ArrayList<>();

        int yearsNo = yearsNo(getDayMillis(currentMillis), getDayMillis(oldMillis));

        if (yearsNo == 0){
            yearsNo = 1;
        }

        Log.d("yearsNo", String.valueOf(yearsNo));

        SimpleDateFormat formatter = new SimpleDateFormat("y", locale);
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < yearsNo; i++){

            calendar.setTimeInMillis(oldMillis + 32140800000L * i);

            DateItem dateItem = new DateItem();
            dateItem.date = formatter.format(calendar.getTime());
            dateItem.timeInMillis = oldMillis + 32140800000L * i;

            temp.add(dateItem);

            Log.d("daysFormat", temp.get(i).date);
            Log.d("daysFormatsss", String.valueOf(oldMillis + 32140800000L * i));
        }

        return temp;
    }

    public static ArrayList<DateItem> getDates(long currentMillis, long oldMillis, Locale locale){

        ArrayList<DateItem> temp = new ArrayList<>();

        int daysNo = daysNo(getDayMillis(currentMillis), getDayMillis(oldMillis));

        if (daysNo == 0){
            daysNo = 1;
        }

        Log.d("daysNo", String.valueOf(daysNo));

        SimpleDateFormat formatter = new SimpleDateFormat("EEEE \n dd, MMMM y", locale);
        Calendar calendar = new GregorianCalendar();

        calendar.setTimeInMillis(oldMillis);

//        for (int i = 0; i <= daysNo; i++){
//
//            calendar.setTimeInMillis(oldMillis + 86400000 * i);
//
//            DateItem dateItem = new DateItem();
//            dateItem.date = formatter.format(calendar.getTime());
//            dateItem.timeInMillis = oldMillis + 86400000 * i;
//
//            temp.add(dateItem);
//
//            Log.d("daysFormat", temp.get(i).date);
//            Log.d("daysFormatsss", String.valueOf(oldMillis + 86400000 * i));
//        }

        temp.add(new DateItem(calendar.getTime().getTime(), formatter.format(calendar.getTime())));

        calendar.add(Calendar.DATE, 1);

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

        Log.d("timeInMillis", String.valueOf(dayInMilliseconds));

        return dayInMilliseconds;

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

        Log.d("timeInMillis", String.valueOf(dayInMilliseconds));

        return dayInMilliseconds;

    }

    public static ArrayList<Integer> getDaysMonthsYearsNo(long currentMillis, long oldMillis){

        ArrayList<Integer> temp = new ArrayList<>();

        int days = (int) TimeUnit.MILLISECONDS.toDays(Math.abs(currentMillis - oldMillis)) + 1;
//
//        int monthsNo = daysNo / 30;
//
//        int yearsNo = daysNo / 365;


        long totalsec = (currentMillis - oldMillis) / 1000;

//        int days = (int) ((totalsec / (1000 * 60 * 60 * 24)) % 7);
//        int weeks = (int) (totalsec / (1000 * 60 * 60 * 24 * 7));
        int months = days / 30; // weeks / 30;
        int years = months / 12;

        months = months - years * 12;

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

    public static ArrayList<String> getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
//        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, new Locale("ar", "EG"));
//
//        // Create a calendar object that will convert the date and time value in milliseconds to date.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(milliSeconds);
//
//
//        Calendar cal = new UmmalquraCalendar();
////        cal.setTimeInMillis(milliSeconds);
//        cal.setTime(calendar.getTime());

        ArrayList<String> temp = new ArrayList<String>();
//        formatter.setCalendar(calendar);
//        temp.add(formatter.format(calendar.getTime()));
////        formatter.setCalendar(cal);
//        temp.add(formatter.format(cal.getTime()));



//        GregorianCalendar gCal = new GregorianCalendar(2012, Calendar.FEBRUARY, 12);
//        Calendar uCal = new UmmalquraCalendar();
//        uCal.setTime(gCal.getTime());
//
//        temp.add(formatter.format(uCal.getTime()));
////        formatter.setCalendar(cal);
//        temp.add(formatter.format(uCal.getTime()));



        GregorianCalendar gCal = new GregorianCalendar(2012, 1, 12);
        Calendar uCal = new UmmalquraCalendar();
        uCal.setTime(gCal.getTime());

        uCal.get(Calendar.YEAR);         // 1433
        uCal.get(Calendar.MONTH);        // 2
        uCal.get(Calendar.DAY_OF_MONTH); // 20

        temp.add(uCal.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("ar")));
        temp.add(uCal.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("ar")));

        return temp;
    }

    public static int daysNo(long currentMillis, long oldMillis){

        return (int) ((currentMillis - oldMillis) / 86400000);

    }

    public static int yearsNo(long currentMillis, long oldMillis){

        return (int) ((currentMillis - oldMillis) / 32140800000L);

    }

    public static ArrayList<Integer> miladiToHegri(int day, int month, int year){

        ArrayList<Integer> temp = new ArrayList<>();

        GregorianCalendar gCal = new GregorianCalendar(2017, Calendar.JUNE, 2);
        Calendar uCal = new UmmalquraCalendar();
        uCal.setTime(gCal.getTime());

        uCal.get(Calendar.YEAR);         // 1433
        uCal.get(Calendar.MONTH);        // 2
        uCal.get(Calendar.DAY_OF_MONTH); // 20

        temp.add(Calendar.DAY_OF_MONTH);
        temp.add(Calendar.MONTH);
        temp.add(Calendar.YEAR);

        return temp;
    }

    public static ArrayList<Integer> hegriToMiladi(int day, int month, int year){

        ArrayList<Integer> temp = new ArrayList<>();

        Calendar uCal = new UmmalquraCalendar(year, month - 1, day);
        GregorianCalendar gCal = new GregorianCalendar();
        gCal.setTime(uCal.getTime());

        gCal.get(Calendar.YEAR);         // 2012
        gCal.get(Calendar.MONTH);        // 1
        gCal.get(Calendar.DAY_OF_MONTH); // 7

        temp.add(Calendar.DAY_OF_MONTH);
        temp.add(Calendar.MONTH);
        temp.add(Calendar.YEAR);

        return temp;
    }

    public static int calcAge(String dob){
        int age = 0;
        Calendar calendar = Calendar.getInstance();

        Log.d("dob", dob);
        int dobDay = Integer.parseInt(dob.substring(0, dob.indexOf('/')));

        dob  = dob.replace(dob.substring(0, dob.indexOf('/') + 1), "");

        Log.d("dob", dob);
        int dobMonth = Integer.parseInt(dob.substring(0, dob.indexOf('/')));

        dob  = dob.replace(dob.substring(0, dob.indexOf('/') + 1), "");

        Log.d("dob", dob);
        int dobYear = Integer.parseInt(dob.substring(0, 4));

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
}
