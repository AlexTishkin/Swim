package com.swimgame.base.realize;

import android.view.View;

import com.swimgame.base.Input;

import java.util.List;


// Интерфейс для обработки касаний(SingleTouch and MultiTouch)

public interface TouchHandler extends View.OnTouchListener {
    public boolean isTouchDown(int pointer);                        // Палец на экране ?
    public int getTouchX(int pointer);                              // Координата X касания
    public int getTouchY(int pointer);                              // Координата Y касания
    public List<Input.TouchEvent> getTouchEvents();                 // Список всех касаний
}