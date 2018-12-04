package com.virtusa.assesment;

import android.app.Application;
import android.content.Context;

/**
 * Created by Anil on 12/3/2018.
 */

public class BaseApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContextObject() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}