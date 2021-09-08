import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

public class App extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(400);
        settings.setHeight(400);
        settings.setTitle("App");
    }

    public static void main(String[] args) {
        launch(args);
    }
}