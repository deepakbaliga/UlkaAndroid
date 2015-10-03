package com.deepakbaliga.ulka;

/**
 * Created by deezdroid on 28/09/15.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.deepakbaliga.ulka.Utils.NetworkUtil;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);

        Log.e("Network Status",status);

        if(status.equals("Wifi enabled") || status.equals("Mobile data enabled")){
            MeteorSingleton.getInstance().reconnect();
        }

    }
}