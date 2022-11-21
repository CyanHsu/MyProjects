import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;

public class HTTPResponse  {

    private Socket clientSocket_;
    private String filename_;
    private HashMap<String, String> header_;
    
    private PrintWriter pw_;

    HTTPResponse(Socket clientSocket, String filename,HashMap header){
        clientSocket_ = clientSocket;
        filename_ = filename;
        header_ = header;

    }

    public PrintWriter getPrintWriter(){
        return pw_;
    }

    public void doit() throws IOException, InterruptedException, NoSuchAlgorithmException {
        String fileResult;

        //open the request file(filename)
        File file = new File(filename_);
        try {
            if (file.exists()) {
                fileResult = "200 Success";
            } else {
                throw new FileNotFoundException();
            }
        }catch(FileNotFoundException e){
            fileResult = "404 not found";
        }


        //send the response header, send "http/1.x + result"
        OutputStream out = clientSocket_.getOutputStream();
        pw_ = new PrintWriter(out);
        boolean WSConnect = false;

        String encodeKey = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((header_.get("Sec-WebSocket-Key") + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")));

        if (header_.containsKey("Sec-WebSocket-Key")){
            WSConnect = true;
        }else {
            WSConnect = false;
        }

        if(WSConnect){
            pw_.println("HTTP/1.1 101 Switching Protocols" );
            pw_.println("Upgrade: "+header_.get("Upgrade"));
            pw_.println("Connection: "+header_.get("Connection"));
            pw_.println("Sec-WebSocket-Accept: " + encodeKey);

            System.out.println("HTTP/1.1 101 Switching Protocols" );
            System.out.println("Upgrade: websocket");
            System.out.println("Connection: Upgrade");
            System.out.println("Sec-WebSocket-Accept: " + encodeKey);

            pw_.print("\n");
            pw_.flush();

        }
        else {
            pw_.println("HTTP/1.1 " + fileResult);
            String[] fileType = filename_.split("\\.", 2);
            pw_.println("Content-Type: text/" + fileType[1]);
            pw_.println("Content-Length: " + file.length());

            pw_.print("\n");
            pw_.flush();

            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.transferTo(out);
            pw_.flush();
            System.out.println("hello");

        }





        // send the date from the file...
//        FileInputStream fileInputStream = new FileInputStream(file);

//        try {
//                fileInputStream.transferTo(out);
//                pw.flush();

//            } catch (IOException e) {
//                System.out.println("File is unable to send");
//                pw.println("File is unable to send");
//                pw.flush();
//                pw.close();
//            }

        // Add delay to check if Thread works
//            System.out.println(file.length());
//            for( int i = 0; i <= file.length(); i++ ) {
//                pw.write( fileInputStream.read() );
//                pw.flush();
//                Thread.sleep( 10 ); // Maybe add <- if images are still loading too quickly...
//            }

        // close
//            pw.close();
//            out.close();

    }



}
