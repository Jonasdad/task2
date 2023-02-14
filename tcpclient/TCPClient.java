package tcpclient;
import java.net.*;
import java.io.*;
import java.util.*;

public class TCPClient {
    Integer limit;
    Integer timeout;
    boolean shutdown;
    
    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
        this.shutdown = shutdown;
        this.timeout = timeout;
        this.limit = limit;
    }
    
    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        Socket socket = new Socket(hostname, port);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //Timeout
        if(this.timeout != null){
            socket.setSoTimeout(this.timeout); 
        }
        //Writes data to server
        socket.getOutputStream().write(toServerBytes);
        //Receives data from server
        InputStream in = socket.getInputStream();
        //If shutdown argument is true from function call - Close and exit
        if(this.shutdown == true){
            System.out.println("Shutdown is true. Closing connection");
            socket.close();
            System.exit(1);
        }
        //Write data from server to a dynamic buffer and then return it as a byte array
        boolean foo = true;
        while(foo){
            out.write(in.read()); //Writes each byte
            //If the size of current buffer is limit -> Set foo false -> Exit loop
            //If there are no available bytes in inputstream -> Set foo false -> Exit loop
            if(Objects.equals(out.size(), this.limit) || in.available() == 0){
                foo = false;
            }
        }
        socket.close();
        return out.toByteArray();
        }
    }

