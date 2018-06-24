package proyecto4server;

import domain.Player;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyServer extends Thread {

    private Socket socket;
    private String action;
    private String nameClient;
    private String data;
    private HashSet<String> names = new HashSet<String>();
    private static HashSet<DataOutputStream> writers = new HashSet<DataOutputStream>();

    public MyServer(Socket socket) {
        this.socket = socket;
        this.action = "";
        this.nameClient = "";
        this.data = "";
    } // constructor

    @Override
    public void run() {
        try {
            DataOutputStream send = new DataOutputStream(socket.getOutputStream());
            DataInputStream receive = new DataInputStream(socket.getInputStream());

            // 1 com: envio datos del server
            send.writeUTF("This is the server (Jueguito)");

            // 4 com: recivo accion y datos
            String actionAndData = receive.readUTF();
            separarActionAndData(actionAndData);

            if (this.action.equals("chat")) {
                if (!names.contains(nameClient) && !nameClient.isEmpty()) {
                    names.add(nameClient);
                    nameClient = "";
                }
                writers.add(send);
                while (true) {
                    String input = receive.readLine();

                    if (input == null) {
                        return;
                    }
                    for (DataOutputStream writer : writers) {
                        writer.writeUTF("MESSAGE " + nameClient + ": " + input);
                    }
                }
            } else if (this.action.equals("log")) {
                System.out.println(Proyecto4Server.players[0]==null);
                if (Proyecto4Server.players[0]==null) {
                    Proyecto4Server.players[0] = new Player(this.nameClient, 0);
                    send.writeUTF(String.valueOf(1));
                } else {
                    if(Proyecto4Server.players[1]==null){
                    Proyecto4Server.players[1] = new Player(this.nameClient, 0);
                    send.writeUTF(String.valueOf(2));
                    }else{
                        send.writeUTF("Connection refused");
                    }
                }
            }
            socket.close();
        } // run
        catch (IOException ex) {
            Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // run

    private void separarActionAndData(String actionAndData) {
        // Token &
        StringTokenizer st = new StringTokenizer(actionAndData, "&");
        this.action = st.nextToken();
        int count;
        if (this.action.equals("chat")) {
            count = 1;
            while (st.hasMoreTokens()) {
                if (count == 2) {
                    this.nameClient = st.nextToken();
                } else if (count == 3) {
                    this.data = st.nextToken();
                }
                count++;
            }
        } else if (this.action.equals("log")) {
            System.out.println("LOG");
            count = 1;
            while (st.hasMoreElements()) {
                if (count == 2) {
                    this.nameClient = st.nextToken();
                }
                count++;
            }
        }
        System.out.println("Action: " + action);
        System.out.println("Name client: " + nameClient);
        System.out.println("Data: " + data);
        System.out.println();
    }

} // end class
