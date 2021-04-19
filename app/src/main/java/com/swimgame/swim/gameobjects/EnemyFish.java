package com.swimgame.swim.gameobjects;

import java.util.Random;

// Вражеские рыбки [Простая и Акула]
public class EnemyFish extends Fish {
    public static final int TYPE_ENEMY_FISH = 0;  // Простая рыбка
    public static final int TYPE_ENEMY_SHARK = 1;  // Акула -> 2 клетки

    public static final int COLOR_RED = 100; // Цвет рыбки -> Для красоты
    public static final int COLOR_GREEN = 101;
    public static final int COLOR_BLUE = 102;

    // TODO: Не задействовано
    public static final int SPEED_SLOW = 44;  // Медленная рыбка
    public static final int SPEED_MEDIUM = 22;  // Средняя рыбка
    public static final int SPEED_FAST = 11;  // Быстрая рыбка

    public boolean isShark;               // Есть возможность передислоцироваться в Акулу
    public boolean isVisible;             // Видима, для пузырька

    public int type;                      // Тип рыбки
    public int color;                     // Цвет рыбки
    public int speed;                     // Скорость рыбки

    public boolean isTurned;              // Красная рыба -> Делала поворот или нет

    public static Random random = new Random();

    // Обновление анимации и движения
    @Override
    public void animate() {
        if (frameTick != speed) {
            frameTick++;
            return;
        }
        frame = frame + 1 != FRAME_COUNT ? frame + 1 : 0;
        frameTick = 0;
    }

    // Движение
    public void move() {
        this.x -= 2;
        if (color == COLOR_RED && type == TYPE_ENEMY_FISH && random.nextInt(150) == 0 && !isTurned && x > 600 && x < 800) {
            if (line == 1 || line == 4) line++;
            else if (line == 3 || line == 5) line--;
            else {
                boolean whereTurn = false;
                if (random.nextInt(2) == 0) whereTurn = true;
                if (whereTurn) line++;
                else line--;
            }
            isTurned = true;
        }
    }

    // Создание новой рыбы-врага (Когда текущая уплыла за границу)
    public void recreateByScore(int score, boolean isTop) {
        if (x > -240) return;
        x = 970;
        isTurned = false;
        isVisible = true;
        color = getColorEnemyFishByScore(score);
        line = isTop ? random.nextInt(3) + 1 : random.nextInt(2) + 4;

        // Появление акулы (из рыбы)
        type = isShark && isToSharkByScore(score) ? EnemyFish.TYPE_ENEMY_SHARK : EnemyFish.TYPE_ENEMY_FISH;
        if (type == EnemyFish.TYPE_ENEMY_SHARK) {
            // Top: Чтобы не зацепить акулой рыбку с 4 и 5 линии
            line = isTop ? random.nextInt(2) + 1 : 4;
        }
    }

    // Возвращение цвета рыбки в зависимости от очков -> По таблице
    private int getColorEnemyFishByScore(int score) {
        int randValue = 1;

        if (score < 2000) randValue = 1;
        else if (score > 2500 && score < 3000) randValue = random.nextInt(8);
        else if (score > 3000 && score < 4000) randValue = random.nextInt(6);
        else if (score > 4000 && score < 7500) randValue = random.nextInt(4);
        else if (score > 7500 && score < 10000) randValue = random.nextInt(3);
        else if (score > 10000 && score < 15000) randValue = random.nextInt(2);
        else if (score > 15000) randValue = 0;

        return randValue == 0 ? EnemyFish.COLOR_RED : random.nextInt(2) + 101; // Синяя | Зеленая
    }

    // Превратиться в рыбу согласно очкам
    private boolean isToSharkByScore(int score) {
        int randValue = 1;

        if (score < 2500) randValue = 1;
        else if (score > 2500 && score < 4000) randValue = random.nextInt(8);
        else if (score > 4000 && score < 7500) randValue = random.nextInt(6);
        else if (score > 7500 && score < 10000) randValue = random.nextInt(3);
        else if (score > 10000 && score < 12000) randValue = random.nextInt(2);
        else if (score > 12000) randValue = 0;

        return randValue == 0;
    }

    // Инициализация рыбок для игры
    public static EnemyFish[] initTopEnemyFish(int enemyCount) {
        EnemyFish[] topEnemyFish = new EnemyFish[enemyCount];
        topEnemyFish[0] = createEnemyFish(960, EnemyFish.TYPE_ENEMY_FISH, true, true, true);
        topEnemyFish[1] = createEnemyFish(1360, EnemyFish.TYPE_ENEMY_FISH, true, false, true);
        topEnemyFish[2] = createEnemyFish(1640, EnemyFish.TYPE_ENEMY_FISH, true, false, true);
        topEnemyFish[3] = createEnemyFish(1920, EnemyFish.TYPE_ENEMY_FISH, false, false, true);
        return topEnemyFish;
    }

    public static EnemyFish[] initBottomEnemyFish(int enemyCount) {
        EnemyFish[] bottomEnemyFish = new EnemyFish[enemyCount];
        bottomEnemyFish[0] = createEnemyFish(960, EnemyFish.TYPE_ENEMY_FISH, true, false, false);
        bottomEnemyFish[1] = createEnemyFish(1240, EnemyFish.TYPE_ENEMY_FISH, false, false, false);
        bottomEnemyFish[2] = createEnemyFish(1520, EnemyFish.TYPE_ENEMY_FISH, false, true, false);
        bottomEnemyFish[3] = createEnemyFish(1920, EnemyFish.TYPE_ENEMY_FISH, true, false, false);
        return bottomEnemyFish;
    }

    private static EnemyFish createEnemyFish(int x, int type, boolean isVisible, boolean canToShark, boolean isTop) {
        EnemyFish enemyFish = new EnemyFish();
        enemyFish.x = x;
        enemyFish.color = random.nextInt(2) + 101; // Случайный цвет Зеленый ИЛИ Синий
        enemyFish.type = type; //EnemyFish.TYPE_ENEMY1;
        enemyFish.line = isTop ? random.nextInt(3) + 1 : random.nextInt(2) + 4;
        enemyFish.isVisible = isVisible;
        enemyFish.isShark = canToShark;
        enemyFish.isTurned = false;
        enemyFish.speed = SPEED_MEDIUM;
        return enemyFish;
    }
}
