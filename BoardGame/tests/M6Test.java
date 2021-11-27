import app.Board;
import app.Player;
import javafx.scene.shape.Circle;
import org.junit.Before;
import org.junit.Test;
import tiles.ChanceTile;
import tiles.GreenTile;
import util.Position;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class M6Test {

    private Board board;
    private Player p1, p2;

    @Before
    public void setup() {
        p1 = new Player("p1", new Circle(2));
        p2 = new Player("p2", new Circle(2));
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        board = new Board(players);
    }

    @Test
    public void statTilesDown() {
        board.getTiles()[4][0] = new GreenTile();
        board.move(p1, 4, Board.Direction.DOWN);
        assertEquals(4, p1.getStats().getTilesTraversed());
    }
    @Test
    public void statTilesUp() {
        board.getTiles()[7][0] = new GreenTile();
        board.move(p1, 1, Board.Direction.UP);
        assertEquals(1, p1.getStats().getTilesTraversed());
    }
    @Test
    public void statTilesRight() {
        board.getTiles()[0][4] = new GreenTile();
        board.move(p1, 4, Board.Direction.RIGHT);
        assertEquals(4, p1.getStats().getTilesTraversed());
    }
    @Test
    public void statTilesLeft() {
        board.getTiles()[0][7] = new GreenTile();
        board.move(p1, 1, Board.Direction.LEFT);
        assertEquals(1, p1.getStats().getTilesTraversed());
    }

    @Test
    public void statCookies() {
        p1.changeNumCookies(4);
        p1.changeNumCookies(-2);
        assertEquals(4, p1.getStats().getCookiesObtained());
    }

    @Test
    public void chanceTile() {
        board.getTiles()[1][0] = new GreenTile();
        board.getTiles()[0][0] = new ChanceTile();
        p1.changeNumCookies(2);
        int cookies = p1.getNumCookies();
        int money = p1.getMoney();
        Position p = p1.getPosition();
        board.getTiles()[0][0].onEnter(p1, board);
        if (p1.getNumCookies() == cookies && p1.getMoney() == money && p.equals(p1.getPosition())) {
            fail();
        }
    }

}
