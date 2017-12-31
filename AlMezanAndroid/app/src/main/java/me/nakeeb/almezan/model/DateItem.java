package me.nakeeb.almezan.model;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class DateItem {

//    String day;
//    int number;
//    String month;
//    int year;

    public long timeInMillis = 0L;
    public String date = "";

    public DateItem(){

    }

    public DateItem(long timeInMillis, String date){

        this.timeInMillis = timeInMillis;
        this.date = date;
    }

//    public DateItem(String day, int number, String month, int year){
//
//        this.day = day;
//        this.number = number;
//        this.month = month;
//        this.year = year;
//
//    }
}
