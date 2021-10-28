package tiles;

import app.Board;
import app.Player;
import javafx.scene.paint.Color;

public class GreenTile extends Tile {

    public static final int MONEY_GAIN = 15;

    public GreenTile() {
        super(Color.GREEN);
    }

    @Override
    public void onEnter(Player player, Board board) {
        player.changeMoney(MONEY_GAIN);
    }
}
