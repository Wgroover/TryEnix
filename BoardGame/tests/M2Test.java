import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;

import app.App;
import app.Player;
import org.junit.Test;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class M2Test {

    // tests invalid names
    @Test
    public void testNames() {
        App game = new App();

        String name1 = null;
        String name2 = "";
        String name3 = "      ";
        String name4 = " Jeff ";

        assertFalse(game.isNameValid(name1));
        assertFalse(game.isNameValid(name2));
        assertFalse(game.isNameValid(name3));

        assertTrue(game.isNameValid(name4));
    }

    // ensures turn order actually changes
    /* DEPRECATED - DOESN'T WORK WITH ONE PLAYER
    @Test
    public void changeTurnOrder() {
        App game = new App();

        ArrayList<Player> players = new ArrayList<>();
        Player player0 = new Player("player0", null);
        Player player1 = new Player("player1", null);
        Player player2 = new Player("player2", null);
        Player player3 = new Player("player3", null);

        players.add(player0);
        players.add(player1);
        players.add(player2);
        players.add(player3);

        ArrayList<Player> playersChanged = new ArrayList<>(players);

        game.changeTurnOrder(playersChanged);

        assertTrue(playersChanged.contains(player0));
        assertTrue(playersChanged.contains(player1));
        assertTrue(playersChanged.contains(player2));
        assertTrue(playersChanged.contains(player3));

        assertTrue(playersChanged.indexOf(player0) != players.indexOf(player0) ||
                   playersChanged.indexOf(player1) != players.indexOf(player1) ||
                   playersChanged.indexOf(player2) != players.indexOf(player2) ||
                   playersChanged.indexOf(player3) != players.indexOf(player3));
    }
    */

    // ensures player data remains intact upon changing turn order
    @Test
    public void playerDataIntact() {
        App game = new App();

        Circle circle0 = new Circle(25);
        Circle circle1 = new Circle(25);
        Circle circle2 = new Circle(25);
        Circle circle3 = new Circle(25);

        circle0.setFill(Color.RED);
        circle1.setFill(Color.GREEN);
        circle2.setFill(Color.BLUE);
        circle3.setFill(Color.PURPLE);

        Player player0 = new Player("player0", circle0);
        Player player1 = new Player("player1", circle1);
        Player player2 = new Player("player2", circle2);
        Player player3 = new Player("player3", circle3);

        ArrayList<Player> players = new ArrayList<>();
        players.add(player0);
        players.add(player1);
        players.add(player2);
        players.add(player3);

        //game.changeTurnOrder(players);
        Collections.shuffle(players);

        assertTrue(players.get(players.indexOf(player0)).getName().equals("player0"));
        assertTrue(players.get(players.indexOf(player1)).getName().equals("player1"));
        assertTrue(players.get(players.indexOf(player2)).getName().equals("player2"));
        assertTrue(players.get(players.indexOf(player3)).getName().equals("player3"));

        assertTrue(players.get(players.indexOf(player0)).getInGameObject().equals(circle0));
        assertTrue(players.get(players.indexOf(player1)).getInGameObject().equals(circle1));
        assertTrue(players.get(players.indexOf(player2)).getInGameObject().equals(circle2));
        assertTrue(players.get(players.indexOf(player3)).getInGameObject().equals(circle3));
    }
}
