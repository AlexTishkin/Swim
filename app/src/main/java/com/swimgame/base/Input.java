package com.swimgame.base;

import java.util.List;

/*
Интерфейс Input: Доступ к вх. потоку: получение реакции юзера
 Опрашивание событий:
   1. Сенсорный экран
   2. Акселерометр
   3. Клавиатура
 Предоставление доступа:
   1. Обработчик событий от дисплея
   2. Обработчик событий от клавиатуры
*/

public interface Input {
    // !!!РАБОТА С СОБЫТИЯМИ КЛАВИАТУРЫ!!! //
    public static class KeyEvent {
        public static final int KEY_DOWN = 0;           // КОНСТАНТА НАЖАТИЯ НА КЛАВИШУ
        public static final int KEY_UP = 1;             // КОНСТАНТА ОТПУСКАНИЯ КЛАВИШИ
        public int type;                                // Тип события(KEY_DOWN | KEY_UP)
        public int keyCode;                             // Код клавиши
        public char keyChar;                            // Юникод-код
    }
    // !!!РАБОТА С СОБЫТИЯМИ ДИСПЛЕЯ!!! //
    public static class TouchEvent {
        public static final int TOUCH_DOWN = 0;        // КОНСТАНТА КАСАНИЯ ДИСПЛЕЯ
        public static final int TOUCH_UP = 1;          // КОНСТАНТА ОТПУСКАНИЯ ДИСПЛЕЯ
        public static final int TOUCH_DRAGGED = 2;     // КОНСТАНТА ДВИЖЕНИЯ ПАЛЬЦА ПО ДИСПЛЕЮ
        public int type;                               // Тип события(^)
        public int x, y;                               // Координаты касания
        public int pointer;                            // ID указателя(мультитач)
    }

    ///////////////////////////** МЕТОДЫ ОПРАШИВАНИЯ ИНТЕРФЕЙСА INPUT **///////////////////////////

    // ОБРАБОТКА НАЖАТИЙ КЛАВИЙ //
    public boolean isKeyPressed(int keyCode);          // Нажата клавиша keyCode или нет

    // СЕНСОРНЫЙ ЭКРАН //
    public boolean isTouchDown(int pointer);           // Состояние касания pointer(ID)
    public int getTouchX(int pointer);                 // Координата X касания pointer(ID)
    public int getTouchY(int pointer);                 // Координата Y касания pointer(ID)

    // АКСЕЛЕРОМЕТР //
    public float getAccelX();                          // Ускорение по оси X
    public float getAccelY();                          // Ускорение по оси Y
    public float getAccelZ();                          // Ускорение по оси Z

    // События расположены в порядке их появления: самое свежее в них размещено в конце списка
    public List<KeyEvent> getKeyEvents();              // Реализация обработчика нажатий на клавиши
    public List<TouchEvent> getTouchEvents();          // Реализация обработчика касаний
}