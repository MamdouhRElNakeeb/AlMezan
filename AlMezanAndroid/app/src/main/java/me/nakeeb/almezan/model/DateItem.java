package me.nakeeb.almezan.model;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class DateItem {

    public long timeInMillis = 0L;
    public String date = "";

    public DateItem(){

    }

    public DateItem(long timeInMillis, String date){

        this.timeInMillis = timeInMillis;
        this.date = date;
    }

}
