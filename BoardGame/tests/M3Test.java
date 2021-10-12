import app.Board;
import app.Player;
import javafx.scene.shape.Circle;
import org.junit.Before;
import org.junit.Test;
import tiles.GreenTile;
import tiles.RedTile;
import tiles.Tile;
import util.Position;

import java.util.ArrayList;
import java.util.List;

import static app.Board.Direction;
import static org.junit.Assert.assertEquals;

public class M3Test {

    private Board board;
    private Player player;

    @Before
    public void init() {
        player = new Player("p1", new Circle());
        List<Player> players = new ArrayList<>();
        players.add(player);
        this.board = new Board(players);
    }

    @Test
    public void checkMoveUp() {
        assertEquals(new Position(0, 0), player.getPosition());
        board.move(player, 4, Direction.UP);
        assertEquals(new Position(4, 0), player.getPosition());
    }
    
    @Test
    public void checkMoveDown() {
        assertEquals(new Position(0, 0), player.getPosition());
        board.move(player, 9, Direction.DOWN);
        assertEquals(new Position(1, 0), player.getPosition());
    }
    
    @Test
    public void checkMoveLeft() {
        assertEquals(new Position(0, 0), player.getPosition());
        board.move(player, 4, Direction.LEFT);
        assertEquals(new Position(0, 4), player.getPosition());
    }
    
    @Test
    public void checkMoveRight() {
        assertEquals(new Position(0, 0), player.getPosition());
        board.move(player, 9, Direction.RIGHT);
        assertEquals(new Position(0, 1), player.getPosition());
    }
    
    @Test
    public void checkGreenTile() {
        assertEquals(0, player.getMoney());

        Tile[][] tiles = board.getTiles();
        tiles[0][2] = new GreenTile(0, 2);
        board.move(player, 2, Direction.RIGHT);

        assertEquals(10, player.getMoney());
    }

    @Test
    public void checkRedTile() {
        assertEquals(0, player.getMoney());

        Tile[][] tiles = board.getTiles();
        tiles[0][2] = new RedTile(0, 2);
        board.move(player, 2, Direction.RIGHT);

        assertEquals(-10, player.getMoney());
    }

}
