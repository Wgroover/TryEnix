package app;

import tiles.ChanceTile;
import tiles.GreenTile;
import tiles.RedTile;
import tiles.Tile;
import util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    public static final int NUM_CHANCE_TILES = 4;

    private Tile[][] tiles;
    private List<Player> players;

    public Board(List<Player> players) {
        this.players = new ArrayList<>(players);
        initTiles();
    }

    private void initTiles() {
        this.tiles = new Tile[HEIGHT][WIDTH];

        Random random = new Random();
        int count = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (count < NUM_CHANCE_TILES) {
                    tiles[i][j] = new ChanceTile(i, j);
                    count++;
                    continue;
                }
                tiles[i][j] = Math.random() < 0.4 ? new RedTile(i, j) : new GreenTile(i, j);
            }
        }

        for (int k = 0; k < 1000; k++) {
            int i1 = random.nextInt(tiles.length), i2 = random.nextInt(tiles.length);
            int j1 = random.nextInt(tiles[i1].length), j2 = random.nextInt(tiles[i2].length);

            Tile t = tiles[i1][j1];
            tiles[i1][j1] = tiles[i2][j2];
            tiles[i2][j2] = t;
        }
    }

    public void move(Player player, int n, Direction direction) {
        if (n < 0) {
            return;
        }

        Position p = player.getPosition();
        if (direction == Direction.UP) {
            p.setI((p.getI() + HEIGHT - n % HEIGHT) % HEIGHT);
        } else if (direction == Direction.DOWN) {
            p.setI((p.getI() + n) % HEIGHT);
        } else if (direction == Direction.LEFT) {
            p.setJ((p.getJ() + WIDTH - n % WIDTH) % WIDTH);
        } else {
            p.setJ((p.getJ() + n) % WIDTH);
        }

        tiles[p.getI()][p.getJ()].onEnter(player);
    }

    public Tile[][] getTiles() {
        return tiles;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public Player getCurrentPlayer() {
        return players.get(0);
    }

    public enum Direction {
        LEFT, RIGHT, UP, DOWN
    }

}
