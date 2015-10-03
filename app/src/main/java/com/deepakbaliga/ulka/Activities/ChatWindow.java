package com.deepakbaliga.ulka.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.deepakbaliga.ulka.Adapters.ChatAdapter;
import com.deepakbaliga.ulka.App;
import com.deepakbaliga.ulka.MeteorSingleton;
import com.deepakbaliga.ulka.Models.ChatModel;
import com.deepakbaliga.ulka.Models.Message;
import com.deepakbaliga.ulka.R;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import java.util.LinkedList;

import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.SubscribeListener;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import me.alexrs.prefs.lib.Prefs;

public class ChatWindow extends AppCompatActivity  {

    private String _id;
    private String _name;
    private String _username;

    private EditText compose;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    private Realm realm;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    private String myID;
    private String myName;
    private String myUsername;

    private LinkedList<ChatModel> chatModels = new LinkedList<>();
    private ChatAdapter chatAdapter;
    private RecyclerView chatRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);


        Log.e("isConnected", MeteorSingleton.getInstance().isConnected() + "");

         intentFilter = new IntentFilter(
                "android.intent.action.MAIN");



        _id = getIntent().getExtras().getString("id");
        _name = getIntent().getExtras().getString("name");
        _username = getIntent().getExtras().getString("username");

        myID = Prefs.with(getApplicationContext()).getString("id", null);
        myUsername = Prefs.with(getApplicationContext()).getString("myusername", null);
        myName = Prefs.with(getApplicationContext()).getString("myname", null);



        initViews();

    }

    private void initViews() {
        compose = (EditText) findViewById(R.id.compose);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        compose.setTypeface(App.montLight);
        toolbarTitle.setTypeface(App.montLight);
        toolbarTitle.setText(_name);

        chatRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        chatAdapter =  new ChatAdapter(chatModels);
        chatRecyclerView.setAdapter(chatAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();


        //Logging list of messages
        getMessages();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getMessages();
            }
        };

        this.registerReceiver(broadcastReceiver,intentFilter);


    }

    public void send(View view){
        App.vibe.vibrate(30);

        if(!isEmpty(compose)){
            MeteorSingleton.getInstance().call("sendmessage", new Object[]{
                    Prefs.with(getApplicationContext()).getString("id", null),
                    _id,
                    compose.getText().toString(),
                    myUsername,
                    myName
            }, new ResultListener() {
                @Override
                public void onSuccess(String s) {
                    Log.e("Response", s + "");
                }

                @Override
                public void onError(String s, String s1, String s2) {
                    Log.e("Error", s + "");
                    Log.e("Error", s2 + "");
                    Toast.makeText(ChatWindow.this, s1, Toast.LENGTH_SHORT).show();
                }
            });


            compose.getText().clear();
        }

    }

    private boolean isEmpty(EditText editText){

        return editText.getText().toString().equals("");
    }


    @Override
    protected void onPause() {
        super.onPause();

        this.unregisterReceiver(broadcastReceiver);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void getMessages(){
        chatModels.clear();

        realm = Realm.getInstance(this);

        RealmQuery<Message> messageRealmQuery = realm.where(Message.class);

        messageRealmQuery.equalTo("from", _id).or().equalTo("from",myID).equalTo("to",_id);

        RealmResults<Message> messages = messageRealmQuery.findAll();


        for(Message message : messages){

            ChatModel model = new ChatModel();
            model.setMessage(message.getMessage());
            model.setMessageID(message.getMessageID());
            if(myID.equals(message.getFrom())){
                model.setMe(true);
            }else{
                model.setMe(false);
            }

            chatModels.add(model);
        }

        for(int i=0;i<chatModels.size();i++){
            Log.e("CHAT", chatModels.get(i).getMessage()+" from "+chatModels.get(i).isMe());
        }

        chatAdapter.notifyDataSetChanged();
        chatRecyclerView.scrollToPosition(chatModels.size()-1);

    }
}
