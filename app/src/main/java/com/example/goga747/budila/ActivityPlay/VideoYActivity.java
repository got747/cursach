package com.example.goga747.budila.ActivityPlay;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.goga747.budila.MainActivity;
import com.example.goga747.budila.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoYActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    public static String API_KEY ;
    int click;
    public static final String VIDEO_ID = "S6RWY8OYwbk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        API_KEY = getString(R.string.API_KEY);

        setContentView(R.layout.activity_video_y);

        LinearLayout llClick = (LinearLayout) findViewById(R.id.llVido);

        int wrapComtemt = LinearLayout.LayoutParams.WRAP_CONTENT;

        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(wrapComtemt,wrapComtemt);

        lParams.gravity= Gravity.CENTER;

        final Button bntnew = new Button(this);
        bntnew.setText("Dismiss");
        bntnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click++;
                if(click>3){

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        llClick.addView(bntnew,lParams);

        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubePlayerView.initialize(API_KEY, this);
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Ошибка инициализации!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {

        if (!wasRestored) {
            //Log.d("my","f");
            player.setFullscreen(true);
            player.loadVideo(VIDEO_ID);
        }
    }

}
