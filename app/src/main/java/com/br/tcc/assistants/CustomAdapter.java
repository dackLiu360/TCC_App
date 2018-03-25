package com.br.tcc.assistants;

import android.content.Context;
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

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<TaskModel> implements View.OnClickListener{

    private ArrayList<TaskModel> dataSet;
    Context mContext;
    private static CustomAdapter adapter;

    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView subject;
        TextView deadline;
        ImageView info;
    }

    public CustomAdapter(ArrayList<TaskModel> data, Context context) {
        super(context, R.layout.item_task, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        TaskModel dataModel=(TaskModel)object;

        switch (v.getId())
        {
            case R.id.item_info:
                Snackbar.make(v, "Descrição " +dataModel.getDescription(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
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
            viewHolder.title = (TextView) convertView.findViewById(R.id.name);
            viewHolder.subject = (TextView) convertView.findViewById(R.id.type);
            viewHolder.deadline = (TextView) convertView.findViewById(R.id.version_number);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

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