import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class WsRequest {

    private Socket clientSocket_;

    private String wsMsg_;

    private boolean isLeave = false;

    WsRequest(Socket clientSocket){
        clientSocket_ = clientSocket;
    }

    public void doit() throws IOException {
            InputStream is = clientSocket_.getInputStream();
            DataInputStream WsData = new DataInputStream(is);
        OutputStream os = clientSocket_.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        dos.w
            //read first 2 bytes
            byte firstByte = WsData.readByte();
            System.out.println("The first byte is : " + Byte.toString(firstByte));
            if(firstByte == -120){
                isLeave = true;
                System.out.println("isLeave = " + isLeave);
            }
            if(!isLeave) {
                byte secondByte = WsData.readByte();
                System.out.println("The second byte is : " + Byte.toString(secondByte));

                boolean isMasked = false;
                if ((secondByte & 0x80) != 0) {
                    isMasked = true;
                }

                int bytePayloadLength = secondByte & 0x7f;
                int payloadLength = 0;

                if (bytePayloadLength < 126) {
                    payloadLength = bytePayloadLength;
                } else if (bytePayloadLength == 126) {
                    payloadLength = WsData.readShort();
                } else {
//                    for (int i = 2; i <= 9; i++) {
                        payloadLength = (int) WsData.readLong();
//                    }
                }

                byte[] masks = new byte[4];
                byte[] encode = new byte[payloadLength]; //actual length?
                if (isMasked) {
                    masks = WsData.readNBytes(4);
//                WsData.readNBytes(masks,0,4);
                }
                encode = WsData.readNBytes(payloadLength);

                byte[] decode = new byte[encode.length];
                for (int i = 0; i < encode.length; i++) {
                    decode[i] = (byte) (encode[i] ^ masks[i % 4]);
                }
                wsMsg_ = new String(decode, StandardCharsets.UTF_8);
                System.out.println(Arrays.toString(decode));
                System.out.println(wsMsg_);
            }
    }

    public String getWsMsg_(){
        return wsMsg_;
    }

    public boolean getIsLeave(){
        return isLeave;
    }

}
