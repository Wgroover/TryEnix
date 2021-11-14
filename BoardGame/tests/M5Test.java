import app.Board;
import app.Player;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import minigames.Reaction;
import minigames.RockPaperScissors;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class M5Test {

    private Player p1, p2;
    private RockPaperScissors rps;
    private Reaction reaction;

    @Before
    public void init() {
        p1 = new Player("p1", new Circle());
        p2 = new Player("p2", new Circle());
        rps = new RockPaperScissors(p1, p2);
        reaction = new Reaction(p1, p2);
    }

    @Test
    public void rockBeatsScissors() {
        setRPSPlayerData(p1, 0);
        setRPSPlayerData(p2, 2);
        assertEquals(p1, getRPSWinner());
        setRPSPlayerData(p1, 2);
        setRPSPlayerData(p2, 0);
        assertEquals(p2, getRPSWinner());
    }
    @Test
    public void scissorsBeatsPaper() {
        setRPSPlayerData(p1, 2);
        setRPSPlayerData(p2, 1);
        assertEquals(p1, getRPSWinner());
        setRPSPlayerData(p1, 1);
        setRPSPlayerData(p2, 2);
        assertEquals(p2, getRPSWinner());
    }
    @Test
    public void paperBeatsRock() {
        setRPSPlayerData(p1, 1);
        setRPSPlayerData(p2, 0);
        assertEquals(p1, getRPSWinner());
        setRPSPlayerData(p1, 0);
        setRPSPlayerData(p2, 1);
        assertEquals(p2, getRPSWinner());
    }
    @Test
    public void nothingBeatsItself() {
        setRPSPlayerData(p1, 0);
        setRPSPlayerData(p2, 0);
        assertNull(getRPSWinner());
        setRPSPlayerData(p1, 1);
        setRPSPlayerData(p2, 1);
        assertNull(getRPSWinner());
        setRPSPlayerData(p1, 2);
        setRPSPlayerData(p2, 2);
        assertNull(getRPSWinner());
    }

    @Test
    public void reactionWin() {
        int before = p1.getMoney();
        setReactionPlayerData(p1, Color.GREEN);
        dealMoney(p1);
        int after = p1.getMoney();
        assertEquals(before + Reaction.WINNINGS, after);
    }

    @Test
    public void reactionLostRich() {
        int before = p1.getMoney();
        setReactionPlayerData(p1, Color.RED);
        dealMoney(p1);
        int after = p1.getMoney();
        assertEquals(before - Reaction.LOSINGS, after);
    }

    @Test
    public void reactionLostPoor() {
        int before = 15;
        p1.setMoney(before);
        setReactionPlayerData(p1, Color.RED);
        dealMoney(p1);
        int after = p1.getMoney();
        assertEquals(0, after);
    }

    @Test
    public void reactionLostZero() {
        int before = 0;
        p1.setMoney(before);
        setReactionPlayerData(p1, Color.RED);
        dealMoney(p1);
        int after = p1.getMoney();
        assertEquals(0, after);
    }

    private void dealMoney(Player p) {
        try {
            Method m = Reaction.class.getDeclaredMethod("dealMoney", Player.class);
            m.setAccessible(true);
            m.invoke(reaction, p);
        } catch (Exception e) {
            System.out.println("Reflection error: " + e.getMessage());
        }
    }
    private void setReactionPlayerData(Player p, Color c) {
        try {
            Field field = Reaction.class.getDeclaredField("playerData");
            field.setAccessible(true);
            ((Map<Player, Color>) field.get(reaction)).put(p, c);
        } catch (Exception e) {
            System.out.println("Reflection error: " + e.getMessage());
        }
    }
    private void setRPSPlayerData(Player p, int i) {
        try {
            Field field = RockPaperScissors.class.getDeclaredField("playerData");
            field.setAccessible(true);
            ((Map<Player, Integer>) field.get(rps)).put(p, i);
        } catch (Exception e) {
            System.out.println("Reflection error: " + e.getMessage());
        }
    }
    private Player getRPSWinner() {
        try {
            Method getWinner = RockPaperScissors.class.getDeclaredMethod("getWinner");
            getWinner.setAccessible(true);
            return (Player) getWinner.invoke(rps);
        } catch (Exception e) {
            System.out.println("Reflection error: " + e.getMessage());
        }
        return null;
    }
}
