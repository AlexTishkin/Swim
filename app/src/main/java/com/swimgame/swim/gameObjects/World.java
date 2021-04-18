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

    // ИГРОВЫЕ ОБЪЕКТЫ
    public MainFish mainFish;       // Главный герой игры
    public EnemyFish[] enemyFish;   // Враги // Верхние три полосы
    public EnemyFish[] enemyFish2;  // Враги // Нижние две полосы

    public Coin[] coins;    // Монетки -> 3 монетки

    public boolean enemyMode = false;                                 // Мод: добавление врагов(для первого экрана false)
    public boolean gameOver = false;                                  // Флаг, закончена ли игра
    public int score = 0;                                             // Текущий счет
    public int coinsCount = 0;                                        // Текущее количество монет
    public int coinsState = 1;                                        // Состояние монет -> Прибавка к очкам [100 - 200 - 300]
    float tickTime = 0;                                               // Счетчик времени, к которой будем прибавлять дельту
    static float tick = TICK_INITIAL;                                 // Скорость движения игровых объектов
    public int scoreTick;                                             // Тики для добавления очков

    private Random random;                                            // Для генерации чисел

    // Координаты картинок фона
    public int[] backgrounds_x = {
            0,
            960,
            960 * 2,
            960 * 3,
            960 * 4,
            960 * 5,
            960 * 6,
            960 * 7,
            960 * 8
    };

    public World() {
        tick = TICK_INITIAL;
        scoreTick = SCORE_TICK;
        this.score = 0;
        this.coinsCount = 0;
        coinsState = 1;
        mainFish = new MainFish();  // Главный герой
        initEnemyFishes();           // Вражеские рыбки

        coins = new Coin[10];

        coins[0] = new Coin(1200, random.nextInt(3) + 1);  // Монетки
        coins[1] = new Coin(1280, random.nextInt(3) + 1);
        coins[2] = new Coin(1480, random.nextInt(3) + 1);
        coins[3] = new Coin(1560, random.nextInt(3) + 1);
        coins[4] = new Coin(1760, random.nextInt(3) + 1);
        coins[5] = new Coin(1840, random.nextInt(3) + 1);

        coins[6] = new Coin(1160, random.nextInt(2) + 4);  // Нижние монетки
        coins[7] = new Coin(1360, random.nextInt(2) + 4);
        coins[8] = new Coin(1440, random.nextInt(2) + 4);
        coins[9] = new Coin(1800, random.nextInt(2) + 4);

        for (int i = 0; i < 10; i++)
            coins[i].color = Coin.COLOR_YELLOW; // Цвет coins
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

            // ЩА РЫБЫ БУДУТ СТОЯТЬ НА МЕСТЕ
            if (isClash()) {
                if (mainFish.isProtectedViaBubble) {
                    if (Settings.soundEnabled) Assets.click.play(1);    // Звук разрыва пузыря
                    mainFish.isProtectedViaBubble = false;
                    this.coinsState = 1;
                } else {
                    gameOver = true;
                    return;
                }
            }                // Если рыбка врезалась, то игра закончена

            if (enemyMode) {
                for (int i = 0; i < ENEMY_COUNT; i++) {
                    enemyFish[i].update();                                  // Анимация верхних врагов
                    enemyFish2[i].update();                                 // Анимация нижних  врагов

                    recreateEnemy(enemyFish[i], true);                     // Пересоздание врага, если ушел[line 1-3]
                    recreateEnemy(enemyFish2[i], false);                   // Пересоздание врага, если ушел[line 4-5]

                    enemyFish[i].move();                                   // Движение верхних врагов
                    enemyFish2[i].move();                                  // Движение нижних врагов

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

    // Базовая инициализация вражеских рыбок
    public void initEnemyFishes() {
        random = new Random();
        enemyFish = new EnemyFish[ENEMY_COUNT];

        enemyFish[0] = new EnemyFish(960);
        enemyFish[0].type = EnemyFish.TYPE_ENEMY1;
        enemyFish[0].line = random.nextInt(3) + 1;
        enemyFish[0].isShark = true;

        enemyFish[1] = new EnemyFish(1360);
        enemyFish[1].type = EnemyFish.TYPE_ENEMY1;
        enemyFish[1].line = random.nextInt(3) + 1;
        enemyFish[1].isShark = false;

        enemyFish[2] = new EnemyFish(1640);
        enemyFish[2].type = EnemyFish.TYPE_ENEMY1;
        enemyFish[2].line = random.nextInt(3) + 1;
        enemyFish[2].isShark = false;

        enemyFish[3] = new EnemyFish(1920);
        enemyFish[3].type = EnemyFish.TYPE_ENEMY1;
        enemyFish[3].line = random.nextInt(3) + 1;
        enemyFish[3].isShark = false;
        enemyFish[3].isVisible = false;

        enemyFish2 = new EnemyFish[ENEMY_COUNT];
        enemyFish2[0] = new EnemyFish(960);
        enemyFish2[0].type = EnemyFish.TYPE_ENEMY1;
        enemyFish2[0].line = random.nextInt(2) + 4;
        enemyFish2[0].isShark = false;

        enemyFish2[1] = new EnemyFish(1240);
        enemyFish2[1].type = EnemyFish.TYPE_ENEMY1;
        enemyFish2[1].line = random.nextInt(2) + 4;
        enemyFish2[1].isShark = false;
        enemyFish2[1].isVisible = false;

        enemyFish2[2] = new EnemyFish(1520);
        enemyFish2[2].type = EnemyFish.TYPE_ENEMY1;
        enemyFish2[2].line = random.nextInt(2) + 4;
        enemyFish2[2].isShark = true;
        enemyFish2[2].isVisible = false;

        enemyFish2[3] = new EnemyFish(1920);
        enemyFish2[3].type = EnemyFish.TYPE_ENEMY1;
        enemyFish2[3].line = random.nextInt(2) + 4;
        enemyFish2[3].isShark = false;
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

    // Было ли столкновение
    public boolean isClash() {
        boolean clash = false;
        for (int i = 0; i < ENEMY_COUNT; i++)
            if (enemyFish[i].type == EnemyFish.TYPE_ENEMY1) { // Столкновение с бич-рыбой
                if (mainFish.line == enemyFish[i].line && mainFish.x < enemyFish[i].x
                        && enemyFish[i].x < mainFish.x + 128 && enemyFish[i].isVisible) {
                    if (mainFish.isProtectedViaBubble) enemyFish[i].isVisible = false;
                    clash = true;
                    break;
                }
            } else if (enemyFish[i].type == EnemyFish.TYPE_ENEMY2) { // Столкновение с акулой
                if ((mainFish.line == enemyFish[i].line || mainFish.line == enemyFish[i].line + 1) && mainFish.x < enemyFish[i].x
                        && enemyFish[i].x < mainFish.x + 128 && enemyFish[i].isVisible) {
                    if (mainFish.isProtectedViaBubble) enemyFish[i].isVisible = false;
                    clash = true;
                    break;
                }
            }

        if (!clash) {
            for (int i = 0; i < ENEMY_COUNT; i++)
                if (enemyFish2[i].type == EnemyFish.TYPE_ENEMY1) { // Столкновение с бич-рыбой
                    if (mainFish.line == enemyFish2[i].line && mainFish.x < enemyFish2[i].x
                            && enemyFish2[i].x < mainFish.x + 128 && enemyFish2[i].isVisible) {
                        if (mainFish.isProtectedViaBubble) enemyFish2[i].isVisible = false;
                        clash = true;
                        break;
                    }
                } else if (enemyFish2[i].type == EnemyFish.TYPE_ENEMY2) { // Столкновение с акулой
                    if ((mainFish.line == enemyFish2[i].line || mainFish.line == enemyFish2[i].line + 1) && mainFish.x < enemyFish2[i].x
                            && enemyFish2[i].x < mainFish.x + 128 && enemyFish2[i].isVisible) {
                        if (mainFish.isProtectedViaBubble) enemyFish2[i].isVisible = false;
                        clash = true;
                        break;
                    }
                }
        }

        return clash;
    }

    // Перемещение удалившейся рыбки//Верх|Низ
    public void recreateEnemy(EnemyFish enemy, boolean isTop) {
        if (enemy.x == -240) { // Пересоздание рыбы [Не меняя вида]
            enemy.x = 970; // Тут можно создавать рыбу

            enemy.color = getColorEnemyFish();

            if (enemy.color == EnemyFish.COLOR_RED && enemy.type == EnemyFish.TYPE_ENEMY1)
                enemy.isTurned = false; // Новая красная рыба - не поворачивала
            if (!enemy.isVisible && score > 1000)
                enemy.isVisible = true; // Включаем видимость -> Уже другая рыба

            if (isTop) enemy.line = random.nextInt(3) + 1;  // Линия та же   -> Первые 2 линии
            else enemy.line = random.nextInt(2) + 4;


            if (enemy.isShark) {   // Если есть возможность передислоцироваться в акулу
                if (!getShark()) enemy.type = EnemyFish.TYPE_ENEMY1; // В рыбу
                else {                                                // В акулу
                    enemy.type = EnemyFish.TYPE_ENEMY2;
                    if (isTop)
                        enemy.line = random.nextInt(2) + 1; // Чтобы не зацепить акулой рыбку с 4 и 5 линии =)
                    else enemy.line = 4;
                }
            } // if enemy.isShark
        }
    }

    // Возвращение цвета рыбки в зависимости от очков -> По таблице
    public int getColorEnemyFish() {
        int returnColor = random.nextInt(2) + 101; // Синяя | Зеленая
        int randValue = 1;

        if (score > 2000 && score < 2500) randValue = random.nextInt(10);
        else if (score > 2500 && score < 3000) randValue = random.nextInt(8);
        else if (score > 3000 && score < 4000) randValue = random.nextInt(6);
        else if (score > 4000 && score < 7500) randValue = random.nextInt(4);
        else if (score > 7500 && score < 10000) randValue = random.nextInt(3);
        else if (score > 10000 && score < 15000) randValue = random.nextInt(2);
        else if (score > 15000) randValue = 0;

        if (randValue == 0) returnColor = EnemyFish.COLOR_RED;

        return returnColor;
    }

    // Перевоплощение в акулу в зависимости от очков -> По таблице
    public boolean getShark() {
        boolean returnValue = false;  // default - none
        int randValue = 1;

        if (score > 2000 && score < 3000) randValue = random.nextInt(10);
        else if (score > 3000 && score < 4000) randValue = random.nextInt(8);
        else if (score > 4000 && score < 7500) randValue = random.nextInt(6);
        else if (score > 7500 && score < 10000) randValue = random.nextInt(3);
        else if (score > 10000 && score < 12000) randValue = random.nextInt(2);
        else if (score > 12000) randValue = 0;

        if (randValue == 0) returnValue = true;

        return returnValue;
    }

}

