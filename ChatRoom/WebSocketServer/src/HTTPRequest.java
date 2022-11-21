import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class HTTPRequest {

    private Socket clientSocket_;
    private String filename;
    private HashMap<String, String> header_ = new HashMap<>();
    private boolean isWsRequest_ = false;

    HTTPRequest(Socket clientSocket){
        clientSocket_ = clientSocket;
    }

    public void doit() throws IOException {

        System.out.println("Client connected");
        InputStream inputStream = clientSocket_.getInputStream();
        Scanner sc = new Scanner(inputStream);

        String line = sc.nextLine();
        String[] lineSplit = line.split(" ", 3);
        filename = lineSplit[1];

        while (!line.equals("")) {
            System.out.println(line);
            line = sc.nextLine();
            if(!line.equals("")) {
                String[] headerSplit = line.split(": ");
                header_.put(headerSplit[0], headerSplit[1]);
            }
        }
        if(header_.get("Connection").equals("Upgrade")){
            isWsRequest_ = true;
        }
        System.out.println(header_.get("Sec-WebSocket-Key"));

//        System.out.println("map check");
//        System.out.println(header_.get("Sec-WebSocket-Key"));

        if (filename.equals("/")) {
            filename = "/index.html";
        }
        filename = "resource" + filename;
    }

    public String getFilename(){
        return filename;
    }

    public HashMap getHeader(){
        return header_;
    }

    public boolean getIsWsRequest(){
        return isWsRequest_;
    }

}
