import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class WebServer {

    private ServerSocket serversocket;


//    private ArrayList<ConnectionHandler> connections_;

    public WebServer(int port) throws IOException, InterruptedException {

        serversocket = new ServerSocket(port);
        System.out.println("Waiting for connection");
        while( true ) {
            Socket clientSocket = serversocket.accept();

            ConnectionHandler ch = new ConnectionHandler(clientSocket);

            Thread clientTread = new Thread(ch);
            clientTread.start();
        }

    }

}
