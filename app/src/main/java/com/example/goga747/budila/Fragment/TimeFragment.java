package com.example.goga747.budila.Fragment;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.example.goga747.budila.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeFragment extends DialogFragment implements View.OnClickListener  {

    TimePicker time;

   TimeFragmentLister lister;

    public TimeFragment() {
        // Required empty public constructor
    }

    public interface TimeFragmentLister { void  onDialogClick(int hour, int minute);}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            lister=(TimeFragmentLister) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+"ищи");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        getDialog().setTitle(R.string.time);

        View v = inflater.inflate(R.layout.fragment_time, null);
        time = (TimePicker) v.findViewById(R.id.timePicker);

        v.findViewById(R.id.btnSetTime).setOnClickListener(this);

        SharedPreferences toFragment = this.getActivity().getSharedPreferences("SelectedAlarmsAND_rewrite", Context.MODE_PRIVATE);
        String[] date = toFragment.getString("rewraite","").split(":");
        Log.d("my","фрагмет "+toFragment.getAll().toString());
        if (date[0]!=""){

            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY,Integer.valueOf(date[0]));
            calendar.set(Calendar.MINUTE,Integer.valueOf(date[1]));

            time.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            time.setCurrentMinute(calendar.get(Calendar.MINUTE));
        }

        return v;
    }

    public  void setData(){
        int hour = time.getCurrentHour();
        int minute = time.getCurrentMinute();

        lister.onDialogClick(hour, minute);
    }

    public void onClick(View v) {
        setData();
        dismiss();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //Log.d(LOG_TAG,"Dismiss");
        setData();
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        //Log.d("my","Time onCancel");
        setData();
    }
}
