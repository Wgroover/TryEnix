import javafx.scene.shape.Circle;

public class Player {
    private String name;
    private int money;
    private Circle inGameObject;

    public Player(String name, Circle inGameObject) {
        this.name = name;
        this.money = 1000;
        this.inGameObject = inGameObject;
    }

    public Player(String name, int money, Circle inGameObject) {
        this.name = name;
        this.money = money;
        this.inGameObject = inGameObject;
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
