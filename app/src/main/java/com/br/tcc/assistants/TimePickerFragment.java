package com.br.tcc.assistants;


import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TextView;
import android.app.DialogFragment;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.TimePicker;

import com.example.victor.tcc.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    int button,textId;
    Button buttonToActivate;
    private final static int TIME_PICKER_INTERVAL = 30;

    public TimePickerFragment() {

    }

    @SuppressLint("ValidFragment")
    public TimePickerFragment(int buttonId, int textId, Button buttonToActivate) {
        this.button = buttonId;
        this.textId = textId;
        this.buttonToActivate = buttonToActivate;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new CustomTimePickerDialog(getActivity(),this, hour, minute,true);
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        //Do something with the user chosen time
        //Get reference of host activity (XML Layout File) TextView widget
        TextView tv = (TextView) getActivity().findViewById(button);
        //Set a message for user
        TextView text = (TextView) getActivity().findViewById(textId);
        buttonToActivate.setEnabled(true);
        //Display the user changed time on TextView
        text.setText(String.valueOf(hourOfDay)+ ":" + String.valueOf(minute));
    }


}