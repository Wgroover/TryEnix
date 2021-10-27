package tiles;

import app.Board;
import app.Player;
import javafx.scene.paint.Color;

public class ChanceTile extends Tile {

    public ChanceTile(int i, int j) {
        super(i, j, Color.BLUE);
    }

    @Override
    public void onEnter(Player player, Board board) {

    }
}
