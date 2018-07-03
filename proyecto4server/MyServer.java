package proyecto4server;

import bussiness.ScoreBussiness;
import domain.Score;
import domain.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
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
            String datos[] = receive.readUTF().split("&");
            this.action = datos[0];
            switch (this.action) {
                case "chat":
                    String ip1;
                    String playerNumber = datos[1];
                    String message = "";
                    if (playerNumber.equals("1")) {
                        message = Proyecto4Server.players[Integer.parseInt(playerNumber) - 1].getName() + ":";
                        ip1 = Proyecto4Server.players[1].getIp();
                    } else {
                        message = Proyecto4Server.players[Integer.parseInt(playerNumber) - 1].getName() + ":";
                        ip1 = Proyecto4Server.players[0].getIp();
                    }
                    message += datos[2];
                    Socket destiny1 = new Socket(ip1, Constants.chatPortNumber);
                    DataOutputStream dat1 = new DataOutputStream(destiny1.getOutputStream());
                    dat1.writeUTF(message);
                    dat1.close();
                    destiny1.close();
                    break;
                case "log":
                    String ipClient = socket.getInetAddress().getHostAddress();
                    if (Proyecto4Server.players[0] == null) {
                        Proyecto4Server.players[0] = new Player(datos[1], 0, ipClient);
                        send.writeUTF(String.valueOf(1));
                        Proyecto4Server.tamannoDeLaMatriz = receive.readUTF(); // el 1 me manda tamanno
                    } else {
                        if (Proyecto4Server.players[1] == null) {
                            Proyecto4Server.players[1] = new Player(datos[1], 0, ipClient);
                            send.writeUTF(String.valueOf(2));
                            send.writeUTF(Proyecto4Server.tamannoDeLaMatriz); // le envio tamanno al 2
                        } else {
                            send.writeUTF("Connection refused");
                        }
                    }
                    break;
                case "attack":
                    String dataR = datos[2] + "&" + datos[3];
                    String ip;
                    String aux = datos[1];
                    if (aux.equals("1")) {
                        ip = Proyecto4Server.players[0].getIp();
                    } else {
                        ip = Proyecto4Server.players[1].getIp();
                    }
                    Socket destiny = new Socket(ip, Constants.atackPortNumber);
                    DataOutputStream dat = new DataOutputStream(destiny.getOutputStream());
                    dat.writeUTF(dataR);
                    dat.close();
                    destiny.close();
                    break;
                case "score":
                    ScoreBussiness bussiness = new ScoreBussiness();
                    if (datos[1].equals(1)) {
                        bussiness.addNewScore(new Score(Proyecto4Server.players[0].getName(), Integer.parseInt(datos[2])));
                    } else {
                        bussiness.addNewScore(new Score(Proyecto4Server.players[0].getName(), Integer.parseInt(datos[2])));
                    }
                    break;
                case "getScore":
                    ScoreBussiness bussines=new ScoreBussiness();
                    List<Score> list=bussines.getAllScores();
                    ObjectOutputStream oOS=new ObjectOutputStream(socket.getOutputStream());
                    oOS.writeObject(list);
                    oOS.close();
                    break;
                case "end":
                    String ipe;
                    String number = datos[1];
                    if (number.equals("1")) {
                        ipe = Proyecto4Server.players[0].getIp();
                    } else {
                        ipe = Proyecto4Server.players[0].getIp();
                    }
                    Socket endDestiny = new Socket(ipe, Constants.atackPortNumber);
                    DataOutputStream endDat = new DataOutputStream(endDestiny.getOutputStream());
                    endDat.writeUTF("end");
                    endDat.close();
                    endDestiny.close();
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
