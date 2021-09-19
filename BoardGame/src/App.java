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
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class App extends GameApplication {

    private final int BASE_WIDTH = 500;
    private final int BASE_HEIGHT = 500;
    private final int UI_WIDTH = 200;
    private final int NUM_TILES_WIDTH = 5;
    private final int NUM_TILES_HEIGHT = 5;
    private final int PADDING = 10;

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
        Button forwardThree = new Button("Move forward three");
        
        VBox buttons = new VBox(forwardOne, forwardThree);
        buttons.setSpacing(20);

        layout.getChildren().addAll(board, buttons);

        getGameScene().addUINode(layout);
        getGameScene().setBackgroundColor(Color.GREY);

        timeline.play();
    }

    protected void initGame() {

    }

    protected void initInput() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}