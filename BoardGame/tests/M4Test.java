import app.Board;
import app.Player;
import javafx.scene.shape.Circle;
import org.junit.Before;
import org.junit.Test;
import tiles.GreenTile;
import tiles.PaywallTile;
import tiles.RedTile;
import tiles.Tile;
import util.Position;

import java.util.ArrayList;
import java.util.List;

import static app.Board.Direction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class M4Test {
    // - moving backwards from not accepting paywall

    private Board board;
    private Player player1;
    private Player player2;
    private PaywallTile paywall;
    int fee;

    @Before
    public void init() {
        player1 = new Player("p1", new Circle());
        player2 = new Player("p2", new Circle());
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        this.board = new Board(players);
        this.paywall = new PaywallTile();
        this.fee = paywall.getFee();
    }

    @Test
    public void notPayFee() {
        player2.changeNumCookies(5);
        assertEquals(500, player2.getMoney());
        assertEquals(5, player2.getNumCookies());
        PaywallTile.action(false, player2, paywall, board);
        assertEquals(500, player2.getMoney());
        assertEquals(4, player2.getNumCookies());
    }

    @Test
    public void paywallClosed() {
        PaywallTile paywall2 = new PaywallTile();
        assertEquals(500, player2.getMoney());
        PaywallTile.action(false, player1, paywall2, board);
        assertEquals(500, player2.getMoney());
    }
    
    @Test
    public void payFee() {
        PaywallTile paywall3 = new PaywallTile();
        assertEquals(500, player1.getMoney());
        PaywallTile.action(true, player1, paywall3, board);
        assertEquals(500 - paywall3.getFee(), player1.getMoney());
        assertTrue(paywall3.getPaidFor());
    }

    @Test
    public void cannotPayFee() {
        player1.changeMoney(-player1.getMoney());
        assertEquals(0, player1.getMoney());
        assertEquals(0, player1.getNumCookies());
        PaywallTile.action(false, player1, paywall, board);
        assertEquals(0, player1.getMoney());
        assertEquals(0, player1.getNumCookies());
    }

    @Test
    public void paywallOpen() {
        PaywallTile paywall = new PaywallTile();
        PaywallTile.action(true, player1, paywall, board);
        int moneyBefore = player1.getMoney();
        assertTrue(paywall.getPaidFor());
        paywall.onEnter(player1, board);
        assertEquals(moneyBefore + PaywallTile.POST_PAYMENT_MONEY_GAIN, player1.getMoney());
    }

    @Test
    public void triesToPay() {
        PaywallTile paywall = new PaywallTile();
        player1.changeMoney(-player1.getMoney());
        assertEquals(0, player1.getMoney());
        paywall.onEnter(player1, board);
        assertFalse(paywall.getPaidFor());
    }

}
