package com.example.androidchatclient;

import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.google.gson.JsonObject;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class MyWebSocket extends WebSocketAdapter {

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        super.onConnected(websocket, headers);
        Log.i("websocket", "ws" + websocket.isOpen());
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        super.onTextMessage(websocket, text);
        JSONObject received = new JSONObject(text);

        String msg = null;
        if(received.get("type").equals("join")) {
            msg = received.get("user") + " has joined the room";
        }
        else if (received.get("type").equals("leave")) {
            msg = received.get("user") + " left";
        }
        else if (received.get("type").equals("message")){
            msg = received.get("user") + " : " + received.get("message");
        }
            ChatRoomActivity.joinMessages_.add(msg);
            ChatRoomActivity.lv_.post(new Runnable() {
                @Override
                public void run() {
                    ChatRoomActivity.chatListAdapter_.notifyDataSetChanged();
                    ChatRoomActivity.lv_.smoothScrollToPosition(ChatRoomActivity.chatListAdapter_.getCount());
                }
            });
//            ChatRoomActivity.updateLV();


        Log.i("websocket", text);
    }
}
