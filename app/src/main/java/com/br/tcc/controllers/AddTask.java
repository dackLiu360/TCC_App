package com.br.tcc.controllers;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;

import com.br.tcc.assistants.TimePickerFragment;
import com.br.tcc.database.local.UserDAO;
import com.example.victor.tcc.R;

import java.util.Calendar;

public class AddTask extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public static TimePicker timePicker;

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timePicker.setCurrentHour(hourOfDay);
            timePicker.setCurrentMinute(minute);
        }
    }
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.requestLayout();
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mDrawerLayout.bringToFront();
        setNavigationViewListener();
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String id = null;
        Intent intent = getIntent();
        final UserDAO udao = new UserDAO(this);
        Cursor data = udao.getData();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item)){


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.buttonProfile:
                intent = new Intent(AddTask.this, Profile.class);
                AddTask.this.startActivity(intent);
                break;
            case R.id.buttonHome:
                intent = new Intent(AddTask.this, HomePage.class);
                AddTask.this.startActivity(intent);
                break;
        }
        return true;
    }
    private void setNavigationViewListener() {
        NavigationView navigationView = findViewById(R.id.navMenuHome);
        System.out.println(navigationView.toString());
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }


    public void onRadioInvidigualClicked(View view) {
    }

    public void onRadioGroupClicked(View view) {
    }
}
