package com.swimgame.swim.screens;

import com.swimgame.base.Game;
import com.swimgame.base.Graphics;
import com.swimgame.base.Input;
import com.swimgame.base.Screen;
import com.swimgame.swim.Assets;
import com.swimgame.swim.Settings;
import com.swimgame.swim.screens.MainMenuScreen;

import java.util.List;

// Экран: справочная информация по игре

public class HelpScreen extends Screen {
    public HelpScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();                    // Считывание всех касаний экрана
        game.getInput().getKeyEvents();                                                           // Считывание всех нажатий клавиш(пул free)
        int len = touchEvents.size();                                                             // Длина массива касаний

        for (int i = 0; i < len; i++) {                                                           // Перебираем в массиве все касания экрана
            Input.TouchEvent event = touchEvents.get(i);                                          // event - очередное касание

            if (event.type == Input.TouchEvent.TOUCH_DOWN){
                if (inBounds(event, 49, 155, 378, 376)){
                    Settings.controlEnabled = true;
                    if (Settings.soundEnabled) Assets.click.play(1);                              // Звук нажатия
                } else
                if (inBounds(event, 541, 155, 378, 376)){
                    Settings.controlEnabled = false;
                    if (Settings.soundEnabled) Assets.click.play(1);                              // Звук нажатия
                }
            }

            if (event.type == Input.TouchEvent.TOUCH_UP) {                                        // Если событие = UP, то продолжаем
                if (inBounds(event, 400, 460, 172, 172)) {                                        // НАЖАТА КНОПКА: ВЫХОД В МЕНЮ
                    game.setScreen(new MainMenuScreen(game));                                     // Переход в меню
                    Settings.save(game.getFileIO());                         // Сохраняем все настройки в файл
                    if (Settings.soundEnabled) Assets.click.play(1);                              // Звук нажатия
                    return;
                }
                if (inBounds(event, 15, 20, 120, 120)) {                                          // ВКЛ|ВЫКЛ МУЗЫКУ
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
        if (Settings.controlEnabled) g.drawPixmap(Assets.help1, 0, 0); else g.drawPixmap(Assets.help2, 0, 0);  // Отрисовка справочной инфы
        if (Settings.soundEnabled) g.drawPixmap(Assets.buttons, 15, 20, 0, 120, 122, 122);        // Отрисовка значка музыки(on/off)
        else g.drawPixmap(Assets.buttons, 14, 20, 120, 120, 122, 122);
    }                                                    // Вывод на экран

    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        return (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1);
    }   // Было ли касание в данной области

    @Override
    public void pause() {
        if (Assets.menu_media != null) {                                                          // Остановка музыки(если играла)
            Settings.save(game.getFileIO());                         // Сохраняем все настройки в файл
            if (Settings.soundEnabled) Assets.menu_media.stop();
        }
    }                                                                     // Остановка игры(Музыка)
    @Override
    public void resume() {
        if (Assets.menu_media != null) {                                                          // Возобновление музыки(если включена)
            if (Settings.soundEnabled) Assets.menu_media.play();
        }
    }                                                                    // Возврат в игру(Музыка)
    @Override
    public void dispose() { }                                                                     // Заглушка
}
