package com.swimgame.swim.gameObjects;

// Предмет для сбора - монетка
public class Coin {
    public static final int COLOR_YELLOW = 0;
    public static final int COLOR_GREEN  = 1;
    public static final int COLOR_RED    = 2;

    public int x, y;                           // Координаты монеты
    public int line;                           // Линия, на которой находится монета[1 - 5]
    public int color;                          // цвет
    public boolean isChecked;                  // Чекнута ли монетка или нет

    public Coin(int x, int line){
        this.x = x;
        this.line = line;
        isChecked = false;
    }

    public void check(){
        isChecked = true;
    }
}
