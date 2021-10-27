package tiles;

import app.Board;
import app.Player;
import javafx.scene.paint.Color;
import util.Position;

public abstract class Tile {

    private Position pos;
    private Color color;

    public Tile(int i, int j, Color color) {
        this.pos = new Position(i, j);
        this.color = color;
    }

    public abstract void onEnter(Player player, Board board);

    public Color getColor() {
        return color;
    }
    public Position getPosition() {
        return pos;
    }

}
