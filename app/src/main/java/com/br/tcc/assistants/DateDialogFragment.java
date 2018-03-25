package com.br.tcc.assistants;

import android.app.DialogFragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.victor.tcc.R;

import java.util.ArrayList;

/**
 * Created by Victor on 3/25/2018.
 */

public class DateDialogFragment extends DialogFragment {
    Cursor dataEach;
    Cursor dataEach2;
    ArrayList listItems=new ArrayList();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    int clickCounter=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        while(dataEach.moveToNext()){
            listItems.add(dataEach.getString(2)+"-"+dataEach.getString(3));
        }


        View rootView = inflater.inflate(R.layout.activity_date, container, false);
        getDialog().setTitle("Simple Dialog");
        Button dismiss = (Button) rootView.findViewById(R.id.dismiss);
        ListView list = (ListView) rootView.findViewById(R.id.listDates);
        adapter = new ArrayAdapter(getActivity(), R.layout.listdate, R.id.textview, listItems);
        list.setAdapter(adapter);
        int contador=0;
        while(dataEach2.moveToNext()){
            System.out.println("TESTE1: "+dataEach2.getString(5));
            if(dataEach2.getString(5).equals("0")){

                list.getChildAt( contador );
                View item_4 = list.getChildAt(contador);
                item_4.setBackgroundColor(Color.RED);
            }
            contador++;
        }

        dismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }
    public void addListItems(Cursor item) {
        dataEach = item;
        dataEach2 = dataEach;
    }
}
