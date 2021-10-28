package tiles;

import app.Board;
import app.Player;
import javafx.scene.paint.Color;

public class CookieTile extends Tile {

    public static final int START_COOKIE_COST = 100;
    public static final int COOKIE_COST_DELTA = 25;

    private int cookieCost;

    public CookieTile() {
        super(Color.YELLOW);
        cookieCost = START_COOKIE_COST;
    }

    public static void action(boolean willPay, Player player, CookieTile cookieTile, Board board) {
        if (willPay) {
            cookieTile.payForCookie(player);
            if (player.getNumCookies() >= Player.COOKIES_REQUIRED) {
                board.win();
            }
        }
    }

    public void payForCookie(Player player) {
        if (player.getMoney() < cookieCost) {
            return;
        }
        player.changeNumCookies(1);
        player.changeMoney(-cookieCost);
        cookieCost += COOKIE_COST_DELTA;
    }
    public int getCookieCost() {
        return cookieCost;
    }

    @Override
    public void onEnter(Player player, Board board) {
        board.promptBuyCookie(player, this);
    }
}
