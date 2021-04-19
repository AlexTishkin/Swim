package com.swimgame.swim.gameObjects;

// Главный герой игры - рыбка ^_^
public class MainFish extends Fish {
    private static final int FRAME_TICK = 22;   // Количество тиков для анимации

    public boolean isProtectedViaBubble;    // Защита Пузырек -> Один раз + Визуализация

    public MainFish() {
        line = 2;
        x = 150;
        isProtectedViaBubble = false;
    }

    @Override
    public void animate() {
        if (frameTick != FRAME_TICK) {
            frameTick++;
            return;
        }
        frame = frame + 1 != FRAME_COUNT ? frame + 1 : 0;
        frameTick = 0;
    }

    // Столкновение с монеткой
    public boolean isClashCoin(Coin coin) {
        return !coin.isChecked && coin.line == line && x + 128 >= coin.x + 20 && x <= coin.x + 60;
    }

    // Столкновение со встречными рыбами
    public boolean isClash(EnemyFish[] topEnemyFish, EnemyFish[] bottomEnemyFish) {
        return isClash(topEnemyFish) || isClash(bottomEnemyFish);
    }

    private boolean isClash(EnemyFish[] enemyFishes) {
        for (EnemyFish enemyFish : enemyFishes) {
            // Столкновение с бич-рыбой
            if (enemyFish.type == EnemyFish.TYPE_ENEMY_FISH) {
                if (line == enemyFish.line && x < enemyFish.x && enemyFish.x < x + 128 && enemyFish.isVisible) {
                    if (isProtectedViaBubble) enemyFish.isVisible = false;
                    return true;
                }
            }
            // Столкновение с акулой
            if (enemyFish.type == EnemyFish.TYPE_ENEMY_SHARK) {
                if ((line == enemyFish.line || line == enemyFish.line + 1) && x < enemyFish.x && enemyFish.x < x + 128 && enemyFish.isVisible) {
                    if (isProtectedViaBubble) enemyFish.isVisible = false;
                    return true;
                }
            }
        }
        return false;
    }


}