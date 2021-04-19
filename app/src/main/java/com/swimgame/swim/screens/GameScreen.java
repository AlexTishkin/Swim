package com.swimgame.swim.screens;

import android.graphics.Color;

import com.swimgame.base.Game;
import com.swimgame.base.Graphics;
import com.swimgame.base.Input;
import com.swimgame.base.Screen;
import com.swimgame.swim.Assets;
import com.swimgame.swim.gameobjects.Coin;
import com.swimgame.swim.gameobjects.EnemyFish;
import com.swimgame.swim.Settings;
import com.swimgame.swim.gameobjects.World;

import java.util.List;

// Экран : Игровой процесс
public class GameScreen extends Screen {
    enum GameState {
        Ready,
        Running,
        Paused,
        GameOver
    }

    // Нажаты кнопки вверх/вниз
    private boolean upButtonClicked, downButtonClicked;
    // Можно двигаться
    private boolean canUpButtonClickedOk, canDownButtonClickedOk;

    // Хранение текущего состояния экрана(default =  готовность)
    GameState state = GameState.Ready;
    // Экземпляр класса World => ЛОГИКА НАШЕГО ПОДВОДНОГО МИРА
    World world;

    // Текущие набранные очки
    int oldScore = 0;
    // Строки для отображения текущих очков и количества собранных монет
    String score = "0", coinsCount = "0";

    // Координата y для реализации контроллера
    int yTouch;

    public GameScreen(Game game) {
        super(game);
        score = String.format("%05d", 0);
        world = new World();

        // Кнопки вверх/вниз (Можно двигаться)
        upButtonClicked = false;
        downButtonClicked = false;
        canUpButtonClickedOk = false;
        canDownButtonClickedOk = false;
    }

