package com.example.goga747.budila.Fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.goga747.budila.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MusicFragment extends DialogFragment implements View.OnClickListener  {

    ListView listM;
    ArrayList<String>  list;
    MediaPlayer ring;
    MusicInterfaceLister lister;

    public interface MusicInterfaceLister{
        void getNameMus(String name);
    }

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof MusicInterfaceLister) {
            lister = (MusicInterfaceLister) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MusicInterfaceLister");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    public void onViewCreated(View view, Bundle state)
    {
        super.onViewCreated(view, state);
        getDialog().setTitle(R.string.ringtone);
        listM= (ListView)view.findViewById(R.id.listView);
        listM.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ring!=null){
                    ring.stop();
                    ring.reset();
                }
                String name=list.get(listM.getCheckedItemPosition());
                Uri uri=Uri.parse(getString(R.string.musUri)+name);
                ring= MediaPlayer.create(getActivity(),uri);

                ring.start();
            }
        });
        view.findViewById(R.id.btnOk).setOnClickListener(this);
        showList();
    }

    @Override
    public void onClick(View view) {
        if (ring!=null){
            ring.stop();
            ring.reset();
            setNameMus();
        }

        dismiss();
    }

    private void setNameMus() {
        String name = list.get(listM.getCheckedItemPosition());
        //Log.d("my",name);
        lister.getNameMus(name);
    }

    //убрал String из <>
    private void showList() {
        ArrayAdapter<String> adapter;
        adapter= new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_single_choice,
                listFileMus());

        listM.setAdapter(adapter);

    }

    public ArrayList<String> listFileMus() {

        Field[] fields = R.raw.class.getFields();
        list = new ArrayList<>();
        for (Field field : fields) {
            String name = field.getName();
            list.add(name);
        }
        return list;
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (ring!=null){
            ring.stop();
            ring.reset();
            setNameMus();
        }

    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (ring!=null){
            ring.stop();
            ring.reset();
            setNameMus();
        }

    }
}
