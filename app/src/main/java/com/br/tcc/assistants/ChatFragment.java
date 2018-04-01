package com.br.tcc.assistants;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.victor.tcc.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Victor on 3/30/2018.
 */

public class ChatFragment extends DialogFragment {



    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activity_main;
    ListView listOfMessage;
    FloatingActionButton fab;
    View rootView;
    String taskID;
    Activity a;
    int counterBefore;
    ImageView closeButton;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.activity_chat, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        a=getActivity();
        activity_main = rootView.findViewById(R.id.activity_chat);
        closeButton = rootView.findViewById(R.id.imageView_close);
        System.out.println("ATIVIDADE "+activity_main.toString());
        fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)rootView.findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().child("Tasks").child(taskID).push().setValue(new ChatMessage(input.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
                input.setText("");
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        // if(FirebaseAuth.getInstance().getCurrentUser() == null){
         //startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        // }
         //else{
        displayChatMessage();

        return rootView;
    }

        private void displayChatMessage() {


        listOfMessage = (ListView)rootView.findViewById(R.id.list_of_message);
        adapter = new FirebaseListAdapter<ChatMessage>(a,ChatMessage.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference().child("Tasks").child(taskID))

        {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                //Get references to the views of list_item.xml
                TextView messageText, messageUser, messageTime;
                messageText = (TextView) v.findViewById(R.id.message_text);
                messageUser = (TextView) v.findViewById(R.id.message_user);
                messageTime = (TextView) v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
                System.out.println(adapter.getCount());

                if(adapter.getCount()!=counterBefore){
                    scrollMyListViewToBottom();
                    counterBefore = adapter.getCount();
                }

            }
        };


        listOfMessage.setAdapter(adapter);
    }

    public void setData(String taskID){
        this.taskID = taskID;

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void scrollMyListViewToBottom() {
        listOfMessage.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listOfMessage.setSelection(adapter.getCount() - 1);
            }
        });
    }

}
