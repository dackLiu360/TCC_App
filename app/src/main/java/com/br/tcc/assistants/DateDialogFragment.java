package com.br.tcc.assistants;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.victor.tcc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 3/25/2018.
 */

public class DateDialogFragment extends DialogFragment {
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    int clickCounter=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_date, container, false);
        getDialog().setTitle("Simple Dialog");
        Button dismiss = (Button) rootView.findViewById(R.id.dismiss);
        ListView list = (ListView) rootView.findViewById(R.id.listDates);
        adapter = new ArrayAdapter(getActivity(), R.layout.listdate, R.id.textview, listItems);
        list.setAdapter(adapter);
        dismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }
    public void addListItems(String item) {
        listItems.add(item);
    }
}
