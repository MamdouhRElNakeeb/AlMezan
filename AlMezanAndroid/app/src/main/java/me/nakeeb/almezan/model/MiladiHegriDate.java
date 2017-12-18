package me.nakeeb.almezan.model;

import java.util.ArrayList;

import me.nakeeb.almezan.helper.Utils;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class MiladiHegriDate {

    int dayM = 0;
    int monthM = 0;
    int yearM = 0;

    int dayH = 0;
    int monthH = 0;
    int yearH = 0;

    public MiladiHegriDate(int day, int month, int year, boolean miladi){

        if (miladi) {
            ArrayList<Integer> temp = Utils.miladiToHegri(day, month, year);

            dayM = day;
            monthM = month;
            yearM = year;

            dayH = temp.get(0) - 1;
            monthH = temp.get(1) - 2;
            yearH = temp.get(2);
        }
        else {
            ArrayList<Integer> temp = Utils.hegriToMiladi(day, month, year);

            dayH = day;
            monthH = month;
            yearH = year;

            dayM = temp.get(0) - 1;
            monthM = temp.get(1) - 2;
            yearM = temp.get(2);
        }

    }
}
