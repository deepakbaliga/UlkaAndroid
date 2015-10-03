package com.deepakbaliga.ulka;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.util.Log;

import com.deepakbaliga.ulka.Utils.MeteorUtils;

import im.delight.android.ddp.Meteor;

/**
 * Created by deezdroid on 27/09/15.
 */
public class App extends Application {

    public static Typeface montBlack;
    public static Typeface montBold;
    public static Typeface montHairline;
    public static Typeface montLight;
    public static Typeface montRegular;
    public static Typeface montUltraLight;
    public static Typeface montSemiBold;

    public static Vibrator vibe;

    @Override
    public void onCreate() {
        super.onCreate();

        //Initializing Typefaces
        initTypeFace();

        //Vibrator
         vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        startService(new Intent(this, MeteorService.class));
        Log.e("Service", "Service Started");
        Meteor.setLoggingEnabled(false);


    }

    private void initTypeFace(){


        montBlack = Typeface.createFromAsset(this.getAssets(), "Montserrat-Black.otf");
        montBold = Typeface.createFromAsset(this.getAssets(), "Montserrat-Bold.otf");
        montHairline = Typeface.createFromAsset(this.getAssets(), "Montserrat-Hairline.otf");
        montLight = Typeface.createFromAsset(this.getAssets(), "Montserrat-Light.otf");
        montRegular = Typeface.createFromAsset(this.getAssets(), "Montserrat-Regular.otf");
        montUltraLight = Typeface.createFromAsset(this.getAssets(), "Montserrat-UltraLight.otf");
        montSemiBold = Typeface.createFromAsset(this.getAssets(), "Montserrat-SemiBold.otf");

    }



}
