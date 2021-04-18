package com.swimgame.swim;

import android.graphics.Color;
import com.swimgame.base.Game;
import com.swimgame.base.Graphics;
import com.swimgame.base.Input;
import com.swimgame.base.Screen;
import java.util.List;

// Экран: Таблица лучших результатов

public class HighscoreScreen extends Screen {
    String lines[] = new String[3];                                                               // Значения рекордов

    public HighscoreScreen(Game game) {
        super(game);
        for (int i = 0; i < 3; i++)                                                               // Сохраняем рекорды в массив lines
            lines[i] = String.valueOf(Settings.highscores[i]);
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();                    // Считывание всех касаний экрана
        game.getInput().getKeyEvents();                                                           // Считывание всех нажатий клавиш(пул free)
        int len = touchEvents.size();                                                             // Длина массива касаний

        for (int i = 0; i < len; i++) {                                                           // Перебираем в массиве все касания экрана
            Input.TouchEvent event = touchEvents.get(i);                                          // event - очередное касание
            if (event.type == Input.TouchEvent.TOUCH_UP) {                                        // Если событие = UP, то продолжаем
                if (event.x > 438 && event.y > 490 && event.x < 560 && event.y < 612) {           // НАЖАТА КНОПКА: ВЫХОД В МЕНЮ
                    if (Settings.soundEnabled) Assets.click.play(1);                              // Звук нажатия
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
                if (event.x > 15 && event.y > 20 && event.x < 135 && event.y < 140) {             // ВКЛ|ВЫКЛ МУЗЫКУ
                    Settings.soundEnabled = !Settings.soundEnabled;                               // Инвертируем значение музыки
                    if (Settings.soundEnabled) Assets.click.play(1);                              // Звук нажатия
                    if (Settings.soundEnabled) Assets.menu_media.play();                          // Музыка (on/off)
                    else Assets.menu_media.stop();
                }
            }
        }
    }                                                     // Обновление состояния экрана

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.background, 0, 0);                                                    // Отрисовка фона
        g.drawPixmap(Assets.highscores_label, 220, 20);                                           // Отрисовка метки Рекорды

        g.drawPixmap(Assets.highscores3, 240, 130, 0,   0, 115, 115);                             // Вывод значков рекордов (123)
        g.drawPixmap(Assets.highscores3, 240, 260, 116, 0, 115, 115);
        g.drawPixmap(Assets.highscores3, 240, 390, 232, 0, 115, 115);

        g.drawPixmap(Assets.ok_button, 438, 490);                                                 // Отрисовка кнопки OK

        g.drawText(lines[0], 370, 220, Color.rgb(255, 252, 0));                                   // Первое место: Значение рекорда
        g.drawText(lines[1], 370, 350, Color.WHITE);                                              // Второе место: Значение рекорда
        g.drawText(lines[2], 370, 480, Color.rgb(223, 67, 74));                                   // Третье место: Значение рекорда

        if (Settings.soundEnabled) g.drawPixmap(Assets.buttons, 15, 20, 0, 120, 122, 122);        // Отрисовка значка музыки(on/off)
        else g.drawPixmap(Assets.buttons, 14, 20, 120, 120, 122, 122);
    }                                                    // Вывод на экран

    @Override
    public void pause() {
        if (Assets.menu_media != null) {                                                          // Остановка музыки(если играла)
            if (Settings.soundEnabled) Assets.menu_media.stop();
        }
    }                                                                     // Остановка игры(Музыка)
    @Override
    public void resume() {
        if (Assets.menu_media != null) {                                                          // Возобновление музыки(если включена)
            if (Settings.soundEnabled)
                Assets.menu_media.play();               // Возобновляем музыку
        }
    }                                                                    // Возврат в игру(Музыка)
    @Override
    public void dispose() {  }                                                                    // Заглушка
}



