package com.example.goga747.budila;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.goga747.budila.ActivityTools.RewriteToolsActivity;
import com.example.goga747.budila.ActivityTools.ToolsClockActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
/*
После перезаписи будильник переустанавливается, надпись на вьюхах обновляются, записи тоже обновляются
Не проверено схема выхововметодов в onActivityResult ВООООБЩЕ!!!!!!

for OR foreach ?

определись с id ? String : int ;

обробатывать всякие появления и пропажу активити

найти способ запретить ходить по активити

Для обновления инфы на кнопках после перезаписи, и установки галок на зупущеных будильниках после возращения с проигрывающих экранов
как вариант удалять view и пересоздовать их после выполнения вышепречисленных действий

сделай то что написанно с низу

НЕГДЕ НЕ ОБРАБАТЫВАЕСТЯ onRestart onResume onPause !!!
*/

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnCreat;
    LinearLayout llMain;
    SharedPreferences saveTools;
    SharedPreferences selectedAlarms;
    AlarmManager al;
    int maxKeyMap; // присваивается значение scanMapToCreatView или в onCreate ,обновляется в saveTools, можем потерять значение при onRestart onResume onPause ?!

    final int REQUST_CODE_TOOLS=1;
    final int REQUST_CODE_Rewrite=2;
    final String LOG_D = "my";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreat = (Button) findViewById(R.id.btnCreat);
        btnCreat.setOnClickListener(this);

        llMain = (LinearLayout)findViewById(R.id.llMain);

        saveTools = getSharedPreferences("Tools_Settings",MODE_PRIVATE);
        selectedAlarms = getSharedPreferences("SelectedAlarmsAND_rewrite",MODE_PRIVATE);
        //Toast.makeText(this, String.valueOf(t.getTime().getMinutes()), Toast.LENGTH_LONG).show();

        List<String> listKey= new ArrayList(saveTools.getAll().keySet());
        Log.d(LOG_D,"saveTools "+saveTools.getAll().toString());
        Log.d(LOG_D,listKey.toString());

        Editor editorSave=saveTools.edit(); // для отладки
        //editorSave.clear();
        editorSave.apply();

        Log.d(LOG_D,"selectedAlarms "+selectedAlarms.getAll().toString());
        Editor editorSelected = selectedAlarms.edit();   //
        editorSelected.remove("rewrite");                // как то не правельно ?
        //editorSelected.remove("selected");
        editorSelected.apply();                          //

        if (!listKey.isEmpty()){
            Log.d(LOG_D,"еще живой");
            findMaxKey(listKey);
            //btnCreat.setEnabled(false);
            scanMapToCreatView(listKey);
        }else {
            maxKeyMap = 0;
            Log.d(LOG_D,String.valueOf(maxKeyMap)+"длина обнулилась");
        }
    }

    private void findMaxKey( List<String> listKey) {
        maxKeyMap= Integer.valueOf(Collections.max(listKey));
        Log.d(LOG_D,"maxKeyMap" + String.valueOf(maxKeyMap));
        /*for (Map.Entry<String, ?> entry:map.()) {
            maxKeyMap= maxKeyMap > Integer.valueOf(entry.getKey()) ? maxKeyMap : Integer.valueOf(entry.getKey());
            //Log.d(LOG_D,"maxKeyMap" + String.valueOf(maxKeyMap));
            //Log.d(LOG_D,entry.getKey());
        }*/
    }

    @Override // для меню удаления , передает в onContextItemSelected
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,v.getId(),1,"Deleted");
        //Log.d("my",String.valueOf(v.getId()));
    }

    @Override // Добавить удаление из запланированных действий Сигнализаций
    public boolean onContextItemSelected(MenuItem item) {

        Editor editor=saveTools.edit();
        editor.remove(String.valueOf(item.getItemId()));
        editor.apply();

        Log.d(LOG_D," файл до удаления  "+saveTools.getAll().toString());

        deletedSelectedAlarm(item.getItemId());
        disabledAlarm(item.getItemId());

        llMain.removeView(findViewById(item.getItemId()));

        Log.d(LOG_D,"длина после удаления"+String.valueOf(maxKeyMap));
        Log.d(LOG_D," файл после удаления  "+String.valueOf(saveTools.getAll()));
        return super.onContextItemSelected(item);
    }

    private void scanMapToCreatView(List<String> listKey) {

        for (String id:listKey) {
            creatViewAlarm(id);

        }
        /*for (Map.Entry<String, ?> entry : map.entrySet()) {
            //Log.d(LOG_D,entry.getKey()+" "+entry.getValue());
            //String date_record=entry.getKey()+":"+entry.getValue();
            creatViewAlarm(entry.getKey());
        }*/
        // Log.d(LOG_D,"отработало чтение");
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnCreat:
                intent = new Intent(this,ToolsClockActivity.class);
                startActivityForResult(intent,REQUST_CODE_TOOLS);
                // Log.d(LOG_D, "отправили");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case REQUST_CODE_TOOLS:
                     //Log.d(LOG_D, data.getStringExtra("Date_Record") + "пришло с формы");
                    try {
                        creatViewAlarm(saveTools(data.getStringExtra("Date_Record")));
                    } catch (Exception e) {
                    }
                    break;
                case REQUST_CODE_Rewrite:
                    try {
                        String s_id = rewriteTools(data.getStringExtra("Rewrite_Date_Record")) ; //быдло
                        if (checkInSelected(Integer.valueOf(s_id))){
                            setAlarm(Integer.valueOf(s_id));
                            creatViewAlarm(s_id);
                        } else creatViewAlarm(s_id);
                    }  catch (Exception e) {

                    }
                    break;
            }
        } else if (resultCode==RESULT_CANCELED){
            switch (requestCode) {
                case REQUST_CODE_TOOLS:
                    Log.d(LOG_D, "пришло с формы");
                         //Log.d(LOG_D, data.getStringExtra("Date_Record") + "пришло с формы");
                    try {
                        creatViewAlarm(saveTools(data.getStringExtra("Date_Record")));
                    } catch (Exception e) {
                    }
                    break;
                case REQUST_CODE_Rewrite:
                        // Log.d(LOG_D, data.getStringExtra("Rewrite_Date_Record") + "пришло с формы");
                    try {
                        String s_id = rewriteTools(data.getStringExtra("Rewrite_Date_Record")) ; //быдло
                        if (checkInSelected(Integer.valueOf(s_id))){
                            setAlarm(Integer.valueOf(s_id));
                            creatViewAlarm(s_id);
                        } else creatViewAlarm(s_id);
                    }  catch (Exception e) {

                    }

                    break;
            }
        }

    }

    private String rewriteTools(String rewrite_date_record) {
        Log.d(LOG_D,saveTools.getAll().toString());
        Editor editor = saveTools.edit();

        String id = getSplit(rewrite_date_record)[0];

        StringBuilder sb = new StringBuilder(rewrite_date_record);

        rewrite_date_record=sb.delete(0,rewrite_date_record.indexOf(":")+1).toString();

        editor.putString(id,rewrite_date_record);

        editor.apply();

        return (id);
    }
