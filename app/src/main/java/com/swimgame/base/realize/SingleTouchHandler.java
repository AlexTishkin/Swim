package com.swimgame.base.realize;

import android.view.MotionEvent;
import android.view.View;

import com.swimgame.base.Input;

import java.util.ArrayList;
import java.util.List;

//  Реализация интерфейса TouchHandler для Android версий 1.5 и 1.6.
//  Обработка одиночных касаний

public class SingleTouchHandler implements TouchHandler {
    boolean isTouched;                                                              // Осуществлено ли касание?
    int touchX;                                                                     // Координата X касания
    int touchY;                                                                     // Координата Y касания
    Pool<Input.TouchEvent> touchEventPool;                                          // Экзы в пулинг
    List<Input.TouchEvent> touchEvents = new ArrayList<Input.TouchEvent>();         // Для возврата касаний
    List<Input.TouchEvent> touchEventsBuffer = new ArrayList<Input.TouchEvent>();   // Еще не обработанные касания
    float scaleX;                                                                   // Для разных девайсов
    float scaleY;

    public SingleTouchHandler(View view, float scaleX, float scaleY) {              // Конструктор
        Pool.PoolObjectFactory<Input.TouchEvent> factory = new                      // Аналог с клавишами(пулинг)
                Pool.PoolObjectFactory<Input.TouchEvent>() {
                    @Override
                    public Input.TouchEvent createObject() {
                        return new Input.TouchEvent();
                    }
                };
        touchEventPool = new Pool<Input.TouchEvent>(factory, 100);                  // Получаем нашу реализацию с пулингом
        view.setOnTouchListener(this);                                              // Регистрируем обработчик касаний
        this.scaleX = scaleX;                                                       // Для разных девайсов
        this.scaleY = scaleY;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {                             // Обработка касания (АНАЛОГ КЛАВИШ)
        synchronized (this) {
            Input.TouchEvent touchEvent = touchEventPool.newObject();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchEvent.type = Input.TouchEvent.TOUCH_DOWN;
                    isTouched = true;
                    break;

                case MotionEvent.ACTION_MOVE:
                    touchEvent.type = Input.TouchEvent.TOUCH_DRAGGED;
                    isTouched = true;
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    touchEvent.type = Input.TouchEvent.TOUCH_UP;
                    isTouched = false;
                    break;
            }
            touchEvent.x = touchX = (int) (event.getX() * scaleX);                 // Подгоняем под девайс
            touchEvent.y = touchY = (int) (event.getY() * scaleY);                 // Подгоняем под девайс
            touchEventsBuffer.add(touchEvent);
            return true;
        }
    }


    @Override
    public boolean isTouchDown(int pointer) {                                      // Есть ли касание
        synchronized (this) {
            if (pointer == 0)
                return isTouched;                                                  // Чисто SingleTouch
            else return false;
        }
    }

    @Override
    public int getTouchX(int pointer) {                                            // return X касания
        synchronized (this) {
            return touchX;
        }
    }

    @Override
    public int getTouchY(int pointer) {                                            // return Y касания
        synchronized (this) {
            return touchY;
        }
    }


    @Override
    public List<Input.TouchEvent> getTouchEvents() {                               // Возвращение листа касаний(вызывать часто!)
        synchronized (this) {
            int len = touchEvents.size();
            for (int i = 0; i < len; i++)
                touchEventPool.free(touchEvents.get(i));
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }
}