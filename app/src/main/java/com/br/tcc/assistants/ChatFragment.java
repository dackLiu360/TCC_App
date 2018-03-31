package com.br.tcc.assistants;

import android.app.Activity;
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
    FloatingActionButton fab;
    View rootView;
    String taskID;
    String username;
    String email;
    String password;
    Activity a;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.activity_chat, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        a=getActivity();
        activity_main = rootView.findViewById(R.id.activity_chat);
        System.out.println("ATIVIDADE "+activity_main.toString());
        fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)rootView.findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().child(taskID).push().setValue(new ChatMessage(input.getText().toString(), username));
                input.setText("");
            }
        });


        // if(FirebaseAuth.getInstance().getCurrentUser() == null){
        // startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        // }
         //else{
        System.out.println("EMAIL: "+email);
        System.out.println("PASSWORD: "+password);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword("teste@teste3.com", "123456");
         //@}

        displayChatMessage();

        return rootView;
    }

        private void displayChatMessage() {


        ListView listOfMessage = (ListView)rootView.findViewById(R.id.list_of_message);
            System.out.println("TAREFA "+taskID);
        adapter = new FirebaseListAdapter<ChatMessage>(a,ChatMessage.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference().child(taskID))

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

            }
        };
        listOfMessage.setAdapter(adapter);
    }

    public void setData(String taskID, String username, String email, String password, Activity a){
        this.taskID = taskID;
        this.username = username;
        this.email = email;
        this.password=password;
        //this.a = a;
    }

}
