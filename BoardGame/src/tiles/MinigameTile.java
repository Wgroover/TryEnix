package tiles;

import java.util.ArrayList;
import java.util.List;

import app.Board;
import app.Player;
import javafx.scene.paint.Color;
import minigames.Minigame;
import minigames.RockPaperScissors;

public class MinigameTile extends Tile {

    public MinigameTile() {
        super(Color.ORANGE);
    }

    @Override
    public void onEnter(Player player, Board board) {
        List<Player> players = new ArrayList<>();
        players.add(player);
        Player otherplayer = player;
        while (player == otherplayer) {
            otherplayer = board.getPlayers().get((int) (Math.random() * board.getPlayers().size()));
        }
        players.add(otherplayer);

        Minigame rps = new RockPaperScissors(players);

        rps.startGame();
    }
}
