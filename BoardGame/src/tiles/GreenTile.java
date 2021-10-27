package tiles;

import app.Board;
import app.Player;
import javafx.scene.paint.Color;

public class GreenTile extends Tile{

    public GreenTile(int i, int j) {
        super(i, j, Color.GREEN);
    }

    @Override
    public void onEnter(Player player, Board board) {
        player.changeMoney(10);
    }
}
