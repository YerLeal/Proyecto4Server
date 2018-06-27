package domain;

public class Player {

    private String name,ip;
    private int score;

    public Player() {
        this.name = "";
        this.score = 0;
        this.ip="0.0.0.0";
    } // default

    public Player(String name, int score,String ip) {
        this.name = name;
        this.score = score;
        this.ip=ip;
    } // sobrecargado

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
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

    @Override
    public String toString() {
        return "Player{" + "name=" + name + ", score=" + score + '}';
    }

} // end class
