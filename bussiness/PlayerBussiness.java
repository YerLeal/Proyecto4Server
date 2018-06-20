package bussiness;

import data.PlayerData;
import domain.Player;
import java.io.IOException;
import java.util.ArrayList;

public class PlayerBussiness {

    private PlayerData data;

    public PlayerBussiness() throws IOException {
        this.data = new PlayerData();
    }

    public boolean addNewPlayer(Player player) throws IOException {
        return this.data.addNewPlayer(player);
    } // addNewPlayer

    public ArrayList<Player> getAllPlayer() throws IOException {
        return this.data.getAllPlayer();
    } // getAllPlayer

} // end class
