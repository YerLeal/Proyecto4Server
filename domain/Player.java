package domain;

public class Player {

    private String name;
    private int score;

    public Player() {
        this.name = "";
        this.score = 0;
    } // default

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    } // sobrecargado

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int sizeInBytes() {
        return this.name.length() * 2 + 8;
    }

    @Override
    public String toString() {
        return "Player{" + "name=" + name + ", score=" + score + '}';
    }

} // end class
