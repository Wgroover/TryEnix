import javafx.scene.shape.Circle;

public class Player {
    private String name;
    private int money;
    private Circle inGameObject;

    public Player(String name) {
        this.name = name;
    }

    public Player(String name, int money) {
        this.name = name;
        this.money = money;
    }
    
    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public Circle getInGameObject() {
        return inGameObject;
    }
}
