package data;

import domain.Player;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class PlayerData {

    private RandomAccessFile randomAccessFile;
    private int regsQuantity;
    private int regSize;

    public PlayerData() throws IOException {
        File file = new File(utilities.Constants.pathScorePlayer);
        this.regSize = 100;
        if (file.exists() && !file.isFile()) {
            throw new IOException(file.getName() + " is an invalid file");
        } else {
            this.randomAccessFile = new RandomAccessFile(file, "rw");
            this.regsQuantity = (int) Math.ceil((double) this.randomAccessFile.length() / (double) regSize);
        }
    } // constructor

    private boolean putValue(int position, Player player) throws IOException {
        if (!(position >= 0 && position <= this.regsQuantity)) {
            System.err.println("1001 - Record position is out of bounds");
            return false;
        } else {
            if (player.sizeInBytes() > this.regSize) {
                System.err.println("1002 - Record size id out of bounds");
                return false;
            } else {
                this.randomAccessFile.seek(position * this.regSize);
                this.randomAccessFile.writeUTF(player.getName());
                this.randomAccessFile.writeInt(player.getScore());
                return true;
            }
        }
    } // putValue: inserta un nuevo jugador al registro

    public boolean addNewPlayer(Player player) throws IOException {
        boolean success = putValue(this.regsQuantity, player);
        if (success) {
            ++this.regsQuantity;
        } // if
        return success;
    } // addNewPlayer: llama a putValue para insertar jugador

    private Player getPlayer(int position) throws IOException {
        if (position >= 0 && position <= this.regsQuantity) {
            this.randomAccessFile.seek(position * this.regSize);
            Player tempPlayer = new Player();
            tempPlayer.setName(this.randomAccessFile.readUTF());
            tempPlayer.setScore(this.randomAccessFile.readInt());
            return tempPlayer;
        } else {
            System.err.println("1003 - position is out of bouns");
            return null;
        }
    } // getPlayer: retorna jugador segun posicion

    public ArrayList<Player> getAllPlayer() throws IOException {
        ArrayList<Player> playersArray = new ArrayList<>();
        for (int i = 0; i < this.regsQuantity; i++) {
            Player tempPlayer = this.getPlayer(i);
            if (tempPlayer != null) {
                playersArray.add(tempPlayer);
            }
        }
        return playersArray;
    } // getAllPlayer: retorna todos los jugadores

} // end class
