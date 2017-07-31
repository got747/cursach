package com.example.goga747.budila.ActivityPlay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.goga747.budila.MainActivity;
import com.example.goga747.budila.R;

public class PlayMusActivity extends AppCompatActivity {

    private static final String LOG_D = "my";
    int click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_mus);

        String ringtone = getIntent().getStringExtra("Ringtone");
        deletedSelectedAlarm(getIntent().getIntExtra("id",0));
        Uri uri=Uri.parse(getString(R.string.musUri)+ringtone);

        LinearLayout llClick = (LinearLayout) findViewById(R.id.llClick);

        int wrapComtemt = LinearLayout.LayoutParams.WRAP_CONTENT;

        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(wrapComtemt,wrapComtemt);

        lParams.gravity= Gravity.CENTER;

        final MediaPlayer ring = MediaPlayer.create(this, uri);
        ring.start();

        final Button bntnew = new Button(this);
        bntnew.setText("Dismiss");
        bntnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click++;
                if(click>3){
                    ring.stop();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        llClick.addView(bntnew,lParams);

    }
    @NonNull
    private String[] getSplit(String date_record) {
        return date_record.split(":");
    }

    private void deletedSelectedAlarm(int id) {

        SharedPreferences selectedAlarms = getSharedPreferences("SelectedAlarmsAND_rewrite", MODE_PRIVATE);

        String[] date_selected=getSplit(selectedAlarms.getString("selected",""));

        String date_answer= "", s_id=String.valueOf(id);

        Log.d(LOG_D,"удаляем из строки "+date_selected+" такой id "+String.valueOf(id));
        Log.d(LOG_D,"length "+String.valueOf(date_selected.length));

        for (String s :date_selected){
           if(! s.equals(s_id)) date_answer+=s+":";
        }

        SharedPreferences.Editor editor = selectedAlarms.edit();
        editor.putString("selected", date_answer);
        editor.apply();

        Log.d(LOG_D,"save string "+date_answer);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
