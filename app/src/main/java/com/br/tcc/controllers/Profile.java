package com.br.tcc.controllers;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.br.tcc.assistants.DateDialogFragment;
import com.br.tcc.database.local.DataDAO;
import com.br.tcc.database.local.TimeBlockDAO;
import com.example.victor.tcc.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "TESTE" ;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimeBlockDAO timeBlockDAO = new TimeBlockDAO(this);
        NavigationView navigationView = findViewById(R.id.navMenuHome);
        setContentView(R.layout.activity_profile);
        DataDAO dataDAO = new DataDAO(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.requestLayout();
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mDrawerLayout.bringToFront();
        setNavigationViewListener();
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Button buttonAddTime = (Button) findViewById(R.id.buttonAddTime);

        buttonAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Time.class);
                Profile.this.startActivity(intent);

            }
        });
        final CompactCalendarView compactCalendarView = (CompactCalendarView) findViewById(R.id.CalendarView);
        Cursor data = dataDAO.getData();
        while(data.moveToNext()){
            DateDialogFragment dialogFragment = new DateDialogFragment ();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = null;
            try {
                d = sdf.parse(data.getString(2));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal.setTime(d);

            Cursor dataEach = timeBlockDAO.getDataId(data.getString(0));
            dialogFragment.addListItems(dataEach);
            Event ev1 = new Event(Color.GREEN, cal.getTimeInMillis(), dialogFragment);
            compactCalendarView.addEvent(ev1);

        }

       // Calendar c = Calendar.getInstance();
      //  c.setTime(Calendar.getInstance().getTime());
        //c.add(Calendar.DATE, 1);  // number of days to add

        //Event ev1 = new Event(Color.GREEN, c.getTimeInMillis(), "Some extra data that I want to store.");
        //compactCalendarView.addEvent(ev1);

       // List<Event> events = compactCalendarView.getEvents(Calendar.getInstance().getTimeInMillis());
        //System.out.println(events.size());

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                if(events.size()>0){
                FragmentManager fm = getFragmentManager();
                DateDialogFragment dialogFragment = (DateDialogFragment)events.get(0).getData();
                dialogFragment.show(fm, "Sample Fragment");
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });



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
                intent = new Intent(Profile.this, Profile.class);
                Profile.this.startActivity(intent);
                break;
            case R.id.buttonHome:
                intent = new Intent(Profile.this, HomePage.class);
                Profile.this.startActivity(intent);
                break;

            case R.id.buttonLogout:
                AlertDialog.Builder builder=new AlertDialog.Builder(Profile.this);
                builder.setMessage("Você quer mesmo sair?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        finish();
                        Intent i=new Intent();
                        i = new Intent(Profile.this, ActivityMain.class);
                        Profile.this.startActivity(i);

                        finish();

                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert=builder.create();
                alert.show();


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
}
