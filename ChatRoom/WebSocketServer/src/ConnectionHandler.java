import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class ConnectionHandler implements Runnable{

    private Socket clientSocket_;
    private String currentRoom;
    private boolean isLeave = false;
    private String[] wsMsgParse;

    private static ArrayList<Socket> clientSockets_ = new ArrayList<>();

    ConnectionHandler(Socket clientSocket){
        clientSocket_ = clientSocket;
        clientSockets_.add(clientSocket);
    }

    @Override
    public void run() {

        HTTPRequest request = new HTTPRequest(clientSocket_);
        try {
            request.doit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HTTPResponse response = new HTTPResponse(clientSocket_, request.getFilename(), request.getHeader());
        try {
            response.doit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        if(request.getIsWsRequest()) {
            while(true){
                //read the WS
                WsRequest wsRequest = new WsRequest(clientSocket_);
                try {
                    isLeave = false;
                    wsRequest.doit();
                    isLeave = wsRequest.getIsLeave();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(!isLeave) {
                    wsMsgParse = wsRequest.getWsMsg_().split(" ", 3);


                    if(wsMsgParse[0].equals("join")) {
                        //create a chatroom
                        Room chatRoom = Room.getRoom(wsMsgParse[2]);
                        //add client to the room
                        chatRoom.addClient(clientSocket_, wsMsgParse[1]);
                        //inform everyone in the room someone joins the room
                        try {
                            chatRoom.joinRoomResponse(wsMsgParse[1]);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {//send msg --------<user>  <message>
                        //check where is the user
                        Room talkRoom = Room.findUser(clientSocket_);
                        //send the msg to all user in the room
                        try {
                            talkRoom.msgResponse(wsMsgParse);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
//                    currentRoom = wsMsgParse[2];
                }
                    //leave
                else if(isLeave){
                    Room leaveRoom = Room.findUser(clientSocket_);
                    try {
                        leaveRoom.leaveResponse(wsMsgParse,clientSocket_);
//                        clientSocket_.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }





//                if(wsMsgParse.length>0){
//                    WsResponse wsResponse = new WsResponse(clientSocket_ , wsMsgParse, currentRoom);
//                    try {
//                        wsResponse.doit();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
            }
        }

        // we are done using the client socket, we can close it now.
        else{
            try {
                clientSocket_.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }












}
