package com.deepakbaliga.ulka.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepakbaliga.ulka.App;
import com.deepakbaliga.ulka.MeteorSingleton;
import com.deepakbaliga.ulka.R;

import im.delight.android.ddp.MeteorCallback;
import me.alexrs.prefs.lib.Prefs;
import tyrantgit.explosionfield.ExplosionField;

public class WelcomeActivity extends AppCompatActivity implements MeteorCallback {

    private ImageView ulkaHoloLogo;
    private TextView  ulkaTitle;
    private TextView  ulkaWelcomeMessage;
    private TextView  ulkaLogin;
    private TextView  ulkaSignup;
    private ExplosionField explosionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);



        //Initialise Views
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MeteorSingleton.getInstance().setCallback(this);

        Log.e("Test", MeteorSingleton.hasInstance() + "");

        if(MeteorSingleton.getInstance().isLoggedIn())
        {
            Log.e("Meteor", MeteorSingleton.getInstance().isLoggedIn()+"");
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    }


    private void initViews() {

        explosionField = ExplosionField.attach2Window(this);

        ulkaHoloLogo = (ImageView) findViewById(R.id.welcome_screen_ulka_holo_logo);
        ulkaTitle = (TextView) findViewById(R.id.welcome_screen_ulka_title);
        ulkaWelcomeMessage = (TextView) findViewById(R.id.welcome_screen_ulka_welcome_message);
        ulkaLogin = (TextView) findViewById(R.id.welcome_screen_login_button);
        ulkaSignup = (TextView) findViewById(R.id.welcome_screen_signup_button);

        ulkaTitle.setTypeface(App.montRegular);
        ulkaWelcomeMessage.setTypeface(App.montHairline);
        ulkaLogin.setTypeface(App.montUltraLight);
        ulkaSignup.setTypeface(App.montUltraLight);


        Animation slideUlka = AnimationUtils.loadAnimation(this, R.anim.ulka_anim);
        Animation fade = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation slideOutRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation slideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);


        ulkaHoloLogo.startAnimation(slideUlka);
        ulkaLogin.startAnimation(slideOutLeft);
        ulkaSignup.startAnimation(slideOutRight);
        ulkaWelcomeMessage.startAnimation(fade);
        ulkaTitle.startAnimation(fade);



    }


    //Login Fucntion
    public void login(View view){

        App.vibe.vibrate(50);

        explosionField.explode(ulkaHoloLogo);

        //Delay of 700 milliseconds
        //This is executed after explosion is over
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                WelcomeActivity.this.finish();

                try {

                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } catch (Exception e) {
                    Log.e("Exception",e.getLocalizedMessage()); }
            }
        }, 1000);

    }

    //Signup Fucntion
    public void signup(View view){

        App.vibe.vibrate(50);

        explosionField.explode(ulkaHoloLogo);

        //Delay of 700 milliseconds
        //This is executed after explosion is over
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, SignupActivity.class));
                WelcomeActivity.this.finish();

                try {

                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } catch (Exception e) {
                    Log.e("Exception", e.getLocalizedMessage());
                }
            }
        }, 1000);
    }


    @Override
    public void onConnect(boolean b) {

        if(MeteorSingleton.getInstance().isLoggedIn())
        {
            Log.e("Meteor", MeteorSingleton.getInstance().isLoggedIn()+"");
            Prefs.with(this).save("loggedin", true);
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }

    }

    @Override
    public void onDisconnect(int i, String s) {
        Log.e("onDisconnect", s);
    }

    @Override
    public void onDataAdded(String s, String s1, String s2) {

    }

    @Override
    public void onDataChanged(String s, String s1, String s2, String s3) {

    }

    @Override
    public void onDataRemoved(String s, String s1) {

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        MeteorSingleton.getInstance().unsetCallback(this);
        finish();
    }
}
