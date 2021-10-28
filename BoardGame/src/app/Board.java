package app;

import javafx.application.Platform;
import tiles.ChanceTile;
import tiles.CookieTile;
import tiles.GreenTile;
import tiles.PaywallTile;
import tiles.RedTile;
import tiles.Tile;
import util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

public class Board {

    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    public static final int NUM_CHANCE_TILES = 4;
    public static final int NUM_PAYWALL_TILES = 8;
    public static final int NUM_COOKIE_TILES = 2;

    private Tile[][] tiles;
    private List<Player> players;

    private BoardUI ui;

    public Board(List<Player> players) {
        this.players = new ArrayList<>(players);
        initTiles();
    }

    public void win() {
        if (ui != null) {
            ui.showWin();
        } else {
            Optional<Player> winner = players.stream().filter(p -> p.getNumCookies() >= Player.COOKIES_REQUIRED).findFirst();
            if (winner.isEmpty()) {
                throw new IllegalStateException("No winner");
            }
            System.out.println("Winner: " + winner.get().getName());
        }
    }
    public void promptBuyCookie(Player player, CookieTile cookieTile) {
        Consumer<Boolean> callback = b -> {
            if (b) {
                cookieTile.payForCookie(player);
                if (player.getNumCookies() >= Player.COOKIES_REQUIRED) {
                    this.win();
                }
            }
        };

        if (ui == null) {
            callback.accept(player.getMoney() >= cookieTile.getCookieCost());
        } else if (player.getMoney() < cookieTile.getCookieCost()) {
            callback.accept(false);
        } else {
            Platform.runLater(() -> ui.promptBuyCookie(player, cookieTile, callback));
        }
    }
    public void promptPaywall(Player player, PaywallTile paywall) {
        Consumer<Boolean> callback = b -> {
            if (b) {
                player.changeMoney(-paywall.getFee());
                paywall.open();
            } else {
                System.out.println("no pay");
            }
        };

        if (ui == null) {
            callback.accept(player.getMoney() >= paywall.getFee());
        } else if (player.getMoney() < paywall.getFee()) {
            callback.accept(false);
        } else {
            Platform.runLater(() -> ui.promptPaywall(player, paywall, callback));
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

        if (ui != null) {
            ui.updateAllPlayerDisplayPositions();
        }

        tiles[p.getI()][p.getJ()].onEnter(player, this);
    }

    public void bindUI(BoardUI ui) {
        this.ui = ui;
    }
    public BoardUI getUI() {
        return ui;
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

    private void initTiles() {
        this.tiles = new Tile[HEIGHT][WIDTH];

        Random random = new Random();
        int count = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (count < NUM_CHANCE_TILES) {
                    tiles[i][j] = new ChanceTile();
                    count++;
                    continue;
                } else if (count < NUM_CHANCE_TILES + NUM_PAYWALL_TILES) {
                    tiles[i][j] = new PaywallTile();
                    count++;
                    continue;
                } else if (count < NUM_CHANCE_TILES + NUM_PAYWALL_TILES + NUM_COOKIE_TILES) {
                    tiles[i][j] = new CookieTile();
                    count++;
                    continue;
                }
                tiles[i][j] = Math.random() < 0.4 ? new RedTile() : new GreenTile();
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

    public enum Direction {
        LEFT, RIGHT, UP, DOWN
    }
}
