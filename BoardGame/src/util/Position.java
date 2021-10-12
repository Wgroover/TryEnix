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

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Position)) {
            return false;
        }
        Position p = (Position) other;
        return this.i == p.i && this.j == p.j;
    }

    @Override
    public String toString() {
        return "(" + i + ", " + j + ")";
    }
}
