package app;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.shape.Circle;
import util.Position;

public class Player {
    private String name;
    private IntegerProperty money;
    private Circle inGameObject;
    private Position pos;

    public Player(String name, Circle inGameObject) {
        this(name, 0, inGameObject);
    }

    public Player(String name, int money, Circle inGameObject) {
        this.name = name;
        this.money = new SimpleIntegerProperty(money);
        this.inGameObject = inGameObject;
        this.pos = new Position();
    }
    
    public String getName() {
        return name;
    }

    public int getMoney() {
        return money.get();
    }
    public void setMoney(int money) {
        this.money.set(money);
    }
    public void changeMoney(int delta) {
        this.money.set(this.money.get() + delta);
    }

    public Circle getInGameObject() {
        return inGameObject;
    }

    public Position getPosition() {
        return pos;
    }

    public IntegerProperty getMoneyProperty() {
        return money;
    }
}
