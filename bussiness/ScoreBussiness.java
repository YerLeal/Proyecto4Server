package bussiness;

import data.Score;
import data.ScoreData;
import domain.Player;
import java.io.IOException;
import java.util.ArrayList;

public class ScoreBussiness {

    private ScoreData data;

    public ScoreBussiness() throws IOException {
        this.data = new ScoreData();
    }

    public boolean addNewScore(Score score) throws IOException {
        return this.data.addNewScore(score);
    } // addNewScore

    public ArrayList<Score> getAllScores() throws IOException {
        return this.data.getAllScores();
    } // getAllScores

} // end class
