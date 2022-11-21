package com.example.androidchatclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    static WebSocket ws_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            ws_ = new WebSocketFactory().createSocket("ws://10.0.2.2:8080/endpoint", 1000 );
        }
        catch( IOException e ) {
            Log.e( "Dd:","WS error" );
        }
        ws_.addListener(new MyWebSocket());
        ws_.connectAsynchronously();
    }

    public void handleJoinClick(View view){


        EditText userName = findViewById(R.id.userNameET);
        EditText roomName = findViewById(R.id.roomNameET);

        Context context = getApplicationContext();
        CharSequence text = "User name or Room name can't be empty ";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,text,duration);


        ws_.sendText("join " +userName.getText().toString()+ " " + roomName.getText().toString() );

        if(userName.getText().toString().equals("") || roomName.getText().toString().equals("")){
            toast.show();
        }
        else{
            Intent intent = new Intent(this, ChatRoomActivity.class);
            intent.putExtra("roomName", roomName.getText().toString());
            intent.putExtra("userName", userName.getText().toString());
            startActivity(intent);

        }


    }

}