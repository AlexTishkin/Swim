package com.swimgame.swim.gameObjects;

public abstract class Fish {
    protected static final int FRAME_COUNT = 6; // Количество кадров
    public int x, y;        // Координаты рыбки
    public int line;        // Линия, на которой находится рыбка [1 - 5]
    public int frame;       // Текущий кадр анимации для отрисовки
    public int frameTick;   // Тики для смены кадры анимации

    public Fish() {
        frame = 0;
    }

    // Подняться на дорожку выше
    public boolean toUp() {
        if (line == 1)
            return false;
        line--;
        return true;
    }

    // Опуститься на дорожку ниже
    public boolean toDown() {
        if (line == 5)
            return false;
        line++;
        return true;
    }

    // Переход к следующему кадру (Анимация)
    public abstract void update();
}