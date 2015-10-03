package com.deepakbaliga.ulka;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.deepakbaliga.ulka.Models.Message;
import com.deepakbaliga.ulka.Utils.MeteorUtils;

import org.json.JSONException;
import org.json.JSONObject;

import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.SubscribeListener;
import io.realm.Realm;
import me.alexrs.prefs.lib.Prefs;

/**
 * Created by deezdroid on 28/09/15.
 */
public class MeteorService extends Service implements MeteorCallback{


    private Realm realm;
    private static String messageString;

    private Intent intent;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        realm = Realm.getInstance(this);

        MeteorSingleton.createInstance(this, MeteorUtils.PATH);
        Log.e("Meteor", "Meteor Instance Created");
        intent = new Intent("android.intent.action.MAIN");

        //TODO Handle this thing before login
        if(Prefs.with(this).getBoolean("loggedin",false))
        MeteorSingleton.getInstance().subscribe("message", new Object[]{Prefs.with(this).getString("id", null)}, new SubscribeListener() {
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

        MeteorSingleton.getInstance().setCallback(this);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnect(boolean b) {
        Log.e("MeteorService", "Connected "+b);


    }

    @Override
    public void onDisconnect(int i, String s) {

    }

    @Override
    public void onDataAdded(String s, String s1, String s2) {

        messageString = s2;

        if(s.equals("users")){
            try {
                Log.e("Meteor Service","Welcome "+new JSONObject(s2).getJSONObject("profile").getString("name"));

                Prefs.with(this).save("myname",new JSONObject(s2).getJSONObject("profile").getString("name"));
                Prefs.with(this).save("myusername",new JSONObject(s2).getString("username"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(s.equals("messages")){
            try {
                Log.e("Meteor Service",""+new JSONObject(s2).getString("message"));
                //Log.e("Meteor Service",""+s2);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Message message = new Message();

                        try {
                            JSONObject messageObject = new JSONObject(messageString);
                            message.setMessage(messageObject.getString("message"));
                            message.setFrom(messageObject.getString("from"));
                            message.setFromUsername(messageObject.getString("username"));
                            message.setFromName(messageObject.getString("name"));
                            message.setTo(messageObject.getString("to"));
                            message.setMessageID(messageObject.getString("messageID"));
                            realm.copyToRealmOrUpdate(message);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        this.sendBroadcast(intent);
    }

    @Override
    public void onDataChanged(String s, String s1, String s2, String s3) {

    }

    @Override
    public void onDataRemoved(String s, String s1) {

    }

    @Override
    public void onException(Exception e) {
            e.printStackTrace();
    }
}
