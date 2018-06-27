package data;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ScoreData {

    private RandomAccessFile randomAccessFile;
    private int regsQuantity;
    private int regSize;

    public ScoreData() throws IOException {
        File file = new File(utilities.Constants.pathScorePlayer);
        this.regSize = 100;
        if (file.exists() && !file.isFile()) {
            throw new IOException(file.getName() + " is an invalid file");
        } else {
            this.randomAccessFile = new RandomAccessFile(file, "rw");
            this.regsQuantity = (int) Math.ceil((double) this.randomAccessFile.length() / (double) regSize);
        }
    } // constructor

    private boolean putValue(int position, Score score) throws IOException {
        if (!(position >= 0 && position <= this.regsQuantity)) {
            System.err.println("1001 - Record position is out of bounds");
            return false;
        } else {
            if (score.sizeInBytes() > this.regSize) {
                System.err.println("1002 - Record size id out of bounds");
                return false;
            } else {
                this.randomAccessFile.seek(position * this.regSize);
                this.randomAccessFile.writeUTF(score.getName());
                this.randomAccessFile.writeInt(score.getScore());
                return true;
            }
        }
    } // putValue: inserta un nuevo score al registro

    public boolean addNewScore(Score score) throws IOException {
        boolean success = putValue(this.regsQuantity, score);
        if (success) {
            ++this.regsQuantity;
        } // if
        return success;
    } // addNewScore: llama a putValue para insertar un nuevo score

    private Score getScore(int position) throws IOException {
        if (position >= 0 && position <= this.regsQuantity) {
            this.randomAccessFile.seek(position * this.regSize);
            Score tempPlayer = new Score();
            tempPlayer.setName(this.randomAccessFile.readUTF());
            tempPlayer.setScore(this.randomAccessFile.readInt());
            return tempPlayer;
        } else {
            System.err.println("1003 - position is out of bouns");
            return null;
        }
    } // getScore: retorna score segun posicion

    public ArrayList<Score> getAllScores() throws IOException {
        ArrayList<Score> scores = new ArrayList<>();
        for (int i = 0; i < this.regsQuantity; i++) {
            Score tempScore = this.getScore(i);
            if (tempScore != null) {
                scores.add(tempScore);
            }
        }
        return scores;
    } // getAllScores: retorna todos los jugadores

} // end class
