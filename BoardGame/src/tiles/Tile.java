package tiles;

import app.Board;
import app.Player;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import util.Position;

public abstract class Tile {

    private ObjectProperty<Color> color;

    public Tile(Color color) {
        this.color = new SimpleObjectProperty<>(color);
    }

    public abstract void onEnter(Player player, Board board);

    public Color getColor() {
        return color.getValue();
    }
    public ObjectProperty<Color> getColorProperty() {
        return color;
    }

}
