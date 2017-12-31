package me.nakeeb.almezan.helper;

import android.app.Application;
import android.content.Context;

/**
 * Created by mamdouhelnakeeb on 12/30/17.
 */

public class Main extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

}
