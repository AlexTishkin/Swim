package com.swimgame.base.realize;

import android.view.View;

import com.swimgame.base.Input;

import java.util.ArrayList;
import java.util.List;

/*  Обработка клавиатурных событий   */
public class KeyBoardHandler implements View.OnKeyListener {
    boolean[] pressedKeys = new boolean[128];                               // Нажата клавиша или нет(индекс - код)
    Pool<Input.KeyEvent> keyEventPool;                                      // Экземпляры классов keyEvent
    List<Input.KeyEvent> keyEventsBuffer = new ArrayList<Input.KeyEvent>(); // Еще не обработанные Game-ом KeyEvent
    List<Input.KeyEvent> keyEvents = new ArrayList<Input.KeyEvent>();       // Return list for getKeyEvents()


    public KeyBoardHandler(View view) {                                     // View, от которого получаем клав. события
        Pool.PoolObjectFactory<Input.KeyEvent> factory = new Pool.PoolObjectFactory<Input.KeyEvent>() {
            @Override
            public Input.KeyEvent createObject() {
                return new Input.KeyEvent();
            }
        };
        keyEventPool = new Pool<Input.KeyEvent>(factory, 100);
        view.setOnKeyListener(this);                                        // Устанавливаем слушатель
        view.setFocusableInTouchMode(true);
        view.requestFocus();                                                // Фокусируем вьюху
    }

    @Override
    public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {// Обработка клавиатурного события
        if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE) return false;
        synchronized (this) {                                               // Одновременные нажатия на разные клавиши
            Input.KeyEvent keyEvent = keyEventPool.newObject();             // Полуаем нашу реализацию с пулингом
            keyEvent.keyCode = keyCode;                                     // Сливаем элементы из события
            keyEvent.keyChar = (char) event.getUnicodeChar();
            if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {  // Декодируем тип keyEvent (и сливаем тип и клавишу)
                keyEvent.type = Input.KeyEvent.KEY_DOWN;
                if (keyCode > 0 && keyCode < 127)
                    pressedKeys[keyCode] = true;
            }
            if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                keyEvent.type = Input.KeyEvent.KEY_UP;
                if (keyCode > 0 && keyCode < 127)
                    pressedKeys[keyCode] = false;
            }
            keyEventsBuffer.add(keyEvent);
        }
        return false;
    }

    public boolean isKeyPressed(int keyCode) {                      // Нажата клавиша или нет(из массива)
        if (keyCode < 0 || keyCode > 127)                           // Инты не синхронизируем (прост)
            return false;
        return pressedKeys[keyCode];
    }

    public List<Input.KeyEvent> getKeyEvents() {
        synchronized (this) {                                       // Синхронизируем объект(др. поток обращается к нам)
            int len = keyEvents.size();
            for (int i = 0; i < len; i++)                           // Вставляем в пул все обратно
                keyEventPool.free(keyEvents.get(i));
            keyEvents.clear();                                      // Очищаем keyEvent
            keyEvents.addAll(keyEventsBuffer);                      // Заполняем его необработанными клавишами
            keyEventsBuffer.clear();                                // Очищаем буфер необр. клавиш
            return keyEvents;                                       // Возвращаем свежий списрк keyEvent
        }
    }
    /*
      Недостаток: нам придется достаточно часто вызывать KeyboardHandler.getKeyEvents(),
      иначе список быстро заполнится и нельзя будет возвратить объекты в Pool.
      Однако если мы будем помнить об этом, все должно быть в порядке.
    */
}