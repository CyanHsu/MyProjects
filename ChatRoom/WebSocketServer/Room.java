import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Room {
    private String name_;

    private ArrayList<Socket> clients_ = new ArrayList<>();

    private static ArrayList<Room> rooms_ = new ArrayList<>();

    private ArrayList<String> oldClients = new ArrayList<>();

    private HashMap<Socket,String> userMap_ = new HashMap<>();

    private Room(String name){
        name_ = name;
    }

    public synchronized void addClient(Socket client, String user){
        if (!clients_.contains(client)){
            clients_.add(client);
            userMap_.put(client,user);
        }
        else {
            return;
        }
    }

    public synchronized static Room getRoom(String name){
        for (Room room: rooms_) {
            if(room.name_.equals(name)){
                return room;
            }
        }
        Room newRoom = new Room(name);
        rooms_.add(newRoom);
        return newRoom;
    }


    public synchronized void joinRoomResponse(String user) throws IOException {
        sendToLatestClient(oldClients);
        String send = new String("{  \"type\" : \"join\"," + "  \"room\" : \"" + name_ + "\"" + ",  \"user\" : " + "\"" + user + "\"" + " }");
        oldClients.add(send);
        sendToAllClients(send);
    }

    public void sendToLatestClient(ArrayList<String> text) throws IOException {
        for(String s : text) {
            byte[] response = responseText(s);
            OutputStream out = clients_.get(clients_.size()-1).getOutputStream();
            out.write(response);
            out.flush();
        }

    }
    public void sendToAllClients(String text) throws IOException {
        byte[] response = responseText(text);

        for (Socket c:clients_) {
            OutputStream out = c.getOutputStream();
            out.write(response);
            out.flush();
        }
    }

    private byte[] responseText(String text) {
        byte[] stringBytes = text.getBytes();
        int textLength = stringBytes.length;
        int payloadExtend = 0;
        int textLengthByte = 0;
        if(textLength < 126){
            textLengthByte = textLength;
        }
        else if(textLength < 65536){
            payloadExtend = 2;
            textLengthByte = 126;
        }
        else{
            payloadExtend = 8;
            textLengthByte = 127;
        }

        byte[] sendback = new byte[2+payloadExtend+textLength];
        sendback[0] = (byte) 0x81;
        sendback[1] = (byte) (textLengthByte & 0x7f);
        if((textLengthByte & 0x7f) < 126){
            for (int i = 0; i < textLength ; i++){
                sendback[i+2] = stringBytes[i];
            }
        }
        else if ((textLengthByte & 0x7f) == 126){
            sendback[2] = (byte) ((textLength >> 8) & 0xff);
            sendback[3] = (byte) (textLength & 0xff);
            for (int i = 0; i < textLength ; i++) {
                sendback[i + 4] = stringBytes[i];
            }
        }
        else {
            for (int i = 2; i < 9; i++) {
                sendback[i] = (byte) (textLength >> (64- (8*(i-1))) & 0xff);
            }
            for (int i = 0; i < textLength ; i++) {
                sendback[i + 9] = stringBytes[i];
            }
        }
        return sendback;
    }


    public synchronized static Room findUser(Socket cs){
        Room targetRoom = null;
        for(Room room : rooms_){
            for(Socket s : room.clients_){
                if(s == cs){
                    targetRoom = room;
                }
            }
        }
        return targetRoom;
    }


    public synchronized void msgResponse(String[] wsMsg_) throws IOException {
        String send = new String("{  \"type\" : \"message\"," + "  \"user\" : \"" + wsMsg_[0] + "\"" + "," + "  \"room\" : \"" + name_+ "\"" + "," + "  \"message\" : " + "\"" + wsMsg_[1] + "\"" + " }");
        sendToAllClients(send);
    }
    public synchronized void leaveResponse(String[] wsMsg_, Socket client) throws IOException {
        String send = new String("{  \"type\" : \"leave\"," + "  \"room\" : \"" + name_ + "\"" + ",  \"user\" : " + "\"" + userMap_.get(client) + "\"" + " }");
        sendToAllClients(send);
        clients_.remove(client);
        oldClients.add(send);
    }

}
