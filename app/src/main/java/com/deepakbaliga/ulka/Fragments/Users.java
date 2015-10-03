package com.deepakbaliga.ulka.Fragments;

/**
 * Created by deezdroid on 27/09/15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.deepakbaliga.ulka.Activities.ChatWindow;
import com.deepakbaliga.ulka.Adapters.UsersAdapter;
import com.deepakbaliga.ulka.Listeners.UsersTouchListener;
import com.deepakbaliga.ulka.MeteorSingleton;
import com.deepakbaliga.ulka.Models.User;
import com.deepakbaliga.ulka.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import me.alexrs.prefs.lib.Prefs;


public class Users extends Fragment {

    private RecyclerView usersRecyclerView;
    private UsersTouchListener usersTouchListener;
    private LinkedList<User> users;
    private UsersAdapter usersAdapter;

    public Users() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        users = new LinkedList<>();


        usersTouchListener = new UsersTouchListener() {
            @Override
            public void touched(int position) {

                startActivity(new Intent(getActivity(), ChatWindow.class).putExtra("id", users.get(position).getId())
                        .putExtra("name", users.get(position).getName())
                        .putExtra("username", users.get(position).getUsername()));

                try {

                    getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } catch (Exception e) {
                    Log.e("Exception",e.getLocalizedMessage()); }
            }
        };

        MeteorSingleton.getInstance().call("allusers", new ResultListener() {
            @Override
            public void onSuccess(String s) {


                users.clear();

                try {
                    JSONArray resultArray = new JSONArray(s);

                    for(int i=0 ; i<resultArray.length(); i++){

                        JSONObject userObject = resultArray.getJSONObject(i);
                        if(!Prefs.with(getActivity().getApplicationContext()).getString("id","").equals(userObject.getString("_id"))) {

                            User user = new User();

                            user.setId(userObject.getString("_id"));
                            user.setUsername(userObject.getString("username"));
                            user.setName(userObject.getJSONObject("profile").getString("name"));
                            user.setEmail(userObject.getJSONArray("emails").getJSONObject(0).getString("address"));

                            users.add(user);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                usersAdapter = new UsersAdapter(users, usersTouchListener);
                usersRecyclerView.setAdapter(usersAdapter);

            }

            @Override
            public void onError(String s, String s1, String s2) {
                Log.e("Error", s);
                Log.e("Error", s1);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_users, container, false);

        usersRecyclerView = (RecyclerView) view.findViewById(R.id.users_recycler_view);

        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        return  view;
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }
}