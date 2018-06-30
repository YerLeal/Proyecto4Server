package proyecto4server;

import bussiness.ScoreBussiness;
import data.Score;
import domain.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
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
                    } else {
                        if (Proyecto4Server.players[1] == null) {
                            Proyecto4Server.players[1] = new Player(datos[1], 0, ipClient);
                            send.writeUTF(String.valueOf(2));
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
                        ip = Proyecto4Server.players[0].getIp();
                    }
                    Socket destiny = new Socket(ip, Constants.atackPortNumber);
                    DataOutputStream dat = new DataOutputStream(destiny.getOutputStream());
                    dat.writeUTF(dataR);
                    dat.close();
                    destiny.close();
                    break;
                case "saveScore":
                    String stringScore = receive.readUTF();
                    Score newData = fromElementToScore(fromStringToElement(stringScore));
                    ScoreBussiness bussiness = new ScoreBussiness();
                    bussiness.addNewScore(newData);
                    break;
                    
                case "getScore":
                    ScoreBussiness bussiness1 = new ScoreBussiness();
                    ArrayList<Score> allScores = bussiness1.getAllScores();
                    send.writeInt(allScores.size());
                    for(int i=0; i<allScores.size(); i++){
                        send.writeUTF(fromScoreToString(allScores.get(i)));
                    }
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

    private Score fromElementToScore(Element eScore) {
        Score score = new Score();
        score.setName(eScore.getAttributeValue("name"));
        score.setScore(Integer.parseInt(eScore.getChildText("score")));
        return score;
    } // fromElementToScore

    private Element fromStringToElement(String scoreString) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            StringReader stringReader = new StringReader(scoreString);
            Document doc = saxBuilder.build(stringReader);
            Element eScore = doc.getRootElement();
            return eScore;
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    } // fromStringToElement

    private String fromScoreToString(Score score) {
        Element eScore = new Element("score");
        Element eScor = new Element("scor");
        eScore.setAttribute("name", score.getName());
        eScor.addContent(String.valueOf(score.getScore()));
        eScore.addContent(eScor);
        XMLOutputter output = new XMLOutputter(Format.getCompactFormat());
        String xmlStringElementEStudent = output.outputString(eScore);
        xmlStringElementEStudent = xmlStringElementEStudent.replace("\n", "");
        return xmlStringElementEStudent;
    } // fromStudentToString
} // end class
