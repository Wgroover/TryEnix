package app;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.shape.Circle;
import util.Position;

public class Player {
    private String name;
    private int money;
    private Circle inGameObject;
    private Position pos;
    private StringProperty formattedMoney;

    public Player(String name, Circle inGameObject) {
        this(name, 0, inGameObject);
    }

    public Player(String name, int money, Circle inGameObject) {
        this.name = name;
        this.money = money;
        this.inGameObject = inGameObject;
        this.pos = new Position();
        this.formattedMoney = new SimpleStringProperty("Money: $" + money);
    }
    
    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
        formattedMoney.setValue("Money: $" + money);
    }
    public void changeMoney(int delta) {
        this.money += delta;
        formattedMoney.setValue("Money: $" + money);
    }

    public Circle getInGameObject() {
        return inGameObject;
    }

    public Position getPosition() {
        return pos;
    }

    public StringProperty getFormattedMoneyProperty() {
        return formattedMoney;
    }
}
