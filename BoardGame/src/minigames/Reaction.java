package minigames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.almasb.fxgl.dsl.FXGL;

import app.App;
import app.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class Reaction extends Minigame {
    Pane main;
    List<Node> previousState;
    Map<Player, Color> playerData = new HashMap<>();

    public Reaction(List<Player> players) {
        super(players);
        previousState = new ArrayList<>();
        initReaction();
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

    private void initReaction() {
        main = new VBox();
        main.setPrefSize(App.BASE_WIDTH, App.BASE_HEIGHT);
        
        Pane playerUI = new HBox();

        Label title = new Label("REACTION!");
        title.setFont(new Font(50));
        title.setPadding(new Insets(20, 20, 20, 20));

        Label subtitle = new Label("Press STOP! when the square is green!");
        subtitle.setFont(new Font(30));
        subtitle.setPadding(new Insets(20, 20, 20, 20));

        for (Player p : getPlayers()) {
            playerUI.getChildren().add(createPlayerUI(p));
        }

        Pane contPane = new VBox();
        contPane.setPadding(new Insets(20, 20, 20, 20));
        Button cont = new Button("Continue");

        contPane.getChildren().add(cont);

        cont.setOnAction(e -> {
            if (playerData.size() != getPlayers().size()) {
                FXGL.getDialogService().showMessageBox("All players must attempt the game!");
            } else {
                String message = "";
                int winnings = 100;
                for (Player p : getPlayers()) {
                    if (playerData.get(p).equals(Color.GREEN)) {
                        p.changeMoney(winnings);
                        message += p.getName() + " won $" + winnings + "!\n";
                    } else {
                        int losings = 50;
                        if (p.getMoney() < losings) {
                            losings = p.getMoney();
                        }

                        p.changeMoney(-losings);
                        message += p.getName() + " lost $" + losings + "...\n";
                    }
                }

                FXGL.getDialogService().showMessageBox(message);
                endGame();
            }
        });

        main.getChildren().addAll(title, subtitle, playerUI, contPane);
    }

    private Pane createPlayerUI(Player player) {
        Pane pane = new VBox();
        pane.setPadding(new Insets(20, 20, 20, 20));

        Label name = new Label(player.getName());
        name.setFont(new Font(24));

        Rectangle r = new Rectangle();
        r.setWidth(100);
        r.setHeight(100);
        r.setFill(Color.RED);

        Timeline t = new Timeline();

        Consumer<Color> updateFill = x -> {
            r.setFill(x);
        };

        Consumer<Double> updateTimeline = x -> {
            t.getKeyFrames().clear();
            t.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(x), f -> updateFill.accept(Color.GREEN)),
                new KeyFrame(Duration.seconds(0.22), f -> updateFill.accept(Color.RED))
            );
            t.play();
        };
        
        t.setCycleCount(1);
        t.setOnFinished(f -> updateTimeline.accept((Math.random() * 2.5) + 1.5));
        t.play();

        Button stop = new Button("STOP!");
        stop.setOnAction(e -> {
            stop.setDisable(true);
            t.stop();
            playerData.put(player, (Color) r.getFill());
        });

        pane.getChildren().addAll(name, r, stop);

        return pane;
    }
}
