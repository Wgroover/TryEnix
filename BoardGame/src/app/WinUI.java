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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WinUI {

    private VBox root;

    public WinUI(List<Player> players) {
        root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(App.BASE_WIDTH + App.UI_WIDTH, App.BASE_HEIGHT);

        List<Player> sorted = new ArrayList<>(players);
        sorted.sort(Comparator.comparingInt(Player::getNumCookies).reversed());
        Player winner = sorted.get(0);
        Circle winnerCircle = new Circle(50, winner.getInGameObject().getFill());
        winnerCircle.setStroke(Color.BLACK);
        Label congrats = new Label(String.format("Congratulations %s!", winner.getName()));
        congrats.setFont(new Font(48));

        root.getChildren().addAll(winnerCircle, congrats);
        root.getChildren().addAll(createOtherPlayerDisplay(sorted.subList(1, sorted.size())));
        root.getChildren().addAll(createAwardDisplay(players));
    }

    public Node getRoot() {
        return root;
    }

    private List<Node> createAwardDisplay(List<Player> players) {
        List<Node> res = new ArrayList<>();

        Player mostTilesTraversed = Collections.max(players,
                Comparator.comparingInt((Player a) -> a.getStats().getTilesTraversed()));
        Player mostMinigamesWon = Collections.max(players,
                Comparator.comparingInt((Player a) -> a.getStats().getMinigamesWon()));
        Player mostCookiesObtained = Collections.max(players,
                Comparator.comparingInt((Player a) -> a.getStats().getCookiesObtained()));

        Label tilesLabel = new Label(String.format("%s travelled the most tiles (%d)!",
                mostTilesTraversed.getName(), mostTilesTraversed.getStats().getTilesTraversed()));
        tilesLabel.setFont(new Font(24));
        Label minigamesLabel = new Label(String.format("%s won the most minigames (%d)!",
                mostMinigamesWon.getName(), mostMinigamesWon.getStats().getMinigamesWon()));
        minigamesLabel.setFont(new Font(24));
        Label cookiesLabel = new Label(String.format("%s obtained the most cookies (%d)!",
                mostCookiesObtained.getName(), mostCookiesObtained.getStats().getCookiesObtained()));
        cookiesLabel.setFont(new Font(24));

        Circle tilesCircle = new Circle(20, mostTilesTraversed.getInGameObject().getFill());
        tilesCircle.setStroke(Color.BLACK);
        Circle minigamesCircle = new Circle(20, mostMinigamesWon.getInGameObject().getFill());
        minigamesCircle.setStroke(Color.BLACK);
        Circle cookiesCircle = new Circle(20, mostCookiesObtained.getInGameObject().getFill());
        cookiesCircle.setStroke(Color.BLACK);

        res.add(new HBox(tilesCircle, tilesLabel));
        res.add(new HBox(minigamesCircle, minigamesLabel));
        res.add(new HBox(cookiesCircle, cookiesLabel));

        res.forEach(n -> ((HBox) n).setAlignment(Pos.CENTER));

        return res;
    }

    private List<Node> createOtherPlayerDisplay(List<Player> otherPlayers) {
        List<Node> res = new ArrayList<>(otherPlayers.size());

        for (Player player : otherPlayers) {
            Circle playerCircle = new Circle(30, player.getInGameObject().getFill());
            playerCircle.setStroke(Color.BLACK);

            Label dispName = new Label(player.getName());
            dispName.setFont(new Font(24));
            Label cookies = new Label(player.getNumCookies() + " cookie" + (player.getNumCookies() == 1 ? "" : "s"));
            cookies.setFont(new Font(24));

            VBox text = new VBox(dispName, cookies);
            text.setSpacing(5);

            HBox section = new HBox(playerCircle, text);
            section.setSpacing(10);
            res.add(section);
        }
        res.forEach(n -> ((HBox) n).setAlignment(Pos.CENTER));

        return res;
    }
}
