package proyecto4server;

import domain.Player;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Proyecto4Server {

    public static Player players[] = new Player[2];
    public static String tamannoDeLaMatriz;

    public static void main(String[] args) {
        players[0] = null;
        try {
            System.out.println("Server Active ");
            ServerSocket mainServer = new ServerSocket(utilities.Constants.socketPortNumber);
            do {
                new MyServer(mainServer.accept()).start();
            } while (true);
        } catch (IOException ex) {
            Logger.getLogger(Proyecto4Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // main

} // end class
