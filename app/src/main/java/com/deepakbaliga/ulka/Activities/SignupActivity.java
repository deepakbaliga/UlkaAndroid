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
import com.deepakbaliga.ulka.Utils.EmailValidator;
import com.deepakbaliga.ulka.Utils.RotateLoading;

import java.util.HashMap;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.SubscribeListener;
import me.alexrs.prefs.lib.Prefs;

public class SignupActivity extends AppCompatActivity {

    private EditText name;
    private EditText username;
    private EditText email;
    private EditText password;

    private TextView title;
    private TextView signupMessage;

    private Button signup;
    private EmailValidator emailValidator;

    private RelativeLayout subParentLayout;
    private RotateLoading rotateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        emailValidator = new EmailValidator();
        initViews();
    }

    private void initViews() {
        name = (EditText) findViewById(R.id.signup_screen_name);
        username = (EditText) findViewById(R.id.signup_screen_username);
        email = (EditText) findViewById(R.id.signup_screen_email);
        password = (EditText) findViewById(R.id.signup_screen_password);
        signup = (Button) findViewById(R.id.signup_screen_signup);
        title = (TextView) findViewById(R.id.signup_screen_ulka_title);
        signupMessage = (TextView) findViewById(R.id.signup_screen_signup_title);
        subParentLayout = (RelativeLayout) findViewById(R.id.topRelativeLayout);
        rotateLoading = (RotateLoading) findViewById(R.id.rotateloading);

        title.setTypeface(App.montRegular);
        signupMessage.setTypeface(App.montLight);

        name.setTypeface(App.montUltraLight);
        username.setTypeface(App.montUltraLight);
        email.setTypeface(App.montUltraLight);
        password.setTypeface(App.montUltraLight);
        signup.setTypeface(App.montUltraLight);
    }

    public void onSignup(View view) {


        if (isEmpty(name)) {
            Toast.makeText(SignupActivity.this, "Enter name", Toast.LENGTH_SHORT).show();

        } else if (isEmpty(username)) {
            Toast.makeText(SignupActivity.this, "Enter Username", Toast.LENGTH_SHORT).show();
        } else if (isEmpty(email)) {
            Toast.makeText(SignupActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
        } else if (isEmpty(password)) {
            Toast.makeText(SignupActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else if (!emailValidator.validate(email.getText().toString().trim())) {
            Toast.makeText(SignupActivity.this, "Email not valid", Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().length() < 8) {
            Toast.makeText(SignupActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
        } else if (username.getText().toString().length() <= 4) {
            Toast.makeText(SignupActivity.this, "Username too short", Toast.LENGTH_SHORT).show();
        } else {
            subParentLayout.setVisibility(View.INVISIBLE);
            rotateLoading.setVisibility(View.VISIBLE);
            rotateLoading.start();

            HashMap<String, Object> profile = new HashMap<String, Object>();
            profile.put("name", name.getText().toString());


            MeteorSingleton.getInstance().registerAndLogin(
                    username.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString(),
                    profile
                    , new ResultListener() {
                        @Override
                        public void onSuccess(String response) {
                            Log.e("Signup", response);


                            MeteorSingleton.getInstance().loginWithEmail(email.getText().toString(),
                                    password.getText().toString(),
                                    new ResultListener() {
                                        @Override
                                        public void onSuccess(String s) {
                                            Log.e("MeteorLogin", s);

                                            Prefs.with(SignupActivity.this).save("loggedin", true);
                                            Prefs.with(SignupActivity.this).save("id", MeteorSingleton.getInstance().getUserId() + "");

                                            if (Prefs.with(SignupActivity.this).getBoolean("loggedin", false))
                                                MeteorSingleton.getInstance().subscribe("message", new Object[]{Prefs.with(SignupActivity.this).getString("id", null)}, new SubscribeListener() {
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

                                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                            finish();

                                        }

                                        @Override
                                        public void onError(String s, String s1, String s2) {
                                            Log.e("MeteorLogin", s);
                                            Log.e("MeteorLogin", s1);
                                        }
                                    });
                            rotateLoading.stop();

                        }

                        @Override
                        public void onError(String s, String s1, String s2) {

                        }
                    }
            );

        }
    }

    private boolean isEmpty(EditText editText) {

        return editText.getText().toString().equals("");
    }
}
