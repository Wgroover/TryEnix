package tiles;

import app.Board;
import app.Player;
import javafx.scene.paint.Color;

public class PaywallTile extends Tile {

    public PaywallTile(int i, int j) {
        super(i, j, Color.GRAY);
    }

    @Override
    public void onEnter(Player player, Board board) {

    }

}
