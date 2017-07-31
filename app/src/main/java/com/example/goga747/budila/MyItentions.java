package com.example.goga747.budila;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.goga747.budila.ActivityPlay.PlayMusActivity;
import com.example.goga747.budila.ActivityPlay.VideoYActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by goga747 on 12.05.2017.
 */

public class MyItentions extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(isConnectedNetwork(context)){
            playVideo(context,intent);
        }else{
            playRingtone(context,intent);
        }
    }

    private void playVideo(Context context, Intent intent) {
        Intent intent1 = new Intent(context.getApplicationContext(), VideoYActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }

    private boolean isConnectedNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(cm.TYPE_WIFI);
        if (info != null && info.isConnected())
        {
            return true;
        }
        info = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (info != null && info.isConnected())
        {
            return true;
        }
        info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected())
        {
            return true;
        }
        return false;
    }

    @NonNull
    private String[] getSplit(String date_record) {
        return date_record.split(":");
    }

    private void playRingtone(Context context, Intent intent) {
        SharedPreferences data = context.getSharedPreferences("Tools_Settings",MODE_PRIVATE);

        int id = intent.getIntExtra("id",1);
        String record = data.getString(String.valueOf(id),"scandium");
        Intent intent1 = new Intent(context.getApplicationContext(), PlayMusActivity.class);
        intent1.putExtra("Ringtone",getSplit(record)[2]);
        intent1.putExtra("id",id);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

        Toast.makeText(context,getSplit(record)[2],Toast.LENGTH_SHORT).show();
        //Log.d("my","playRingtone");

    }
}
