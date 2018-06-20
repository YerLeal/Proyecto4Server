package proyecto4server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyServer extends Thread {

    private int socketPortNumber;

    public MyServer(int socketPortNumber) {
        this.socketPortNumber = socketPortNumber;;
    } // constructor

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.socketPortNumber);
            do {
                Socket socket = serverSocket.accept();
                PrintStream send = new PrintStream(socket.getOutputStream());
                BufferedReader receive = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                
                System.out.println(receive.readLine());
                
                socket.close();

            } while (true);
        } // run
        catch (IOException ex) {
            Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // run

} // end class
