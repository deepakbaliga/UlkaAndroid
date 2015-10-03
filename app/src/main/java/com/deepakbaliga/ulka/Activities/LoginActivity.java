package com.deepakbaliga.ulka.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deepakbaliga.ulka.App;
import com.deepakbaliga.ulka.MeteorSingleton;
import com.deepakbaliga.ulka.R;
import com.deepakbaliga.ulka.Utils.RotateLoading;

import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.SubscribeListener;
import me.alexrs.prefs.lib.Prefs;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;

    private TextView ulka;
    private TextView loginMessage;

    private RelativeLayout subParentLayout;
    private RotateLoading rotateLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

    }

    private void initViews() {

        email = (EditText) findViewById(R.id.login_screen_email);
        password = (EditText) findViewById(R.id.login_screen_password);
        login = (Button) findViewById(R.id.login_screen_login);
        ulka = (TextView) findViewById(R.id.login_screen_ulka_title);
        loginMessage = (TextView) findViewById(R.id.login_screen_login_title);
        subParentLayout = (RelativeLayout) findViewById(R.id.topRelativeLayout);
        rotateLoading = (RotateLoading) findViewById(R.id.rotateloading);


        email.setTypeface(App.montUltraLight);
        password.setTypeface(App.montUltraLight);
        login.setTypeface(App.montUltraLight);
        ulka.setTypeface(App.montRegular);
        loginMessage.setTypeface(App.montLight);
    }

    public void onLogin(View view) {

        App.vibe.vibrate(50);

        if (isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();

        } else if (isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else {
            //Login

            subParentLayout.setVisibility(View.INVISIBLE);
            rotateLoading.setVisibility(View.VISIBLE);
            rotateLoading.start();

            MeteorSingleton.getInstance().loginWithEmail(email.getText().toString(),
                    password.getText().toString(),
                    new ResultListener() {
                        @Override
                        public void onSuccess(String s) {
                            Log.e("MeteorLogin", s);
                            Log.e("MeteorID", MeteorSingleton.getInstance().getUserId() + "");

                            Prefs.with(LoginActivity.this).save("loggedin", true);
                            Prefs.with(LoginActivity.this).save("id", MeteorSingleton.getInstance().getUserId() + "");

                            if (Prefs.with(LoginActivity.this).getBoolean("loggedin", false))
                                MeteorSingleton.getInstance().subscribe("message", new Object[]{Prefs.with(LoginActivity.this).getString("id", null)}, new SubscribeListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.e("MeteorService", "Subscribed to 'messages'");
                                    }

                                    @Override
                                    public void onError(String s, String s1, String s2) {
                                        Log.e("MeteorService", "Code " + s);
                                        Log.e("MeteorService", "Message " + s1);
                                        Log.e("MeteorService", "Error " + s2);
                                    }
                                });

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();

                        }

                        @Override
                        public void onError(String s, String s1, String s2) {
                            Log.e("MeteorLogin", s);
                            Log.e("MeteorLogin", s1);
                        }
                    });

        }


    }

    private boolean isEmpty(EditText editText) {

        return editText.getText().toString().equals("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        rotateLoading.stop();
    }
}
