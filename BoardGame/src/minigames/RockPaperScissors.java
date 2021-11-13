package minigames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.almasb.fxgl.dsl.FXGL;

import app.App;
import app.Player;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class RockPaperScissors extends Minigame {
    Pane main;
    List<Node> previousState;
    Map<Player, Integer> playerData = new HashMap<>();

    public RockPaperScissors(List<Player> players) {
        super(players);
        previousState = new ArrayList<>();
        initRPS();
    }

    @Override
    public void startGame() {
        Platform.runLater(() -> {
            for (Node i : FXGL.getGameScene().getUINodes()) {
                previousState.add(i);
            }
            FXGL.getGameScene().clearUINodes();
            FXGL.getGameScene().addUINode(main);
        });
    }

    @Override
    public void endGame() {
        Platform.runLater(() -> {
            FXGL.getGameScene().clearUINodes();
            for (Node i : previousState) {
                FXGL.getGameScene().addUINode(i);
            }
        });
    }

    private void initRPS() {
        main = new VBox();
        main.setPrefSize(App.BASE_WIDTH, App.BASE_HEIGHT);

        Label title = new Label("ROCK PAPER SCISSORS!");
        title.setFont(new Font(50));
        title.setPadding(new Insets(20, 20, 20, 20));

        Pane playerUI = new HBox();

        Pane player1 = createPlayerUI(getPlayers().get(0));
        Pane player2 = createPlayerUI(getPlayers().get(1));

        playerUI.getChildren().addAll(player1, player2);

        Button go = new Button("GO!");
        go.setPadding(new Insets(20, 20, 20, 20));

        go.setOnAction(e -> {
            if (playerData.size() != 2) {
                FXGL.getDialogService().showMessageBox("Both players must lock in first!");
            } else {
                Player winner = getWinner();
                if (winner == null) {
                    FXGL.getDialogService().showMessageBox("It was a tie...");
                } else {
                    Player loser = getPlayers().get((getPlayers().indexOf(winner) + 1) % 2);
                    int delta = 100;
                    if (loser.getMoney() < delta) {
                        delta = loser.getMoney();
                    }
                    winner.changeMoney(delta);
                    loser.changeMoney(-delta);

                    FXGL.getDialogService().showMessageBox(winner.getName() + " defeated " + loser.getName() + "!\n"
                        + winner.getName() + " stole $" + delta + " from " + loser.getName() + ".");
                }

                endGame();
            }
        });

        main.getChildren().addAll(title, playerUI, go);
    }

    private Pane createPlayerUI(Player player) {
        Pane pane = new VBox();
        pane.setPadding(new Insets(20, 20, 20, 20));

        Label name = new Label(player.getName());
        name.setFont(new Font(24));

        ComboBox<String> box = new ComboBox<>();
        box.getItems().addAll("Rock", "Paper", "Scissors");
        box.getSelectionModel().selectFirst();

        Button lock = new Button("Lock in!");

        pane.getChildren().addAll(name, box, lock);

        lock.setOnAction(e -> {
            name.setText(player.getName() + " locked in!");
            playerData.put(player, box.getSelectionModel().getSelectedIndex());
            pane.getChildren().removeAll(box, lock);
        });

        return pane;
    }

    private Player getWinner() {
        Player player1 = getPlayers().get(0);
        Player player2 = getPlayers().get(1);
        int p1 = playerData.get(player1);
        int p2 = playerData.get(player2);

        if (p1 == p2) {
            return null;
        }

        int result = (p1 * 2) + (p2 * 3);

        if (result == (0 * 2) + (2 * 3) || result == (1 * 2) + (0 * 3) || result == (2 * 2) + (1 * 3)) {
            return player1;
        } else {
            return player2;
        }
    }
}
