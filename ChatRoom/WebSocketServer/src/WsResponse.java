import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class WsResponse {

    private Socket clientSocket_;

    private String[] wsMsg_;

    private String room_;

    private static ArrayList<Socket> clients_ = new ArrayList<>();

    private boolean isLeave = false;

    WsResponse(Socket clientSocket, String[] wsMsg, String room){
        clientSocket_ = clientSocket;
        wsMsg_ = wsMsg;
        room_ = room;
        if(!clients_.contains(clientSocket)) {
            clients_.add(clientSocket);
        }
    }

    public void doit() throws IOException {

//        OutputStream out = clientSocket_.getOutputStream();
        DataOutputStream do = new DataOutputStream(clientSocket_.getOutputStream());

        OutputStream test = clientSocket_.getOutputStream();

            String send = new String();
//            String currentRoom = wsMsg_[2];
//            String currentUser = wsMsg_[1];
            if(wsMsg_[0].equals("join")) {
                send = new String("{  \"type\" : \"join\"," + "  \"room\" : \"" + wsMsg_[2] + "\"" + ",  \"user\" : " + "\"" + wsMsg_[1] + "\"" + " }");

            }
            else{
                send = new String("{  \"type\" : \"message\"," + "  \"user\" : \"" + wsMsg_[0] + "\"" + "," + "  \"room\" : \"" + room_ + "\"" + "," + "  \"message\" : " + "\"" + wsMsg_[1] + "\"" + " }");
            }

        System.out.println(send);
            byte[] stringBytes = send.getBytes();

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
//            out.write(sendback);
//            out.flush();




        for (Socket c:clients_) {
            OutputStream out = c.getOutputStream();
            out.write(sendback);
            out.flush();
        }
    }


}
