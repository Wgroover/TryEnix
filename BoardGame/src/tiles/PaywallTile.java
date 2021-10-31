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

    public static void action(boolean willPay, Player player, PaywallTile paywall, Board board) {
        if (willPay) {
            player.changeMoney(-paywall.getFee());
            paywall.open();
        } else {
            if (player.getNumCookies() > 0) {
                player.changeNumCookies(-1);
                if (board.getUI() != null) {
                    board.getUI().showMessage(String.format("%s lost a cookie :(", player.getName()));
                }
            }
        }
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
    public boolean getPaidFor() {
        return paidFor;
    }
    public void open() {
        paidFor = true;
        super.getColorProperty().set(Color.GREEN.darker());
    }
}
