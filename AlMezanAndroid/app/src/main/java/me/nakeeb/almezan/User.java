package me.nakeeb.almezan;

import android.support.annotation.Keep;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

@Keep
public class User {


    public String name = "";
    public String email = "";
    public String dob = "";
    public long startTime = 0;

    public User(String name, String email, String dob, long startTime) {

        this.name = name;
        this.email = email;
        this.dob = dob;
        this.startTime = startTime;

    }

    public User(){

    }
}
