package app;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.StringConverter;
import tiles.Tile;

import java.util.List;
import java.util.function.Consumer;

public class BoardUI {

    private final static double TILE_WIDTH = (double) App.BASE_WIDTH / Board.WIDTH;
    private final static double TILE_HEIGHT = (double) App.BASE_HEIGHT / Board.HEIGHT;

    private final App app;
    private final Board board;

    private Node ui;
    private Pane boardUI;
    private Pane sideUI;

    private Timeline openAnimation;

    public BoardUI(App app, Board board) {
        this.app = app;
        this.board = board;
        this.ui = initBoard();
    }

    public void playOpeningAnimation() {
        openAnimation.playFromStart();
    }
    public Node getRoot() {
        return ui;
    }

    public void updatePlayerDisplayPosition(Player player) {
        player.getInGameObject().setTranslateX(player.getPosition().getJ() * TILE_WIDTH + TILE_WIDTH / 2);
        player.getInGameObject().setTranslateY(player.getPosition().getI() * TILE_HEIGHT + TILE_HEIGHT / 2);
    }
    public void updateAllPlayerDisplayPositions() {
        for (Player p : board.getPlayers()) {
            updatePlayerDisplayPosition(p);
        }
    }

    private Node initBoard() {
        boardUI = new StackPane();
        sideUI = initSidePanel();

        Pane tileDisplay = initTileDisplay();
        Pane playerTokens = initPlayerTokens();
        boardUI.getChildren().add(0, tileDisplay);
        boardUI.getChildren().add(1, playerTokens);

        HBox gameUIContainer = new HBox(boardUI, sideUI); // split the board from the buttons
        gameUIContainer.setSpacing(20);

        return gameUIContainer;
    }
    private Pane initPlayerTokens() {
        Pane tokenContainer = new Pane();
        tokenContainer.setPrefSize(App.BASE_WIDTH, App.BASE_HEIGHT);

        for (Player p : board.getPlayers()) {
            tokenContainer.getChildren().add(p.getInGameObject());
        }
        updateAllPlayerDisplayPositions();

        return tokenContainer;
    }
    private Pane initTileDisplay() {
        openAnimation = new Timeline();
        Pane boardDisplay = new Pane(); // the tile board

        Tile[][] tiles = this.board.getTiles();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Rectangle r = new Rectangle(j * TILE_WIDTH, i * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
                r.setFill(tiles[i][j].getColor());
                r.setOpacity(0);
                r.setStroke(Color.BLACK);

                boardDisplay.getChildren().add(r);
                openAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(0.25 * (i + j) + 0.1),
                        new KeyValue(r.opacityProperty(), 1.0)));
            }
        }

        return boardDisplay;
    }
    private Pane initSidePanel() {
        Node dieRoller = initDieRoller();
        Node playerDisplay = initPlayerInfoDisplay();
        VBox sideUI = new VBox(dieRoller, playerDisplay);
        sideUI.setPadding(new Insets(20));
        sideUI.setSpacing(20);

        return sideUI;
    }
    private Pane initDieRoller() {
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
                Board.Direction d = Board.Direction.values()[(int) (Math.random() * 4)];
                dir.setText(d.name().substring(0, 1));
                moveNum.setText("" + dist);
                if (doMove) {
                    this.board.move(this.board.getCurrentPlayer(), dist, d);
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

        return movementGroup;
    }
    private Pane initPlayerInfoDisplay() {
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

        return playerData;
    }

}
