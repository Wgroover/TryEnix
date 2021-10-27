package tiles;

import app.Board;
import app.Player;
import javafx.scene.paint.Color;

public class RedTile extends Tile {

    public RedTile(int i, int j) {
        super(i, j, Color.RED);
    }

    @Override
    public void onEnter(Player player, Board board) {
        player.changeMoney(-10);
    }
}
