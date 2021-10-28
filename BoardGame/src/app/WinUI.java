package app;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WinUI {

    private VBox root;

    public WinUI(List<Player> players) {
        root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(App.BASE_WIDTH + App.UI_WIDTH, App.BASE_HEIGHT);

        List<Player> sorted = new ArrayList<>(players);
        sorted.sort(Comparator.comparingInt(Player::getNumCookies));
        Player winner = sorted.get(0);
        Circle winnerCircle = new Circle(50, winner.getInGameObject().getFill());
        winnerCircle.setStroke(Color.BLACK);
        Label congrats = new Label(String.format("Congratulations %s!", winner.getName()));
        congrats.setFont(new Font(48));

        root.getChildren().addAll(winnerCircle, congrats);
        root.getChildren().addAll(createOtherPlayerDisplay(sorted.subList(1, sorted.size())));
    }

    public Node getRoot() {
        return root;
    }

    private List<Node> createOtherPlayerDisplay(List<Player> otherPlayers) {
        List<Node> res = new ArrayList<>(otherPlayers.size());

        for (Player player : otherPlayers) {
            Circle playerCircle = new Circle(30, player.getInGameObject().getFill());
            playerCircle.setStroke(Color.BLACK);

            Label dispName = new Label(player.getName());
            dispName.setFont(new Font(24));
            Label cookies = new Label(player.getNumCookies() + " cookie" + (player.getNumCookies() == 1 ? "s" : ""));
            cookies.setFont(new Font(24));

            VBox text = new VBox(dispName, cookies);
            text.setSpacing(5);

            HBox section = new HBox(playerCircle, text);
            section.setSpacing(10);
            res.add(section);
        }

        return res;
    }
}
