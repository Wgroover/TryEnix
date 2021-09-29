import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class App extends GameApplication {

    private final int BASE_WIDTH = 500;
    private final int BASE_HEIGHT = 500;
    private final int UI_WIDTH = 200;
    private final int NUM_TILES_WIDTH = 5;
    private final int NUM_TILES_HEIGHT = 5;
    private int[][] tiles = new int[NUM_TILES_HEIGHT * NUM_TILES_WIDTH][2];
    private Circle player = new Circle(25);
    private int position = 0;


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("App");
        settings.setWidth(BASE_WIDTH + UI_WIDTH);
        settings.setHeight(BASE_HEIGHT);
        settings.setVersion("1.0");
    }

    @Override
    protected void initUI() {
        Pane board = new Pane(); // the tile board
        HBox layout = new HBox(); // split the board from the buttons
        layout.setSpacing(20);

        Timeline timeline = new Timeline();

        for (int i = 1; i < NUM_TILES_WIDTH; i++) {
            double startX = i * (BASE_WIDTH / NUM_TILES_WIDTH);
            Line line = new Line(startX, 0, startX, 0);

            board.getChildren().add(line);

            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.5), 
                new KeyValue(line.endYProperty(), BASE_HEIGHT)));
        }

        for (int i = 1; i < NUM_TILES_HEIGHT; i++) {
            double startY = i * (BASE_HEIGHT / NUM_TILES_HEIGHT);
            Line line = new Line(0, startY, 0, startY);

            board.getChildren().add(line);

            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.5), 
                new KeyValue(line.endXProperty(), BASE_WIDTH)));
        }

        Button forwardOne = new Button("Move forward one");
        forwardOne.setOnAction(e -> move(1));
        Button forwardThree = new Button("Move forward three");
        forwardThree.setOnAction(e -> move(3));
        
        VBox buttons = new VBox(forwardOne, forwardThree);
        buttons.setSpacing(20);

        layout.getChildren().addAll(board, buttons);

        getGameScene().addUINode(layout);
        getGameScene().setBackgroundColor(Color.GREY);

        getGameScene().addUINode(player);

        timeline.play();
    }

    protected void initGame() {
        int count = 0;
        for (int i = 1; i <= NUM_TILES_HEIGHT; i++) {
            int pos_Y = ((BASE_HEIGHT / NUM_TILES_HEIGHT) * i) - ((BASE_HEIGHT / NUM_TILES_HEIGHT) / 2);
            for (int j = 1; j <= NUM_TILES_WIDTH; j++) {
                int pos_X = ((BASE_WIDTH / NUM_TILES_WIDTH) * j) - ((BASE_WIDTH / NUM_TILES_WIDTH) / 2);
                tiles[count][0] = pos_Y;
                tiles[count][1] = pos_X;
                count++;
            }
        }

        player.setTranslateY(tiles[0][0]);
        player.setTranslateX(tiles[0][1]);
    }

    private void move(int n) {
        if (n < 0) {
            return;
        }

        if (position == tiles.length - 1) {
            return;
        }

        int newpos = position + n;

        if (position + n > tiles.length) {
            newpos = tiles.length - 1;
        }

        player.setTranslateY(tiles[newpos][0]);
        player.setTranslateX(tiles[newpos][1]);

        position = newpos;
    }

    public static void main(String[] args) {
        launch(args);
    }
}