    @Override
    // Контроллер (Обновление состояния логического мира)
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        // Обновление экрана в зависимости от состояния
        if (state == GameState.Ready) updateReady(touchEvents, deltaTime);
        if (state == GameState.Running) updateRunning(touchEvents, deltaTime);
        if (state == GameState.Paused) updatePaused(touchEvents);
        if (state == GameState.GameOver) updateGameOver(touchEvents, deltaTime);
    }

    // Контроллер экрана <Готовность>
    private void updateReady(List<Input.TouchEvent> touchEvents, float deltaTime) {
        if (touchEvents.size() > 0 && touchEvents.get(0).y > 80) {
            touchEvents = game.getInput().getTouchEvents();
            if (Settings.soundEnabled) Assets.click.play(1);
            world.enemyMode = true;
            state = GameState.Running;
        }
        world.update(deltaTime);
    }

    // Контроллер экрана <Пауза>
    private void updatePaused(List<Input.TouchEvent> touchEvents) {
        int len = touchEvents.size();
        // В зависимости от клавиш переход в состояние
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                // Выйти в главное меню
                if (inBounds(event, 111, 300, 172, 172)) {
                    if (Settings.soundEnabled) Assets.click.play(1);
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
                // Продолжить игру
                if (inBounds(event, 394, 300, 172, 172)) {
                    if (Settings.soundEnabled) Assets.click.play(1);
                    state = GameState.Running;
                    return;
                }
                // Выйти из игры
                if (inBounds(event, 677, 300, 172, 172)) {
                    if (Settings.soundEnabled) Assets.click.play(1);
                    Settings.save(game.getFileIO());
                    System.exit(0);
                    return;
                }
            }
        }
    }

    // Контроллер экрана <Конец игры>
    private void updateGameOver(List<Input.TouchEvent> touchEvents, float deltaTime) {
        int len = touchEvents.size();
        // В зависимости от клавиш переход в состояние
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                // Играть еще раз
                if (inBounds(event, 225, 345, 172, 172)) {
                    game.setScreen(new GameScreen(game)); // Начать новую игру
                    if (Settings.soundEnabled) Assets.click.play(1);
                    return;
                }
                // Выйти в главное меню
                if (inBounds(event, 562, 345, 172, 172)) {
                    if (Settings.soundEnabled) Assets.click.play(1);
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
        // Прокрутка фона после проигрыша
        world.enemyMode = false;
        world.gameOver = false;
        world.update(deltaTime);
    }

    // Контроллер экрана <Игровой процесс> (Состояние логического игрового мира по нажатиям) РЕФАКТОООООООР!!!!!!!
    private void updateRunning(List<Input.TouchEvent> touchEvents, float deltaTime) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            // Контроллер игры
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                // Верхнее меню
                if (yTouch < 80 && event.y < 80) {
                    // Пауза
                    if (event.x < 90) {
                        if (Settings.soundEnabled) Assets.click.play(1);
                        state = GameState.Paused;
                        return;
                    }
                    // Музыка вкл/выкл
                    if (event.x > 90 && event.x < 170) {
                        Settings.soundEnabled = !Settings.soundEnabled;
                        if (Settings.soundEnabled) Assets.game_media.play();
                        else Assets.game_media.stop();
                        return;
                    }

                }
                // Управление 2 - Тач
                else if (!Settings.controlEnabled) {
                    if (event.y - yTouch > 0) world.mainFish.toDown();
                    else if (yTouch - event.y > 0) world.mainFish.toUp();
                }
                upButtonClicked = false;
                downButtonClicked = false;
            }
            // Управление 1 - кнопки (default)
            if (event.type == Input.TouchEvent.TOUCH_DOWN) {
                yTouch = event.y;
                if (Settings.controlEnabled) {
                    if (yTouch > 80) {
                        if (event.x < 150) {
                            if (yTouch < 360) {
                                if (Settings.soundEnabled) Assets.click.play(1);
                                canUpButtonClickedOk = world.mainFish.toUp();
                                upButtonClicked = true;
                            } else {
                                if (Settings.soundEnabled) Assets.click.play(1);
                                canDownButtonClickedOk = world.mainFish.toDown();
                                downButtonClicked = true;
                            }
                        }
                    }
                }
            }
        }

        // Обновление мира
        world.update(deltaTime);
        // Если игра закончена, то к состоянию GameOver
        if (world.gameOver) state = GameState.GameOver;

        // Если отличаются очки, обновить
        if (oldScore != world.score.getScore()) {
            oldScore = world.score.getScore();
            score = String.format("%05d", oldScore);
        }

        coinsCount = String.format("%02d", world.score.coinsCount);
    }

    @Override
    public void present(float deltaTime) {
        drawWorld(world);
        // Отрисовка в зависимости от состояния
        if (state == GameState.Ready) drawReadyUI();
        if (state == GameState.Running) drawRunningUI();
        if (state == GameState.Paused) drawPausedUI();
        if (state == GameState.GameOver) drawGameOverUI();
    }

    // Визуализация экрана <Готовность>
    private void drawReadyUI() {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.ready, 245, 80);
    }

    // Визуализация экрана <Пауза>
    private void drawPausedUI() {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.pause, 239, 67);
        g.drawPixmap(Assets.back_button, 111, 300);
        g.drawPixmap(Assets.play_button, 394, 300);
        g.drawPixmap(Assets.ex_button, 677, 300);
    }

    // Визуализация экрана <Конец игры>
    private void drawGameOverUI() {
        Graphics g = game.getGraphics();

        // Отрисовка фона <Костыль> (?)
        for (int i = 0; i < 9; i++) {
            if (world.background.backgrounds_x[i] <= 960 && world.background.backgrounds_x[i] >= -960)
                g.drawPixmap(Assets.backgrounds[i], world.background.backgrounds_x[i], 0);
        }

        // Избавление от нулей (Да, плохо)
        score = String.valueOf(Integer.valueOf(score));
        g.drawText(score, 1, Color.WHITE);

        g.drawPixmap(Assets.repeat_button, 225, 345);
        g.drawPixmap(Assets.back_button, 562, 345);
    }

    // Визуализация экрана <Игровой процесс>
    private void drawRunningUI() {
        Graphics g = game.getGraphics();
        // drawGridLayout(g);
        drawTopButtons(g);
        drawCoins(g);
        drawScore(g);
        if (Settings.controlEnabled)
            drawControlButtons(g);
    }

    // Отобразить верхние кнопки для  игрового экрана
    private void drawTopButtons(Graphics g) {
        g.drawPixmap(Assets.pause_button, 10, 0);
        if (Settings.soundEnabled)
            g.drawPixmap(Assets.mini_sound_button, 92, 0, 0, 0, 80, 80);
        else g.drawPixmap(Assets.mini_sound_button, 92, 0, 80, 0, 80, 80);
    }

    // Вывод количества монет
    private void drawCoins(Graphics g) {
        if (world.score.coinsState == 1) g.drawPixmap(Assets.coin, g.getWidth() / 2 - 120, 0);
        else if (world.score.coinsState == 2) g.drawPixmap(Assets.coin2, g.getWidth() / 2 - 120, 0);
        else g.drawPixmap(Assets.coin3, g.getWidth() / 2 - 120, 0);
        g.drawText(coinsCount, 3, Color.rgb(255, 252, 0));
    }

    // Вывод количества очков
    private void drawScore(Graphics g) {
        g.drawText(score, 2, Color.WHITE);
    }

    // Отобразить кнопки для управления
    private void drawControlButtons(Graphics g) {
        if (!upButtonClicked) g.drawPixmap(Assets.up_button, 10, 90);
        else
            g.drawPixmap(canUpButtonClickedOk ? Assets.up_click1_button : Assets.up_click2_button, 10, 90);

        if (!downButtonClicked) g.drawPixmap(Assets.down_button, 10, 370);
        else
            g.drawPixmap(canDownButtonClickedOk ? Assets.down_click1_button : Assets.down_click2_button, 10, 370);
    }

    // Показать разметку
    private void drawGridLayout(Graphics g) {
        // Меню
        g.drawLine(0, 80, 960, 80, Color.BLACK);
        // Очки
        g.drawLine(710, 0, 710, 80, Color.BLACK);
        // Пауза
        g.drawLine(90, 0, 90, 80, Color.BLACK);
        // Музыка вкл / выкл
        g.drawLine(170, 0, 170, 80, Color.BLACK);
        // Вспомогательные полосы
        g.drawLine(0, 180, 960, 180, Color.BLACK);
        g.drawLine(0, 280, 960, 280, Color.BLACK);
        g.drawLine(0, 379, 960, 379, Color.BLACK);
        g.drawLine(0, 480, 960, 480, Color.BLACK);
        g.drawLine(0, 580, 960, 580, Color.BLACK);
    }

    // Отрисовка мира
    private void drawWorld(World world) {
        Graphics g = game.getGraphics();

        for (int i = 0; i < 9; i++) {                       // Отрисовка фона
            if (world.background.backgrounds_x[i] <= 960 && world.background.backgrounds_x[i] >= -960)
                g.drawPixmap(Assets.backgrounds[i], world.background.backgrounds_x[i], 0);
        }

        g.drawPixmap(Assets.person[world.mainFish.frame], world.mainFish.x,
                world.mainFish.line * 100 - 7, 0, 0, 128, 93); // Отрисовка главного героя

        if (world.mainFish.isProtectedViaBubble)
            g.drawPixmap(Assets.protectBubble, world.mainFish.x - 5,
                    world.mainFish.line * 100 - 40, 0, 0, 140, 140); // Отрисовка защитного пузырька. если есть

        // Отрисовка части монет сзади
        for (int i = 1; i < 10; i += 2)
            if (!world.coins[i].isChecked) {
                // Разноцветные шарики
                if (world.coins[i].color == Coin.COLOR_YELLOW)
                    g.drawPixmap(Assets.coin, world.coins[i].x - 10, world.coins[i].line * 100, 0, 0, 80, 80);
                else if (world.coins[i].color == Coin.COLOR_GREEN)
                    g.drawPixmap(Assets.coin2, world.coins[i].x - 10, world.coins[i].line * 100, 0, 0, 80, 80);
                else if (world.coins[i].color == Coin.COLOR_RED)
                    g.drawPixmap(Assets.coin3, world.coins[i].x - 10, world.coins[i].line * 100, 0, 0, 80, 80);
            }


        if (world.enemyMode) // Чтобы при ожидании не было рыбок
            for (int i = 0; i < World.ENEMY_COUNT; i++) {  // Отрисовка врагов
                if (world.topEnemyFish[i].isVisible)
                    drawEnemyFish(world.topEnemyFish[i]);         // Отрисовка верхних врагов
                if (world.bottomEnemyFish[i].isVisible)
                    drawEnemyFish(world.bottomEnemyFish[i]);        // Отрисовка нижних врагов
            }


        // Отрисовка части монет спереди
        for (int i = 0; i < 10; i += 2)
            if (!world.coins[i].isChecked) {
                // Разноцветные шарики
                if (world.coins[i].color == Coin.COLOR_YELLOW)
                    g.drawPixmap(Assets.coin, world.coins[i].x - 10, world.coins[i].line * 100, 0, 0, 80, 80);
                else if (world.coins[i].color == Coin.COLOR_GREEN)
                    g.drawPixmap(Assets.coin2, world.coins[i].x - 10, world.coins[i].line * 100, 0, 0, 80, 80);
                else if (world.coins[i].color == Coin.COLOR_RED)
                    g.drawPixmap(Assets.coin3, world.coins[i].x - 10, world.coins[i].line * 100, 0, 0, 80, 80);
            }


    }

    // Отрисовка вражеской рыбки
    public void drawEnemyFish(EnemyFish enemyFish) {
        Graphics g = game.getGraphics();
        //  if (enemyFish != null && enemyFish.x < 960 && enemyFish.x > -128) { // Проверка на вхождение в экранную область
        if (enemyFish.type == EnemyFish.TYPE_ENEMY_FISH) {
            int color = enemyFish.color;

            switch (color) { // Отрисовка рыбок в зависимости от цвета
                case EnemyFish.COLOR_RED:
                    g.drawPixmap(Assets.enemyRed[enemyFish.frame], enemyFish.x - 6, enemyFish.line * 100, 0, 0, 120, 87);
                    break;
                case EnemyFish.COLOR_GREEN:
                    g.drawPixmap(Assets.enemyGreen[enemyFish.frame], enemyFish.x - 6, enemyFish.line * 100, 0, 0, 120, 87);
                    break;
                case EnemyFish.COLOR_BLUE:
                    g.drawPixmap(Assets.enemyBlue[enemyFish.frame], enemyFish.x - 6, enemyFish.line * 100, 0, 0, 120, 87);
                    break;
            }
        } else if (enemyFish.type == EnemyFish.TYPE_ENEMY_SHARK)
            g.drawPixmap(Assets.enemy2[enemyFish.frame], enemyFish.x, enemyFish.line * 100, 0, 0, 240, 150);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Было ли касание в данной области
    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        return (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1);
    }

    @Override
    // Активность ставится на паузу ИЛИ игровой экран заменяется на другой (Save settings)
    public void pause() {
        if (Assets.game_media != null && Settings.soundEnabled)
            Assets.game_media.stop();

        // Если состояние = ИГРА, то заменим на состояние = ПАУЗА (Закрытие активности)
        if (state == GameState.Running)
            state = GameState.Paused;

        // Если игра в состоянии GameOver
        if (world.gameOver) {
            // Добавляем наши очки в таблицу рекордов
            Settings.addScore(world.score.getScore());
            // Сохраняем все настройки в файл
            Settings.save(game.getFileIO());
        }
    }

    @Override
    public void resume() {
        if (Assets.game_media != null && Settings.soundEnabled)
            Assets.game_media.play();
    }

    @Override
    public void dispose() {
    }

}
