package app;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.StringConverter;
import tiles.ChanceTile;
import tiles.GreenTile;
import tiles.RedTile;
import tiles.Tile;
import util.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static app.Board.Direction;

public class App extends GameApplication {

    public final static int BASE_WIDTH = 600;
    public final static int BASE_HEIGHT = 600;
    private final static int UI_WIDTH = 400;

    private Board board;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Board Game");
        settings.setWidth(BASE_WIDTH + UI_WIDTH);
        settings.setHeight(BASE_HEIGHT);
        settings.setVersion("1.0");
        settings.setMainMenuEnabled(true);
    }

    @Override
    protected void initUI() {
        VBox layout = new VBox();
        layout.setPadding(new Insets(5, 5, 5, 5));
        layout.setSpacing(10);

        Label label = new Label("Choose the desired number of players:");
        Spinner<Integer> playerCount = new Spinner<>(1, 4, 1);
        Button next = new Button("Next");

        layout.getChildren().addAll(label, playerCount, next);
        getGameScene().addUINode(layout);

        next.setOnAction(e -> {
            getGameScene().clearUINodes();
            initPlayerConfig(playerCount.getValue());
        });
    }

    protected void initPlayerConfig(int numPlayers) {
        VBox layout = new VBox();
        layout.setPadding(new Insets(5, 5, 5, 5));

        class PlayerData {
            public TextField name;
            public ColorPicker color;
        }

        ArrayList<PlayerData> list = new ArrayList<>();

        for (int i = 0; i < numPlayers; i++) {
            Label label1 = new Label("---Player " + (i + 1) + "---");
            Label label2 = new Label("Name:");
            Label label3 = new Label("Color:");
            Label label4 = new Label("");

            TextField name = new TextField();
            ColorPicker color = new ColorPicker();
            color.setValue(new Color(Math.random(), Math.random(), Math.random(), 1));

            layout.getChildren().addAll(label1, label2, name, label3, color, label4);
            PlayerData data = new PlayerData();
            data.name = name;
            data.color = color;
            list.add(data);
        }

        Button next = new Button("Start Game");
        next.setOnAction(e -> {
            boolean passed = true;
            for (int i = 0; i < numPlayers; i++) {
                if (!isNameValid(list.get(i).name.getText())) {
                    Alert error = new Alert(AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Every player must have a valid name.");
                    error.setContentText("Player " + (i + 1) + "'s name caused this error.");
                    error.showAndWait();
                    passed = false;
                    break;
                }
            }

            if (passed) {
                List<Player> players = new ArrayList<>();
                for (int i = 0; i < numPlayers; i++) {
                    Circle circle = new Circle(25);
                    circle.setStroke(Color.BLACK);
                    circle.setFill(list.get(i).color.getValue());
                    players.add(new Player(list.get(i).name.getText(), circle));
                }

                getGameScene().clearUINodes();
                Collections.shuffle(players);
                this.board = new Board(players);
                initBoard();
            }
        });

        layout.getChildren().add(next);
        getGameScene().addUINode(layout);
    }

    public boolean isNameValid(String name) {
        return name != null && !name.trim().equals("");
    }

    protected void initBoard() {
        BoardUI ui = new BoardUI(this, this.board);
        getGameScene().addUINode(ui.getRoot());
        getGameScene().setBackgroundColor(Color.GREY);

        ui.playOpeningAnimation();
    }

    public static void main(String[] args) {
        launch(args);
    }
}