package app;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import tiles.GreenTile;
import tiles.Tile;
import util.Position;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App extends GameApplication {

    private final int BASE_WIDTH = 600;
    private final int BASE_HEIGHT = 600;
    private final int UI_WIDTH = 400;
    private final int NUM_TILES_WIDTH = 5;
    private final int NUM_TILES_HEIGHT = 5;
    private final double TILE_WIDTH = (double) BASE_WIDTH / NUM_TILES_WIDTH;
    private final double TILE_HEIGHT = (double) BASE_HEIGHT / NUM_TILES_HEIGHT;
    private Tile[][] tiles = new Tile[NUM_TILES_HEIGHT][NUM_TILES_WIDTH];

    private int numPlayers;
    private ArrayList<Player> players;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Board Game");
        settings.setWidth(BASE_WIDTH + UI_WIDTH);
        settings.setHeight(BASE_HEIGHT);
        settings.setVersion("1.0");
        settings.setMainMenuEnabled(true);

        players = new ArrayList<>();
    }

    @Override
    protected void initUI() {
        VBox layout = new VBox();
        layout.setPadding(new Insets(5, 5, 5, 5));
        layout.setSpacing(10);

        Label label = new Label("Choose the desired number of players:");
        Spinner<Integer> playerCount = new Spinner<Integer>(2, 4, 2);
        Button next = new Button("Next");

        layout.getChildren().addAll(label, playerCount, next);
        getGameScene().addUINode(layout);

        next.setOnAction(e -> {
            numPlayers = playerCount.getValue();
            getGameScene().clearUINodes();
            initPlayerConfig();
        });
    }

    protected void initPlayerConfig() {
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
                for (int i = 0; i < numPlayers; i++) {
                    Circle circle = new Circle(25);
                    circle.setFill(list.get(i).color.getValue());
                    players.add(new Player(list.get(i).name.getText(), circle));
                }

                getGameScene().clearUINodes();
                initBoard();
            }
        });

        layout.getChildren().add(next);
        getGameScene().addUINode(layout);
    }

    public boolean isNameValid(String name) {
        if (name == null) {
            return false;
        }
        
        if (name.trim().equals("")) {
            return false;
        }

        return true;
    }

    protected void initBoard() {
        Pane board = new Pane(); // the tile board
        HBox layout = new HBox(); // split the board from the buttons
        layout.setSpacing(20);

        changeTurnOrder(players);

        Timeline timeline = new Timeline();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = new GreenTile(i, j);

                Rectangle r = new Rectangle(j * TILE_WIDTH, i * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
                r.setFill(tiles[i][j].getColor());
                r.setOpacity(0);
                r.setStroke(Color.BLACK);

                board.getChildren().add(r);
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.25 * (i + j) + 0.1),
                        new KeyValue(r.opacityProperty(), 1.0)));
            }
        }

        Button forwardOne = new Button("Move forward one");
        forwardOne.setOnAction(e -> move(players.get(0), 1, Direction.RIGHT));
        Button forwardThree = new Button("Move forward three");
        forwardThree.setOnAction(e -> move(players.get(0), 1, Direction.DOWN));

        VBox sideUI = new VBox(forwardOne, forwardThree);
        sideUI.setSpacing(20);

        VBox playerData = new VBox();
        Label moveOrderLabel = new Label("--------MOVE ORDER--------");
        playerData.getChildren().add(moveOrderLabel);
        for (int i = 0; i < numPlayers; i++) {
            Label label1 = new Label("---Player " + (i + 1) + "---");
            Label label2 = new Label("Name: " + players.get(i).getName());
            Label label3 = new Label();
            label3.textProperty().bind(players.get(i).getFormattedMoneyProperty());
            Label label4 = new Label("");

            Circle playerCircle = players.get(i).getInGameObject();
            Circle dispCircle = new Circle(playerCircle.getRadius());
            dispCircle.setFill(playerCircle.getFill());

            playerData.getChildren().addAll(label1, dispCircle, label2, label3, label4);
        }

        sideUI.getChildren().add(playerData);

        layout.getChildren().addAll(board, sideUI);

        getGameScene().addUINode(layout);
        getGameScene().setBackgroundColor(Color.GREY);

        for (Player p : players) {
            getGameScene().addUINode(p.getInGameObject());
        }

        updateAllPlayerDisplayPositions();

        timeline.play();
    }

    public void changeTurnOrder(ArrayList<Player> players) {
        ArrayList<Player> original = new ArrayList<>(players);
        Collections.shuffle(players);
        while(true) {
            for (int i = 0; i < original.size(); i++) {
                if (original.get(i) != players.get(i)) {
                    return;
                }
            }

            Collections.shuffle(players);
        }
    }

    @Override
    protected void initGame() {
    }

    private void move(Player player, int n, Direction direction) {
        if (n < 0) {
            return;
        }

        Position p = player.getPosition();
        if (direction == Direction.UP) {
            p.setI((p.getI() + NUM_TILES_HEIGHT - n % NUM_TILES_HEIGHT) % NUM_TILES_HEIGHT);
        } else if (direction == Direction.DOWN) {
            p.setI((p.getI() + n) % NUM_TILES_HEIGHT);
        } else if (direction == Direction.LEFT) {
            p.setJ((p.getJ() + NUM_TILES_WIDTH - n % NUM_TILES_WIDTH) % NUM_TILES_WIDTH);
        } else {
            p.setJ((p.getJ() + n) % NUM_TILES_WIDTH);
        }

        tiles[p.getI()][p.getJ()].onEnter(player);

        updatePlayerDisplayPosition(player);
    }

    private void updatePlayerDisplayPosition(Player player) {
        player.getInGameObject().setTranslateX(player.getPosition().getJ() * TILE_WIDTH + TILE_WIDTH / 2);
        player.getInGameObject().setTranslateY(player.getPosition().getI() * TILE_HEIGHT + TILE_HEIGHT / 2);
    }
    private void updateAllPlayerDisplayPositions() {
        for (Player p : players) {
            updatePlayerDisplayPosition(p);
        }
    }

    private enum Direction {
        LEFT, RIGHT, UP, DOWN
    }

    public static void main(String[] args) {
        launch(args);
    }
}