/////////////////тут изменить
    @NonNull
    private String saveTools(String date_record) {
        Editor editor = saveTools.edit();
        int id = ++maxKeyMap;//из-за это id=0 не будет
        Log.d(LOG_D,"длина после save "+String.valueOf(id)+" "+String.valueOf(maxKeyMap));
        editor.putString(String.valueOf(id),date_record);
        editor.apply();

        Log.d(LOG_D,"то что сохранили и его id "+String.valueOf(id)+":"+date_record);
        return String.valueOf(id);

    }

    public void creatViewAlarm(String s_id) {
  //лень переписывать ,давай клеять

        String date_record=s_id+":"+saveTools.getString(s_id,""); // пустая строка не в тему

        String[] pars = getSplit(date_record);

        /*Log.d(LOG_D,date_record);
        Log.d(LOG_D,pars[0]);
        Log.d(LOG_D,pars[1]);
        Log.d(LOG_D,pars[2]);
        Log.d(LOG_D,pars[3]);*/

        int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;

        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                wrapContent, wrapContent);

        lParams.gravity = Gravity.CENTER;

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setId(Integer.valueOf(pars[0]));

        final Button btnNew = new Button(this);
        btnNew.setText(pars[1]+" : "+ pars[2]);
        btnNew.setId(Integer.valueOf(pars[0]));
        btnNew.setTag(Integer.valueOf(pars[0])); // не нужно есть id

        btnNew.setOnCreateContextMenuListener(this);

        /* Log.d(LOG_D,pars[0]+" ID");
        Log.d(LOG_D,String.valueOf(btnNew.getId()));*/
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RewriteToolsActivity.class);
                intent.putExtra("id",btnNew.getId());
                llMain.removeView(findViewById(btnNew.getId()));

                if (checkInSelected(btnNew.getId())){
                    deletedSelectedAlarm(btnNew.getId());
                    disabledAlarm(btnNew.getId());
                }

                startActivityForResult(intent,REQUST_CODE_Rewrite);

                //Log.d(LOG_D,"кнопка нажата"+String.valueOf(btnNew.getId()));

            }
        });

        layout.addView(btnNew);

        final CheckBox checkid= new CheckBox(this);
        //ставит галку если будильни был установлен
        if (checkInSelected(Integer.valueOf(pars[0]))) checkid.setChecked(true);

        checkid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){

                    int id = btnNew.getId();
                    saveSelectedAlarm(id);
                    setAlarm(id);
                }else{

                    int id = btnNew.getId();
                    deletedSelectedAlarm(id);
                    disabledAlarm(id);
                    //Log.d(LOG_D,"отжат");
                }
            }
        });

        layout.addView(checkid);

        llMain.addView(layout, lParams);
    }

    private boolean checkInSelected(int id) {

        String[] date_selected=getSplit(selectedAlarms.getString("selected",""));

        //Log.d(LOG_D,"проверяем в такой строке "+date_selected.toString()+" такой id "+String.valueOf(id));

        String s_id = String.valueOf(id);
        Boolean flag=false;
        for (String s : date_selected) {
            //Log.d(LOG_D,"a_id "+date_selected[i]+" s_id "+s_id);
            if (s.equals(s_id)) {
                flag = true;
                break;
            }
        }
        //Log.d(LOG_D,"flag "+flag.toString());
        return flag;
/*
        if (date_selected.contentEquals(s)){
            Log.d(LOG_D," вернули true" );
            return true;
        }else {
            Log.d(LOG_D," вернули false" );
            return false;
        }
*/
    }

    // удаляет id нажатого будильника // НЕ ПРОВЕРЕНО ЗДЕСЬ
    private void deletedSelectedAlarm(int id) {
        String[] date_selected=getSplit(selectedAlarms.getString("selected",""));

        String date_answer= "", s_id=String.valueOf(id);

        Log.d(LOG_D,"удаляем из строки "+date_selected+" такой id "+String.valueOf(id));
        Log.d(LOG_D,"length "+String.valueOf(date_selected.length));

        for (String s : date_selected) {
            if (!s.equals(s_id)) date_answer += s + ":";
        }

        Editor editor = selectedAlarms.edit();
        editor.putString("selected", date_answer);
        editor.apply();

        Log.d(LOG_D,"save string "+date_answer);
        /*String s_id=String.valueOf(id);

        date_selected="";

        Log.d("my","do "+s.toString());
        int my =s.length;
        Log.d("my", "do " + String.valueOf(my));

        for (int i=0; i < s.length;i++) {
            Log.d("my",String.valueOf(i));
            Log.d("my","do "+date_selected+" :s[i]: "+s[i]+" :i=: "+String.valueOf(i));
            //s[i].equals(s_id)?continue : date_selected+=s[i]+":" ;
            if (!s[i].equals(s_id)){
                date_selected+=s[i]+":";
                Log.d("my","do "+date_selected+" :s[i]: "+s[i]+" :i=: "+String.valueOf(i));
            }
            Log.d("my","ssss "+date_selected);
        }
*/


    }

    //сохраняет id нажатого будильника
    private void saveSelectedAlarm(int id) {
        String date_selected=selectedAlarms.getString("selected","");

        Log.d(LOG_D,"вставляем id "+String.valueOf(id)+" в строку "+date_selected);

        //if (!date_selected.contains(String.valueOf(id))){
            date_selected+=String.valueOf(id)+":";

            Editor editorSelected = selectedAlarms.edit();
            editorSelected.putString("selected",date_selected);
            editorSelected.apply();
            Log.d(LOG_D,"после вставки "+date_selected);
        //}



    }

