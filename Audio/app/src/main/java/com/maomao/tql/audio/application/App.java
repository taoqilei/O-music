package com.maomao.tql.audio.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.maomao.tql.audio.service.PlayService;

public class App extends Application {
    public static Context sContext;
    private static  App mInstance;
    public static int sScreenWidth;
    public static int sScreenHeight;

    @Override
    public void onCreate(){
        super.onCreate();

        sContext = getApplicationContext();

        startService(new Intent(this,PlayService.class));
        //startService Download.class

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        sScreenWidth = dm.widthPixels;
        sScreenHeight = dm.heightPixels;
    }

    public static Context getInstance(){
        if (mInstance == null){
            mInstance = new App();
        }
        return mInstance;
    }
}
