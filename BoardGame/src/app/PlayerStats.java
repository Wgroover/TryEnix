package app;

public class PlayerStats {

    private Player player;

    private int tilesTraversed;
    private int minigamesWon;
    private int cookiesObtained;

    public PlayerStats(Player player) {
        this.player = player;
    }

    public int getTilesTraversed() {
        return tilesTraversed;
    }
    public void incrementTilesTraversed(int delta) {
        this.tilesTraversed += delta;
    }

    public int getMinigamesWon() {
        return minigamesWon;
    }
    public void incrementMinigamesWon(int delta) {
        this.minigamesWon += delta;
    }

    public int getCookiesObtained() {
        return cookiesObtained;
    }
    public void incrementCookiesObtained(int delta) {
        this.cookiesObtained += delta;
    }
}