// удаляет сигнализацию
    private void disabledAlarm(int id) {

        Intent intent = new Intent(this,MyItentions.class);
        //intent.putExtra("id",id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),id,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        al= (AlarmManager) getSystemService(ALARM_SERVICE);
        al.cancel(pendingIntent);
        Log.d("my","отменили");


    }

    @NonNull
    private String[] getSplit(String date_record) {
        return date_record.split(":");
    }

    private void setAlarm(int id) {

        String data = saveTools.getString(String.valueOf(id),"");
       //Log.d(LOG_D,data);
        String[] parsData = getSplit(data);
        /*Log.d(LOG_D,saveTools.getString(String.valueOf(id),""));
        Log.d(LOG_D,parsData[0]);
        Log.d(LOG_D,parsData[1]);
        Log.d(LOG_D,parsData[2]);*/

        Calendar currentTime = Calendar.getInstance();
        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY,Integer.valueOf(parsData[0]));
        time.set(Calendar.MINUTE,Integer.valueOf(parsData[1]));
        time.set(Calendar.SECOND,0);

        Intent intent = new Intent(this,MyItentions.class);
        intent.putExtra("id",id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        
        al= (AlarmManager) getSystemService(ALARM_SERVICE);

        if (time.after(currentTime)) {
            al.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
        } else {
            al.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis()+86400000, pendingIntent);
        }


        Log.d("my","установили");
    }
}

