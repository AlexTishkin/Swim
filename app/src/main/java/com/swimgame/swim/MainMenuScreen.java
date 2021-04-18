package com.swimgame.swim;

import com.swimgame.base.Game;
import com.swimgame.base.Graphics;
import com.swimgame.base.Input;
import com.swimgame.base.Screen;
import java.util.List;

// Экран: главное меню

public class MainMenuScreen extends Screen {

    public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();                    // Считывание всех касаний экрана
        game.getInput().getKeyEvents();                                                           // Считывание всех нажатий клавиш(пул free)
        int len = touchEvents.size();                                                             // Длина массива касаний

        for (int i = 0; i < len; i++) {                                                           // Перебираем в массиве все касания экрана
            Input.TouchEvent event = touchEvents.get(i);                                          // event - очередное касание

            if (event.type == Input.TouchEvent.TOUCH_UP) {                                        // Если событие = UP, то продолжаем
                if (inBounds(event, 15, 20, 120, 120)) {                                          // ВКЛ|ВЫКЛ ЗВУК
                    Settings.soundEnabled = !Settings.soundEnabled;                               // Инвертируем значение музыки
                    if (Settings.soundEnabled) Assets.click.play(1);                              // Звук нажатия
                    if (Settings.soundEnabled) Assets.menu_media.play();                          // Музыка (on/off)
                    else Assets.menu_media.stop();
                }
                if (inBounds(event, 830, 20, 120, 120)) {                                         // НАЖАТА КНОПКА: ВЫХОД ИЗ ИГРЫ
                    Settings.save(game.getFileIO());                                              // Сохраняем все данные
                    System.exit(0);                                                               // Выход
                    return;
                }
                if (inBounds(event, 395, 330, 172, 172)) {                                        // НАЖАТА КНОПКА: НАЧАТЬ ИГРУ
                    game.setScreen(new GameScreen(game));                                         // Переход на экран: игра
                    if (Settings.soundEnabled) Assets.click.play(1);                              // Звук нажатия
                    return;
                }
                if (inBounds(event, 215, 340, 145, 145)) {       // Таблица рекордов
                    game.setScreen(new HighscoreScreen(game));                                    // Переход на экран: Таблица рекордов
                    if (Settings.soundEnabled) Assets.click.play(1);                              // Звук нажатия
                    return;
                }
                if (inBounds(event, 605, 340, 145, 145)) {                                        // НАЖАТА КНОПКА: ПОМОЩЬ
                    game.setScreen(new HelpScreen(game));                                         // Переход на экран: Справочная информация
                    if (Settings.soundEnabled) Assets.click.play(1);                              // Звук нажатия
                    return;
                }
            }
        }
    }                                                     // Обновление состояния экрана

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.background, 0, 0);                                                    // Отрисовка фона
        g.drawPixmap(Assets.logo, 150, 70);                                                       // Отрисовка логотипа
        g.drawPixmap(Assets.play_button, 415, 345);                                               // Отрисовка кнопки: Начать игру
        g.drawPixmap(Assets.records_button, 235, 355);                                            // Отрисовка кнопки: Рекорды
        g.drawPixmap(Assets.help_button, 625, 355);                                               // Отрисовка кнопки: Помощь
        g.drawPixmap(Assets.buttons, 830, 20, 0, 240, 120, 120);                                  // Отрисовка кнопки: Выход

        if (Settings.soundEnabled) g.drawPixmap(Assets.buttons, 15, 20, 0, 120, 122, 122);          // Отрисовка значка звука(on/off)
        else g.drawPixmap(Assets.buttons, 14, 20, 120, 120, 122, 122);

        //  if (Settings.musicEnabled)  g.drawPixmap(Assets.buttons, 15, 160, 0, 120, 122, 122);      // Отрисовка значка музыки(on/off)
        //  else g.drawPixmap(Assets.buttons, 14, 160, 120, 120, 122, 122);
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
            if (Settings.soundEnabled)
                Assets.menu_media.play();               // Возобновляем музыку
        }
    }                                                                    // Возврат в игру(Музыка)
    @Override
    public void dispose() {  }                                                                    // Заглушка
}

