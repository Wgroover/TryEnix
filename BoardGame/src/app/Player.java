package app;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.shape.Circle;
import util.Position;

public class Player {
    public static final int COOKIES_REQUIRED = 5;

    private String name;
    private IntegerProperty money;
    private IntegerProperty numCookies;
    private Circle inGameObject;
    private Position pos;

    private PlayerStats stats;

    public Player(String name, Circle inGameObject) {
        this(name, 500, inGameObject);
    }

    public Player(String name, int money, Circle inGameObject) {
        this.name = name;
        this.money = new SimpleIntegerProperty(money);
        this.inGameObject = inGameObject;
        this.pos = new Position();
        this.numCookies = new SimpleIntegerProperty(0);
        this.stats = new PlayerStats(this);
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
    public IntegerProperty getMoneyProperty() {
        return money;
    }

    public int getNumCookies() {
        return numCookies.get();
    }
    public void setNumCookies(int numCookies) {
        this.numCookies.set(numCookies);
    }
    public void changeNumCookies(int delta) {
        this.numCookies.set(this.numCookies.get() + delta);
        if (delta > 0) {
            stats.incrementCookiesObtained(delta);
        }
    }
    public IntegerProperty getCookiesProperty() {
        return numCookies;
    }

    public Circle getInGameObject() {
        return inGameObject;
    }

    public Position getPosition() {
        return pos;
    }

    public PlayerStats getStats() {
        return stats;
    }
}
