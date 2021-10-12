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

    private final static int BASE_WIDTH = 600;
    private final static int BASE_HEIGHT = 600;
    private final static int UI_WIDTH = 400;
    private final static double TILE_WIDTH = (double) BASE_WIDTH / Board.WIDTH;
    private final static double TILE_HEIGHT = (double) BASE_HEIGHT / Board.HEIGHT;

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

        Timeline timeline = new Timeline();

        Tile[][] tiles = this.board.getTiles();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Rectangle r = new Rectangle(j * TILE_WIDTH, i * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
                r.setFill(tiles[i][j].getColor());
                r.setOpacity(0);
                r.setStroke(Color.BLACK);

                board.getChildren().add(r);
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.25 * (i + j) + 0.1),
                        new KeyValue(r.opacityProperty(), 1.0)));
            }
        }

        Label dir = new Label("R");
        dir.setFont(new Font(24));
        dir.setAlignment(Pos.CENTER);
        dir.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        dir.setPrefSize(50, 50);

        Label moveNum = new Label("1");
        moveNum.setFont(new Font(24));
        moveNum.setAlignment(Pos.CENTER);
        moveNum.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        moveNum.setPrefSize(50, 50);

        Button move = new Button("Roll");
        move.setOnAction(e -> {
            move.setDisable(true);
            Consumer<Boolean> rollDie = doMove -> {
                int dist = (int) (Math.random() * 6) + 1;
                Direction d = Direction.values()[(int) (Math.random() * 4)];
                dir.setText(d.name().substring(0, 1));
                moveNum.setText("" + dist);
                if (doMove) {
                    move(this.board.getCurrentPlayer(), dist, d);
                    move.setDisable(false);
                }
            };
            Timeline t = new Timeline(new KeyFrame(Duration.seconds(0.1), f -> rollDie.accept(false)));

            t.setOnFinished(f -> rollDie.accept(true));

            t.setCycleCount(10);
            t.playFromStart();
        });

        HBox movementGroup = new HBox(move, dir, moveNum);
        movementGroup.setSpacing(20);

        VBox sideUI = new VBox(movementGroup);
        sideUI.setPadding(new Insets(20));
        sideUI.setSpacing(20);

        VBox playerData = new VBox();
        Label moveOrderLabel = new Label("--------MOVE ORDER--------");
        playerData.getChildren().add(moveOrderLabel);

        List<Player> players = this.board.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Label label1 = new Label("---Player " + (i + 1) + "---");
            Label label2 = new Label("Name: " + players.get(i).getName());
            Label label3 = new Label();
            StringProperty playerMoney = new SimpleStringProperty();
            playerMoney.bindBidirectional(players.get(i).getMoneyProperty(), new StringConverter<>() {
                @Override
                public String toString(Number object) {
                    return "Money: $" + object;
                }

                @Override
                public Number fromString(String string) {
                    throw new UnsupportedOperationException("Money string to int");
                }
            });
            label3.textProperty().bind(playerMoney);
            Label label4 = new Label("");

            Circle playerCircle = players.get(i).getInGameObject();
            Circle dispCircle = new Circle(playerCircle.getRadius());
            dispCircle.setFill(playerCircle.getFill());
            dispCircle.setStroke(playerCircle.getStroke());

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

    private void move(Player player, int n, Direction direction) {
        board.move(player, n, direction);

        updatePlayerDisplayPosition(player);
    }

    private void updatePlayerDisplayPosition(Player player) {
        player.getInGameObject().setTranslateX(player.getPosition().getJ() * TILE_WIDTH + TILE_WIDTH / 2);
        player.getInGameObject().setTranslateY(player.getPosition().getI() * TILE_HEIGHT + TILE_HEIGHT / 2);
    }
    private void updateAllPlayerDisplayPositions() {
        for (Player p : board.getPlayers()) {
            updatePlayerDisplayPosition(p);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}