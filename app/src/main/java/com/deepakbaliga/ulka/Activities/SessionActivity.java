package com.deepakbaliga.ulka.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.deepakbaliga.ulka.R;

import me.alexrs.prefs.lib.Prefs;

public class SessionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        if(Prefs.with(this).getBoolean("loggedin",false)){
            startActivity(new Intent(SessionActivity.this, MainActivity.class));
            finish();

        }else{
            startActivity(new Intent(SessionActivity.this, WelcomeActivity.class));
            finish();

        }
    }
}
