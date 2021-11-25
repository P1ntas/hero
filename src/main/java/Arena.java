import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena {

    private final int width;
    private final int height;
    private final Hero hero;
    private final List<Wall> walls;
    private final List<Coin> coins;
    private final List<Monster> monsters;


    public Arena(int width, int height) {
        this.width = width;
        this.height = height;
        this.walls = createWalls();
        this.coins = createCoins();
        this.monsters = createMonsters();
        hero = new Hero(width / 2, height / 2);
    }

    private List<Wall> createWalls() {
        List<Wall> walls = new ArrayList<>();

        for (int w = 0; w < width; w++) {
            walls.add(new Wall(w, 0));
            walls.add(new Wall(w, height - 1));
        }
        for (int i = 1; i < height - 1; i++) {
            walls.add(new Wall(0, i));
            walls.add(new Wall(width - 1, i));
        }
        return walls;
    }

    private List<Coin> createCoins() {
        Random random = new Random();
        ArrayList<Coin> coins = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            coins.add(new Coin(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1));
        }
            return coins;

        }

    public void processKey(KeyStroke key) {

        switch (key.getKeyType()) {
            case ArrowUp -> moveHero(hero.moveUp());
            case ArrowDown -> moveHero(hero.moveDown());
            case ArrowLeft -> moveHero(hero.moveLeft());
            case ArrowRight -> moveHero(hero.moveRight());
        }
        retrieveCoins();
        verifyMonsterCollisions();
        moveMonsters();
        verifyMonsterCollisions();
    }

    public void moveHero(Position position) {
        if (canHeroMove(position)) {
            hero.setPosition(position);
        }
    }

    private boolean canHeroMove(Position position) {
        if (position.getX() < 0) return false;
        else if (position.getX() > width) return false;
        else if (position.getY() < 0) return false;
        else if (position.getY() > height) return false;
        for ( Wall wall : walls) {
            if (wall.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

    private void retrieveCoins() {
        for (Coin coin : coins)
            if (hero.getPosition().equals(coin.getPosition())) {
                coins.remove(coin);
                break;
            }
    }

    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#008000"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        graphics.setForegroundColor(TextColor.Factory.fromString("#FFFF33"));
        hero.draw(graphics);

        for (Wall wall : walls) {
            wall.draw(graphics);
        }
        for (Coin coin : coins) {
            coin.draw(graphics);
        }
    }

    private List<Monster> createMonsters() {
        Random random = new Random();
        ArrayList<Monster> monsters = new ArrayList<>();
        for (int k = 0; k < 5; k++)
            monsters.add(new Monster(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1));
        return monsters;
    }

    private void verifyMonsterCollisions() {
        for (Monster monster : monsters)
            if (hero.getPosition().equals(monster.getPosition())) {
                System.out.println("You died!");
                System.exit(0);
            }
    }

    private void moveMonsters() {
        for (Monster monster : monsters) {
            Position monsterPosition = monster.move();
            if (canHeroMove(monsterPosition))
                monster.setPosition(monsterPosition);
        }
    }
}
