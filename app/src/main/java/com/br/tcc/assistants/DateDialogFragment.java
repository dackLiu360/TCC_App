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
    ArrayList <Integer>positions =new ArrayList();


    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    int clickCounter=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int cotador=0;
        while(dataEach.moveToNext()){
            listItems.add(dataEach.getString(2)+"-"+dataEach.getString(3));
            if(dataEach.getString(5).equals("0")){
                positions.add(cotador);
            }
            cotador++;

        }


        View rootView = inflater.inflate(R.layout.activity_date, container, false);
        getDialog().setTitle("Simple Dialog");
        Button dismiss = (Button) rootView.findViewById(R.id.dismiss);
        ListView list = (ListView) rootView.findViewById(R.id.listDates);
        System.out.println("TAMANHO: "+positions.size());
        adapter = new ArrayAdapter(getActivity(), R.layout.listdate, R.id.textview, listItems){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                for (int i = 0; i < positions.size(); i++) {
                    if(positions.get(i)==(position)){
                        view.setBackgroundColor(Color.parseColor("#ff0000"));
                    }

                }


                return view;
            }
        };
        list.setAdapter(adapter);

                //View item_4 = list.getChildAt(1);
                //item_4.setBackgroundColor(Color.RED);

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
