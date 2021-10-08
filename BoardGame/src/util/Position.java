package util;

public class Position {

    private int i, j;

    public Position(int i, int j) {
        this.i = i;
        this.j = j;
    }
    public Position() {
        this(0, 0);
    }

    public void move(int deltaI, int deltaJ) {
        this.i += deltaI;
        this.j += deltaJ;
    }
    public void moveTo(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }
    public void setI(int i) {
        this.i = i;
    }
    public int getJ() {
        return j;
    }
    public void setJ(int j) {
        this.j = j;
    }
}
