package com.swimgame.swim.gameObjects;

import java.util.Random;

// Предмет для сбора - монетка
public class Coin {
    public static final int COLOR_YELLOW = 0;
    public static final int COLOR_GREEN = 1;
    public static final int COLOR_RED = 2;

    public int x, y;                           // Координаты монеты
    public int line;                           // Линия, на которой находится монета[1 - 5]
    public int color;                          // цвет
    public boolean isChecked;                  // Чекнута ли монетка или нет

    public static Random random = new Random();

    public Coin(int x, int line, int color) {
        this.x = x;
        this.line = line;
        this.color = color;
        isChecked = false;
    }

    public void recreate(boolean isTop) {
        if (x > -240) return;
        x = 970;
        isChecked = false;
        line = isTop ? random.nextInt(3) + 1 : random.nextInt(2) + 4;
        color = getCoinColor();
    }

    private int getCoinColor() {
        int randomValue = random.nextInt(100);
        if (randomValue < 10) return Coin.COLOR_RED;
        if (randomValue < 40) return Coin.COLOR_GREEN;
        return Coin.COLOR_YELLOW;
    }

    public void move() {
        x -= 1;
    }

    // Инициализация монеток для игры
    public static Coin[] createCoins() {
        Coin[] coins = new Coin[10];
        // Верхние монетки
        coins[0] = new Coin(1200, random.nextInt(3) + 1, Coin.COLOR_YELLOW);
        coins[1] = new Coin(1280, random.nextInt(3) + 1, Coin.COLOR_YELLOW);
        coins[2] = new Coin(1480, random.nextInt(3) + 1, Coin.COLOR_YELLOW);
        coins[3] = new Coin(1560, random.nextInt(3) + 1, Coin.COLOR_YELLOW);
        coins[4] = new Coin(1760, random.nextInt(3) + 1, Coin.COLOR_YELLOW);
        coins[5] = new Coin(1840, random.nextInt(3) + 1, Coin.COLOR_YELLOW);
        // Нижние монетки
        coins[6] = new Coin(1160, random.nextInt(2) + 4, Coin.COLOR_YELLOW);
        coins[7] = new Coin(1360, random.nextInt(2) + 4, Coin.COLOR_YELLOW);
        coins[8] = new Coin(1440, random.nextInt(2) + 4, Coin.COLOR_YELLOW);
        coins[9] = new Coin(1800, random.nextInt(2) + 4, Coin.COLOR_YELLOW);
        return coins;
    }
}
