package com.swimgame.swim.gameobjects;

import com.swimgame.swim.Assets;
import com.swimgame.swim.Settings;

// Логическая модель мира
public class World {
    public static final int WORLD_HEIGHT = 5;                         // 5 линий, где можно плыть
    public static final int ENEMY_COUNT = 4;                          // Количество врагов

    public static final float TICK_INITIAL = 0.0040f;                 // Начальный временной интервал, используемый для движения объектов 0.0240f;
    public static final float TICK_DECREMENT = 0.00005f;              // Значение, на которое будет уменьшаться тик(увеличение скорости)

    float tickTime = 0;                                               // Счетчик времени, к которой будем прибавлять дельту
    static float tick = TICK_INITIAL;                                 // Скорость движения игровых объектов

    // Игровые объекты
    public WorldBackground background;
    public WorldScore score;
    public MainFish mainFish;
    public EnemyFish[] topEnemyFish;    // Враги (Верхние три полосы)
    public EnemyFish[] bottomEnemyFish; // Враги (Нижние две полосы)
    public Coin[] coins;

    // Мод: добавление врагов(для первого экрана false)
    public boolean enemyMode;
    // Флаг, закончена ли игра
    public boolean gameOver;

    public World() {
        enemyMode = false;
        gameOver = false;

        background = new WorldBackground();
        score = new WorldScore();
        mainFish = new MainFish();
        coins = Coin.createCoins();
        topEnemyFish = EnemyFish.initTopEnemyFish(ENEMY_COUNT);
        bottomEnemyFish = EnemyFish.initBottomEnemyFish(ENEMY_COUNT);
    }

    // Обновление мира и всех объектов в нем(по дельте)
    public void update(float deltaTime) {
        if (gameOver) return;
        // Прибавляем дельту к счетчику(для равномерной смены кадров)
        tickTime += deltaTime;

        // Обновлять столько tick, сколько накопилось [ПРИЕМ]
        while (tickTime > tick) {
            tickTime -= tick;
            mainFish.animate();
            background.move();

            if (enemyMode) {
                onEnemyHandling();
                onCoinHandling();
                score.addScore();
            }
        }
    }

    // Обработка врагов (Анимация, Движение, Пересоздание)
    private void onEnemyHandling() {
        // Произошло столкновение со встречной рыбкой
        if (mainFish.isClash(topEnemyFish, bottomEnemyFish)) {
            if (!mainFish.isProtectedViaBubble) {
                gameOver = true;
                return;
            }

            if (Settings.soundEnabled) Assets.click.play(1);
            mainFish.isProtectedViaBubble = false;
            score.resetCoinsState();
        }

        for (int i = 0; i < ENEMY_COUNT; i++) {
            // Анимация врагов
            topEnemyFish[i].animate();
            bottomEnemyFish[i].animate();

            // Пересоздание врага, если ушел
            topEnemyFish[i].recreateByScore(score.getScore(), true);
            bottomEnemyFish[i].recreateByScore(score.getScore(), false);

            // Движение врагов
            topEnemyFish[i].move();
            bottomEnemyFish[i].move();
        }
    }

    // Обработка монеток (Столкновение, Движение, Пересоздание)
    private void onCoinHandling() {
        for (int i = 0; i < coins.length; i++) {
            // Столкновение с монеткой
            if (mainFish.isClashCoin(coins[i])) {
                if (Settings.soundEnabled) Assets.click.play(1);
                coins[i].isChecked = true;

                // Увеличение счетчика собранных монеток
                boolean isBubbleActivation = score.addCoin(coins[i]);
                if (isBubbleActivation)
                    mainFish.isProtectedViaBubble = true;
            }

            boolean isTop = i < 6;
            coins[i].recreate(isTop);
            coins[i].move();
        }
    }
}

