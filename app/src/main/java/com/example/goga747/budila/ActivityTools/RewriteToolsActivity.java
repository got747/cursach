package com.example.goga747.budila.ActivityTools;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.goga747.budila.Fragment.MusicFragment;
import com.example.goga747.budila.Fragment.TimeFragment;
import com.example.goga747.budila.R;
//ПРИКРУТИ ИНТЕРФЕЙС ЧТОБЫ БЫЛО ПО ЛЮДСКИ
/**
 * Created by goga747 on 13.05.2017.
 */

public class RewriteToolsActivity extends AppCompatActivity implements View.OnClickListener ,
        MusicFragment.MusicInterfaceLister , TimeFragment.TimeFragmentLister
{
    TextView txtTime;
    TextView txtRington;
    TextView txtLabel;

    Button okCreat;

    DialogFragment time;
    DialogFragment mus;

    String[] record;
    int id;

    String S_TIME;
    String S_NAME_RINGTONE;

    SharedPreferences toFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_clock);

        //Log.d("my","сработало");

        SharedPreferences data = getSharedPreferences("Tools_Settings",MODE_PRIVATE);

        id = getIntent().getIntExtra("id",0);        
      
        String getRecord = data.getString(String.valueOf(id),"00:00:scandium");
        record = getSplit(getRecord);
        
        toFragment = getSharedPreferences("SelectedAlarmsAND_rewrite",MODE_PRIVATE);
        Editor editor = toFragment.edit();
        editor.putString("rewrite",getRecord);
        editor.apply();
        Log.d("my","фрагмет "+toFragment.getAll().toString());
        Log.d("my", toFragment.toString());
        
        txtTime = (TextView) findViewById(R.id.prewTime);
        txtTime.setText(" "+ record[0]+" : "+ record[1]);
        S_TIME=record[0]+":"+record[1];

        txtRington = (TextView) findViewById(R.id.txtNameMusic);
        txtRington.setText(record[2]);
        S_NAME_RINGTONE=record[2];

        txtLabel = (TextView) findViewById(R.id.textName);
        String label= record.length>3 ? record[3] : "";
        txtLabel.setText(label);

        okCreat = (Button) findViewById(R.id.OkCreat);
        okCreat.setOnClickListener(this);

        LinearLayout timeUser = (LinearLayout) findViewById(R.id.layoutTime);
        timeUser.setOnClickListener(this);

        LinearLayout ringtone = (LinearLayout) findViewById(R.id.layoutRingtone);
        ringtone.setOnClickListener(this);

        time = new TimeFragment();
        mus = new MusicFragment();

        //Log.d("myLogs","перешли");
    }
    @NonNull
    private String[] getSplit(String date_record) {
        return date_record.split(":");
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.layoutTime:
                time.setTargetFragment(time,RESULT_OK);
                //send.setTime(1);
                //send.setTime(Integer.valueOf(record[0]),Integer.valueOf(record[1]));
                time.show(getFragmentManager(),"Time");
                break;
            case R.id.layoutRingtone:
                mus.setTargetFragment(mus,RESULT_OK);
                mus.show(getFragmentManager(),"Ringtone");
                break;
            case R.id.OkCreat:
                Editor editor = toFragment.edit();
                editor.remove("rewrite");
                editor.apply();
                //Log.d("my",txtLabel.getText().toString());
                intent.putExtra("Rewrite_Date_Record",String.valueOf(id)+":"+S_TIME+":"+S_NAME_RINGTONE+":"+txtLabel.getText().toString());
                //Log.d("my",S_TIME+":"+S_NAME_RINGTONE+":"+txtLabel.getText().toString());
                setResult(RESULT_OK,intent);
                finish();

                break;
        }

    }
//не работает
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Editor editor = toFragment.edit();
        editor.remove("rewrite");
        editor.apply();
        //ниже не особо нужный код
        Intent intent=new Intent();
        intent.putExtra("Rewrite_Date_Record",String.valueOf(id)+":"+S_TIME+":"+S_NAME_RINGTONE+":"+txtLabel.getText().toString());
        //Log.d("my",S_TIME+":"+S_NAME_RINGTONE+":"+txtLabel.getText().toString());
        setResult(RESULT_CANCELED,intent);
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Editor editor = toFragment.edit();
        editor.remove("rewrite");
        editor.apply();
        //ниже не особо нужный код
        Intent intent=new Intent();
        intent.putExtra("Rewrite_Date_Record",String.valueOf(id)+":"+S_TIME+":"+S_NAME_RINGTONE+":"+txtLabel.getText().toString());
        //Log.d("my",S_TIME+":"+S_NAME_RINGTONE+":"+txtLabel.getText().toString());
        setResult(RESULT_CANCELED,intent);
        finish();
        // Log.d("my","dsf");
    }

    @Override
    public void getNameMus(String name) {
        S_NAME_RINGTONE=name;
        txtRington.setText(name);
    }

    @Override
    public void onDialogClick(int hour, int minute) {
        S_TIME =" "+String.valueOf(hour)+" : "+String.valueOf(minute);
        txtTime.setText(S_TIME);
        S_TIME =String.valueOf(hour)+":"+String.valueOf(minute);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Editor editor = toFragment.edit();
        editor.remove("rewrite");
        editor.apply();
    }
}
