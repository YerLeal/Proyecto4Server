package proyecto4server;

import domain.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.Constants;

public class MyServer extends Thread {

    private Socket socket;
    private String action;
    
    public MyServer(Socket socket) {
        this.socket = socket;
        this.action = "";
    } // constructor

    @Override
    public void run() {
        try {
            
            DataOutputStream send = new DataOutputStream(socket.getOutputStream());
            DataInputStream receive = new DataInputStream(socket.getInputStream());
            
            // 1 com: envio datos del server
            //send.writeUTF("This is the server (Jueguito)");

            // 4 com: recivo accion y datos
            String datos[]=receive.readUTF().split("&");
            this.action = datos[0];
            
            switch (this.action) {
                case "chat":
                    String ip1;
                    String aux1=datos[1];
                    String message="";
                    if(aux1.equals("1")){
                        message=Proyecto4Server.players[Integer.parseInt(aux1)-1].getName()+":";
                        ip1=Proyecto4Server.players[1].getIp();
                    }else{
                        message=Proyecto4Server.players[Integer.parseInt(aux1)-1].getName()+":";
                        ip1=Proyecto4Server.players[0].getIp();
                    }
                    message+=datos[2];
                    Socket destiny1=new Socket(ip1, Constants.chatPortNumber);
                    DataOutputStream dat1=new DataOutputStream(destiny1.getOutputStream());
                    dat1.writeUTF(message);
                    dat1.close();
                    destiny1.close();
                    break;
                case "log":
                    String ipClient=socket.getInetAddress().getHostAddress();
                    if (Proyecto4Server.players[0]==null) {
                        Proyecto4Server.players[0] = new Player(datos[1], 0,ipClient);
                        send.writeUTF(String.valueOf(1));  
                    } else {
                        if(Proyecto4Server.players[1]==null){
                            Proyecto4Server.players[1] = new Player(datos[1], 0,ipClient);
                            send.writeUTF(String.valueOf(2));
                        }else{
                            send.writeUTF("Connection refused");
                        }
                    }   break;
                case "attack":
                    String dataR=datos[2]+"&"+datos[3];
                    String ip;
                    String aux=datos[1];
                    if(aux.equals("1")){
                        ip=Proyecto4Server.players[1].getIp();
                    }else{
                        ip=Proyecto4Server.players[0].getIp();
                    }
                    Socket destiny=new Socket(ip, Constants.atackPortNumber);
                    DataOutputStream dat=new DataOutputStream(destiny.getOutputStream());
                    dat.writeUTF(dataR);
                    dat.close();
                    destiny.close();
                    break;
                case "saveScore":
                    
                    break;
                default:
                    break;
            }
            socket.close();
        } // run
        catch (IOException ex) {
            Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // run


} // end class
