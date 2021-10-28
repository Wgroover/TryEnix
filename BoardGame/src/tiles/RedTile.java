package tiles;

import app.Board;
import app.Player;
import javafx.scene.paint.Color;

public class RedTile extends Tile {

    public static final int MONEY_LOSS = -10;

    public RedTile() {
        super(Color.RED);
    }

    @Override
    public void onEnter(Player player, Board board) {
        player.changeMoney(MONEY_LOSS);
    }
}
