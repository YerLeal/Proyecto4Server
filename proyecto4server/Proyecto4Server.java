package proyecto4server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Proyecto4Server {

    public static void main(String[] args) {
        try {
            System.out.println("Server IP: " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {
            Logger.getLogger(Proyecto4Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        MyServer myServer = new MyServer(utilities.Constants.socketPortNumber);
        myServer.start();
    } // main

} // end class
