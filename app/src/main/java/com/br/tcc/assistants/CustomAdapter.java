package com.br.tcc.assistants;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victor.tcc.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<TaskModel> implements View.OnClickListener{

    private ArrayList<TaskModel> dataSet;
    Context mContext;
    private static CustomAdapter adapter;
    FragmentManager fm;
    Activity a;




    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView subject;
        TextView deadline;
        ImageView info;
    }

    public CustomAdapter(ArrayList<TaskModel> data, Context context, FragmentManager fm, Activity a) {
        super(context, R.layout.item_task, data);
        this.dataSet = data;
        this.mContext=context;
        this.fm =fm;
        this.a=a;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        TaskModel dataModel=(TaskModel)object;

        switch (v.getId())
        {
            case R.id.description:
                String json;
                SharedPreferences appSharedPrefs = PreferenceManager
                        .getDefaultSharedPreferences(mContext);
                Gson gson = new Gson();
                json = appSharedPrefs.getString("username", "");
                String username = gson.fromJson(json, String.class);
                json = appSharedPrefs.getString("email", "");
                String email = gson.fromJson(json, String.class);
                json = appSharedPrefs.getString("password", "");
                String password = gson.fromJson(json, String.class);
                ChatFragment chat = new ChatFragment();
                chat.setData(dataModel.getId_task(),username, email, password, a);
                chat.show(fm,"Chat");
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TaskModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_task, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title_task);
            viewHolder.subject = (TextView) convertView.findViewById(R.id.subject);
            viewHolder.deadline = (TextView) convertView.findViewById(R.id.deadline);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.description);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.title.setText(dataModel.getTitle());
        viewHolder.subject.setText(dataModel.getSubject());
        viewHolder.deadline.setText(dataModel.getDeadline());
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}