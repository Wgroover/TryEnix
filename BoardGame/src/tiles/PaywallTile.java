package tiles;

import app.Board;
import app.Player;
import javafx.scene.paint.Color;

public class PaywallTile extends Tile {

    public static final int POST_PAYMENT_MONEY_GAIN = 25;

    private int fee;
    private boolean paidFor;

    public PaywallTile() {
        super(Color.GRAY.darker());
        this.fee = (int) (Math.random() * 40) + 10;
        this.paidFor = false;
    }

    @Override
    public void onEnter(Player player, Board board) {
        if (!paidFor) {
            board.promptPaywall(player, this);
        } else {
            player.changeMoney(POST_PAYMENT_MONEY_GAIN);
        }
    }

    public int getFee() {
        return fee;
    }
    public void open() {
        paidFor = true;
        super.getColorProperty().set(Color.GREEN.darker());
    }
}
