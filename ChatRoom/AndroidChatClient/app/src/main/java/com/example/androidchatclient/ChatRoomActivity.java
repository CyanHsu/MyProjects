package com.example.androidchatclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    static ArrayList<String> joinMessages_ = new ArrayList<>();

    static ListView lv_;

    static ArrayAdapter chatListAdapter_;

    private String userName_;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        TextView roomTitle = findViewById(R.id.roomTitle);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String roomName = extras.getString("roomName");
            userName_ = extras.getString("userName");

            roomTitle.setText(roomName);
        }



        chatListAdapter_ = new ArrayAdapter(this, android.R.layout.simple_list_item_1, joinMessages_);
        lv_ = findViewById(R.id.msgLV);
        lv_.setAdapter(chatListAdapter_);

    }
//    public static void updateLV(){
//        lv_.post(new Runnable() {
//            @Override
//            public void run() {
//                chatListAdapter_.notifyDataSetChanged();
//                lv_.smoothScrollToPosition(chatListAdapter_.getCount());
//            }
//        });
//
//    }

    public void handleSendMsgClick(View view){
        EditText msg = findViewById(R.id.msgET);
        MainActivity.ws_.sendText(userName_+ " " + msg.getText().toString());
        msg.setText(null);
    }

    public void handleLogOutClick(View view){
//        joinMessages_.add(MainActivity.ws_.sendClose().toString());
        String s = MainActivity.ws_.sendClose().toString();
        Log.i("websocket", s);
        MainActivity.ws_.sendClose();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}