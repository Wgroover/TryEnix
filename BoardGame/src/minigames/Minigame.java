package minigames;

import java.util.List;

import app.Player;

public abstract class Minigame {
    private List<Player> players;

    public Minigame(List<Player> players) {
        this.players = players;
    }

    public abstract void startGame();
    public abstract void endGame();

    public List<Player> getPlayers() {
        return players;
    }
}
