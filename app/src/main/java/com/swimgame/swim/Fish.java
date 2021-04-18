package com.swimgame.swim;

// Рыбка // Main and Enemy --> split

public abstract class Fish {
    protected static final int FRAME_COUNT = 6;                     // Количество кадров
    public int x, y;                                                // Координаты рыбки
    public int line;                                                // Линия, на которой находится рыбка [1 - 5]
    public int frame;                                               // Текущий кадр анимации для отрисовки
    public int frameTick;                                           // Тики для смены кадры анимации

    public Fish(){
        frame = 0;                                                  // Нулевой кадр
    }


    public boolean toUp(){
        if (line != 1){
            line --;
            return true;
        } else  return false;
    }                  // Подняться на дорожку выше

    public boolean toDown(){
        if (line != 5) {
            line++;
            return true;
        } else return false;
    }                // Опуститься на дорожку ниже

    public abstract void update(); // Переход к следующему кадру[АНИМАЦИЯ]

}