package com.swimgame.swim.screens;

import com.swimgame.base.Game;
import com.swimgame.base.Graphics;
import com.swimgame.base.Input;
import com.swimgame.base.Screen;
import com.swimgame.swim.Assets;
import com.swimgame.swim.Settings;

import java.util.List;

// Экран: главное меню
public class MainMenuScreen extends Screen {

    public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    // Обновление состояния экрана
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len = touchEvents.size();
        // Перебираем в массиве все касания экрана
        for (int i = 0; i < len; i++) {
            // event - очередное касание
            Input.TouchEvent event = touchEvents.get(i);

            // Если событие = UP, то продолжаем
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                // ВКЛ|ВЫКЛ ЗВУК
                if (inBounds(event, 15, 20, 120, 120)) {
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if (Settings.soundEnabled) Assets.click.play(1);
                    if (Settings.soundEnabled) Assets.menu_media.play();
                    else Assets.menu_media.stop();
                }
                // НАЖАТА КНОПКА: НАЧАТЬ ИГРУ
                if (inBounds(event, 395, 330, 172, 172)) {
                    game.setScreen(new GameScreen(game));
                    if (Settings.soundEnabled) Assets.click.play(1);
                    return;
                }
                // Таблица рекордов
                if (inBounds(event, 215, 340, 145, 145)) {
                    game.setScreen(new HighscoreScreen(game));
                    if (Settings.soundEnabled) Assets.click.play(1);
                    return;
                }
                // НАЖАТА КНОПКА: ПОМОЩЬ
                if (inBounds(event, 605, 340, 145, 145)) {
                    game.setScreen(new HelpScreen(game));
                    if (Settings.soundEnabled) Assets.click.play(1);
                    return;
                }
            }
        }
    }

    @Override
    // Вывод на экран
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.background, 0, 0);
        g.drawPixmap(Assets.logo, 150, 70);
        g.drawPixmap(Assets.play_button, 415, 345);
        g.drawPixmap(Assets.records_button, 235, 355);
        g.drawPixmap(Assets.help_button, 625, 355);

        // Отрисовка значка звука(on/off)
        if (Settings.soundEnabled)
            g.drawPixmap(Assets.buttons, 15, 20, 0, 120, 122, 122);
        else g.drawPixmap(Assets.buttons, 14, 20, 120, 120, 122, 122);

    }

    // Было ли касание в данной области
    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        return (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1);
    }

    @Override
    public void pause() {
        if (Assets.menu_media != null) {
            Settings.save(game.getFileIO());
            if (Settings.soundEnabled) Assets.menu_media.stop();
        }
    }

    @Override
    public void resume() {
        if (Assets.menu_media != null) {
            if (Settings.soundEnabled)
                Assets.menu_media.play();
        }
    }

    @Override
    public void dispose() {
    }
}

