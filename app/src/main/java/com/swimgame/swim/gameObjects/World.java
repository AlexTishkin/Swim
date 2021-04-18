package com.swimgame.swim.gameObjects;

import com.swimgame.swim.Assets;
import com.swimgame.swim.Settings;

import java.util.Random;

// Логическая модель мира
public class World {
    public static final int WORLD_HEIGHT = 5;                         // 5 линий, где можно плыть
    public static final int ENEMY_COUNT = 4;                          // Количество врагов
    public static final int SCORE_TICK = 40;                          // Количество тиков для добавления очков x2
    public static final int SCORE_INCREMENT = 10;                     // Значение, на которое будет увеличиваться счет за проплытие

    public static final float TICK_INITIAL = 0.0040f;               // Начальный временной интервал, используемый для движения объектов 0.0240f;
    public static final float TICK_DECREMENT = 0.00005f;              // Значение, на которое будет уменьшаться тик(увеличение скорости)

    float tickTime = 0;                                               // Счетчик времени, к которой будем прибавлять дельту
    static float tick = TICK_INITIAL;                                 // Скорость движения игровых объектов

    // Игровые объекты
    public MainFish mainFish;           // Главный герой игры
    public EnemyFish[] topEnemyFish;    // Враги (Верхние три полосы)
    public EnemyFish[] bottomEnemyFish; // Враги (Нижние две полосы)

    public Coin[] coins;    // Монетки -> 3 монетки

    public boolean enemyMode = false;                                 // Мод: добавление врагов(для первого экрана false)
    public boolean gameOver = false;                                  // Флаг, закончена ли игра

    public int score = 0;                                             // Текущий счет
    public int coinsCount = 0;                                        // Текущее количество монет
    public int coinsState = 1;                                        // Состояние монет -> Прибавка к очкам [100 - 200 - 300]
    public int scoreTick = SCORE_TICK;                                // Тики для добавления очков

    private final Random random;                                      // Для генерации чисел

    // Координаты картинок фона
    public int[] backgrounds_x;

    public World() {
        random = new Random();

        mainFish = new MainFish();
        coins = Coin.createCoins();
        topEnemyFish = EnemyFish.initTopEnemyFish(ENEMY_COUNT);
        bottomEnemyFish = EnemyFish.initBottomEnemyFish(ENEMY_COUNT);

        // Задний фон
        backgrounds_x = new int[9];
        for (int i = 0; i < 9; i++)
            backgrounds_x[i] = 960 * i;
    }

    // Обновление мира и всех объектов в нем(по дельте)
    public void update(float deltaTime) {
        if (gameOver)
            return;                                         // Проверка, не окончена ли игра
        tickTime += deltaTime;                              // Прибавляем дельту к счетчику(для равномерной смены кадров)

        while (tickTime > tick) {                                      // Обновлять столько tick, сколько накопилось [ПРИЕМ]
            tickTime -= tick;                                          // Отнимаем tick от счетчика
            /* ТУТ ПРОИСХОДЯТ ВСЕ ВНУТРИИГРОВЫЕ СОБЫТИЯ *////////////////////////////////////////////////////////////////////////////
            moveBackground();                                          // Анимация фона
            mainFish.update();                                         // Анимация рыбки

            // Если рыбка врезалась, то игра закончена
            if (mainFish.isClash(topEnemyFish, bottomEnemyFish)) {
                if (mainFish.isProtectedViaBubble) {
                    if (Settings.soundEnabled) Assets.click.play(1);    // Звук разрыва пузыря
                    mainFish.isProtectedViaBubble = false;
                    this.coinsState = 1;
                } else {
                    gameOver = true;
                    return;
                }
            }

            if (enemyMode) {
                for (int i = 0; i < ENEMY_COUNT; i++) {
                    // Анимация врагов
                    topEnemyFish[i].update();
                    bottomEnemyFish[i].update();

                    // Пересоздание врага, если ушел
                    topEnemyFish[i].recreateByScore(score, true);
                    bottomEnemyFish[i].recreateByScore(score, false);

                    // Движение врагов
                    topEnemyFish[i].move();
                    bottomEnemyFish[i].move();
                }

                // animationCoin
                for (int i = 0; i < 10; i++) {
                    if (!coins[i].isChecked && mainFish.line == coins[i].line &&
                            mainFish.x + 128 >= coins[i].x + 20 && mainFish.x <= coins[i].x + 60) {   // isClashCoin -> После
                        if (Settings.soundEnabled)
                            Assets.click.play(1);        // Звук того, что монета чекнута
                        coins[i].check(); // Монетка чекнута

                        if (coins[i].color == Coin.COLOR_YELLOW) this.coinsCount++;
                        if (coins[i].color == Coin.COLOR_GREEN) this.coinsCount += 3;
                        if (coins[i].color == Coin.COLOR_RED) this.coinsCount += 5;

                        if (coinsCount >= 50) {
                            coinsCount = 0;
                            coinsState++;
                            score += 100 * Math.min(coinsState, 5);
                            mainFish.isProtectedViaBubble = true;
                        }
                    }

                    if (coins[i].x == -240) {
                        coins[i].x = 970; // recreateCoin
                        coins[i].isChecked = false;

                        switch (random.nextInt(40)) {
                            case 19:
                                coins[i].color = Coin.COLOR_RED;
                                break;

                            case 18:
                            case 17:
                            case 16:
                            case 15:
                            case 5:
                            case 4:
                                coins[i].color = Coin.COLOR_GREEN;
                                break;

                            default:
                                coins[i].color = Coin.COLOR_YELLOW;
                                break;
                        }

                        if (i < 6) coins[i].line = random.nextInt(3) + 1;
                        else coins[i].line = random.nextInt(2) + 4;
                    }
                    coins[i].x -= 1;                          // moveCoin
                }

                addScore();                                                // Добавление очков
            }
        }
    }

    // Движение фонового изображения
    public void moveBackground() {
        for (int i = 0; i < 9; i++) {
            backgrounds_x[i]--; // Сдвиг

            if (backgrounds_x[i] <= -960)
                backgrounds_x[i] = 7680;
        }
    }

    // Добавление очков
    public void addScore() {
        if (enemyMode) {
            if (scoreTick == 0) {
                score += SCORE_INCREMENT;
                scoreTick = SCORE_TICK;
            } else scoreTick--;
        }
    }
}

