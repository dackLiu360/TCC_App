package com.br.tcc.controllers;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.br.tcc.assistants.DateDialogFragment;
import com.br.tcc.assistants.TimeBlockModel;
import com.br.tcc.assistants.TimeModel;
import com.example.victor.tcc.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "TESTE" ;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ActionBar toolbar;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //NavigationView navigationView = findViewById(R.id.navMenuHome);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navMenuHome);
        View headerView = navigationView.getHeaderView(0);
        TextView emailMenu = (TextView) headerView.findViewById(R.id.emailMenu);
        TextView nameMenu = (TextView) headerView.findViewById(R.id.nameMenu);
        emailMenu.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        nameMenu.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

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




        SharedPreferences sharedPrefs2 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson3 = new Gson();
        String json3 = sharedPrefs2.getString("TimeList", "");
        Type type2 = new TypeToken<ArrayList<TimeModel>>() {}.getType();
        ArrayList<TimeModel> listTmodel = gson3.fromJson(json3, type2);


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("TimeBlockList", "");
        Type type = new TypeToken<ArrayList<TimeBlockModel>>() {}.getType();
        ArrayList<TimeBlockModel> listTbmodel = gson.fromJson(json, type);
        System.out.println("LISTA NO PROFILE "+listTbmodel.size());

        toolbar = getSupportActionBar();
        toolbar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));






      /*  showPreviousMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.showPreviousMonth();
            }
        });

        showNextMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.showNextMonth();
            }
        });*/


        for (TimeModel tm : listTmodel){
            ArrayList<TimeBlockModel> listTbmodelAssistent = new ArrayList<>();
            DateDialogFragment dialogFragment = new DateDialogFragment ();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                cal.setTime(sdf.parse(tm.getDay()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (TimeBlockModel tbmAssistent : listTbmodel){
                if(tbmAssistent.getId_time().equals(tm.getId_time())){
                    listTbmodelAssistent.add(tbmAssistent);

                }
            }

            dialogFragment.addListItems(listTbmodelAssistent);
            Event ev1 = new Event(Color.GREEN, cal.getTimeInMillis(), dialogFragment);
            compactCalendarView.addEvent(ev1);


        }

        // final CompactCalendarView compactCalendarView = (CompactCalendarView) findViewById(R.id.CalendarView);

        //toolbar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                toolbar.setTitle(dateFormatForMonth.format(firstDayOfNewMonth));
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

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

}
