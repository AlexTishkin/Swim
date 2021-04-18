package com.swimgame.base.realize;

import android.content.Context;
import android.os.Build;
import android.view.View;

import com.swimgame.base.Input;

import java.util.List;


// API ОБРАБОТКИ ВВОДА НАШЕГО ФРЕЙМВОРКА

public class AndroidInput implements Input {
    AccelerometerHandler accelHandler;                          // Акселерометр
    KeyBoardHandler keyHandler;                                 // Обработка клавиш
    TouchHandler touchHandler;                                  // Обработка касаний

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
        accelHandler = new AccelerometerHandler(context);       // Инициализация обработчиков
        keyHandler = new KeyBoardHandler(view);
        if (Integer.parseInt(Build.VERSION.SDK) < 5)             // В зависимости от версии
            touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
        else touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
    }

    ////////////////// КАЖДЫЙ ВЫЗОВ МЕТОДА ПЕРЕДАЕТСЯ СООТВЕТСТВУЮЩЕМУ ОБРАБОТЧИКУ //////////////////
    @Override
    public boolean isKeyPressed(int keyCode) {
        return keyHandler.isKeyPressed(keyCode);
    }
    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }
    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    @Override
    public float getAccelX() {
        return accelHandler.getAccelX();
    }
    @Override
    public float getAccelY() {
        return accelHandler.getAccelY();
    }
    @Override
    public float getAccelZ() { return accelHandler.getAccelZ(); }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
    @Override
    public List<KeyEvent> getKeyEvents() {
        return keyHandler.getKeyEvents();
    }
}