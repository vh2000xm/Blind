package com.example.innoz.iotapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by Ahn on 2018-02-27.
 */

public class Alram_BroadCast extends BroadcastReceiver {
    String TAG = "ALRAM";

    private String room_name = null;

    @Override
    public void onReceive(Context context, Intent intent) {//알람 시간이 되었을때 onReceive를 호출함
        //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고
        Toast.makeText(context, "알람 움직임 EXTRA : " + intent.getExtras().getString("roomname"), Toast.LENGTH_SHORT).show();
        room_name = intent.getExtras().getString("roomname");
        context.startService(new Intent(context,Alram_Service.class).putExtra("roomname",room_name));
    }


}


