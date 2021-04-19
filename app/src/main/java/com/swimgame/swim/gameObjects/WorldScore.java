package com.swimgame.swim.gameObjects;

public class WorldScore {
    // Количество тиков для добавления очков x2
    public static final int SCORE_TICK = 40;

    // Значение, на которое будет увеличиваться счет за проплытие
    public static final int SCORE_INCREMENT = 10;

    // Тики для добавления очков
    public int scoreTick = SCORE_TICK;

    // Текущий счет
    private int score;

    // Текущее количество монет
    public int coinsCount;

    // Состояние монет -> Прибавка к очкам
    public int coinsState;

    public WorldScore() {
        score = 0;
        coinsCount = 0;
        coinsState = 1;
    }

    public int getScore() {
        return score;
    }

    public void resetCoinsState() {
        coinsState = 1;
    }

    // Увеличение счетчика очков и активация пузыря (Сбор монетки)
    // True, если можно активировать защиту (Пузырек)
    public boolean addCoin(Coin coin) {
        if (coin.color == Coin.COLOR_YELLOW) coinsCount++;
        if (coin.color == Coin.COLOR_GREEN) coinsCount += 3;
        if (coin.color == Coin.COLOR_RED) coinsCount += 5;

        if (coinsCount < 50)
            return false;

        coinsCount = 0;
        coinsState++;
        score += 100 * Math.min(coinsState, 5);
        return true;
    }

    // Добавление очков (по тикам)
    public void addScore() {
        if (scoreTick == 0) {
            score += SCORE_INCREMENT;
            scoreTick = SCORE_TICK;
        } else scoreTick--;
    }


}
