package tiles;

import app.Board;
import app.Player;
import app.Board.Direction;
import javafx.scene.paint.Color;

public class ChanceTile extends Tile {

    public ChanceTile() {
        super(Color.BLUE);
    }

    @Override
    public void onEnter(Player player, Board board) {
        double effect = Math.random();
        System.out.println("Effect: " + effect);
        if (effect < 0.2) {
            // Lose money : 0-0.2
            int delta = Math.min(10, player.getMoney());
            player.changeMoney(-delta);
            if (board.getUI() != null) {
                board.getUI().showMessage(player.getName() + " lost $" + delta + " :(");
            }
            System.out.println("Deducted money");
        } else if (effect < 0.4) {
            // Gain money : 0.2-0.4
            player.changeMoney(10);
            if (board.getUI() != null) {
                board.getUI().showMessage(player.getName() + " gained $" + 10 + " :)");
            }
            System.out.println("Added money");
        } else if (effect < 0.6 && player.getNumCookies() > 0) {
            // Lose a cookie : 0.4-0.6
            player.changeNumCookies(-1);
            if (board.getUI() != null) {
                board.getUI().showMessage(player.getName() + " lost a cookie :(");
            }
            System.out.println("Deducted cookie");
        } else if (effect < 0.8) {
            // Gain a cookie : 0.6-0.8
            player.changeNumCookies(1);
            if (board.getUI() != null) {
                board.getUI().showMessage(player.getName() + " got a cookie! :)");
            }
            System.out.println("Added cookie");
        } else {
            // Change position on the board : 0.8-1.0
            board.move(player, 1, Direction.DOWN);
            if (board.getUI() != null) {
                board.getUI().showMessage(player.getName() + " was moved!");
            }
            System.out.println("Moved player!");
        }
    }
}
