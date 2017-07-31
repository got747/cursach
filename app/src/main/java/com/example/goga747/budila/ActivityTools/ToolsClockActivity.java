package com.example.goga747.budila.ActivityTools;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.goga747.budila.Fragment.MusicFragment;
import com.example.goga747.budila.Fragment.TimeFragment;
import com.example.goga747.budila.R;

import java.io.Serializable;
import java.util.Calendar;

public class ToolsClockActivity extends AppCompatActivity implements View.OnClickListener
        , TimeFragment.TimeFragmentLister,MusicFragment.MusicInterfaceLister
{
    TextView txtTime;
    TextView txtRington;
    TextView txtLabel;

    Button okCreat;

    DialogFragment time;
    DialogFragment mus;

    String S_TIME;
    String S_NAME_RINGTONE;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_clock);


        txtTime = (TextView) findViewById(R.id.prewTime);
        Calendar defaultedTime = Calendar.getInstance();

        Serializable s_hour = defaultedTime.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + defaultedTime.get(Calendar.HOUR_OF_DAY) : defaultedTime.get(Calendar.HOUR_OF_DAY);
        Serializable minute = defaultedTime.get(Calendar.MINUTE) < 10 ? "0" + defaultedTime.get(Calendar.MINUTE) : defaultedTime.get(Calendar.MINUTE);
        txtTime.setText(s_hour+" : "+minute);
        S_TIME = s_hour+":"+minute;

        txtRington = (TextView) findViewById(R.id.txtNameMusic);
        txtRington.setText("scandium");
        S_NAME_RINGTONE="scandium";

        txtLabel = (TextView) findViewById(R.id.textName);

        okCreat = (Button) findViewById(R.id.OkCreat);
        okCreat.setOnClickListener(this);



        LinearLayout data = (LinearLayout) findViewById(R.id.layoutTime);
        data.setOnClickListener(this);

        LinearLayout ringtone = (LinearLayout) findViewById(R.id.layoutRingtone);
        ringtone.setOnClickListener(this);

        time = new TimeFragment();
        mus = new MusicFragment();

        //Log.d("myLogs","перешли");
    }

    @Override
    public void onClick(View view) {

          intent = new Intent();

        switch (view.getId()){
            case R.id.layoutTime:
                time.setTargetFragment(time,RESULT_OK);
                time.show(getFragmentManager(),"Time");
                break;
            case R.id.layoutRingtone:
                mus.setTargetFragment(mus,RESULT_OK);
                mus.show(getFragmentManager(),"Ringtone");
                break;
            case R.id.OkCreat:


                intent.putExtra("Date_Record",S_TIME+":"+S_NAME_RINGTONE+":"+txtLabel.getText().toString());

                    setResult(RESULT_OK,intent);
                    finish();

                break;
        }
    }
// не работает
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent=new Intent();
        intent.putExtra("Date_Record",S_TIME+":"+S_NAME_RINGTONE+":"+txtLabel.getText().toString());
        Log.d("my",S_TIME+":"+S_NAME_RINGTONE+":"+txtLabel.getText().toString());

        setResult(RESULT_CANCELED,intent);
        finish();

    }

    @Override
    public void onDialogClick(int hour, int minute) {
        String s_hour,s_minute;
        s_hour = hour<10? "0"+String.valueOf(hour): String.valueOf(hour);
        s_minute = minute<10? "0"+String.valueOf(minute): String.valueOf(minute);

        S_TIME =" "+s_hour+" : "+s_minute;
        txtTime.setText(S_TIME);
        S_TIME =s_hour+":"+s_minute;

    }

    @Override
    public void getNameMus(String name) {
        S_NAME_RINGTONE=name;
        txtRington.setText(name);
    }
}
