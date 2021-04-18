package com.swimgame.swim.screens;

import android.graphics.Color;

import com.swimgame.base.Game;
import com.swimgame.base.Graphics;
import com.swimgame.base.Input;
import com.swimgame.base.Screen;
import com.swimgame.swim.Assets;
import com.swimgame.swim.gameObjects.Coin;
import com.swimgame.swim.gameObjects.EnemyFish;
import com.swimgame.swim.Settings;
import com.swimgame.swim.gameObjects.World;

import java.util.List;

// В СЛЕД. ВЕРСИИ РЫБА - абстрактный класс [Наследники - mainFIsh и enemyFish]

// Экран : Игровой процесс

public class GameScreen extends Screen {
    enum GameState {                                    // Состояния
        Ready,                                          // Готовность
        Running,                                        // Игра
        Paused,                                         // Пауза
        GameOver                                        // Игра окончена
    }

    private boolean upButtonClicked, downButtonClicked; // Нажаты кнопки вверх/вниз
    private boolean upButtonClickedOk, downButtonClickedOk;  // Можно двигаться

    GameState state = GameState.Ready;                 // Хранение текущего состояния экрана(default =  готовность)
    World world;                                       // Экземпляр класса World => ЛОГИКА НАШЕГО ПОДВОДНОГО МИРА
    int oldScore = 0;                                  // Текущий набранные очки
    String score = "0";                                // Строка для отображения текущих очков
    String coinsCount = "0";                          // Строка для отображения количества собранных монет

    int yTouch;                                        // Координата y для реализации контроллера

    public GameScreen(Game game) {
        super(game);                                   // Конструктор родительского класса
        score = String.format("%05d", 0);
        world = new World();                           // Создание экземпляра класса логики игры
        upButtonClicked = false;                       // Клавиши по умолчанию не нажаты[Для вьюхи]
        downButtonClicked = false;
        upButtonClickedOk = false;                     // Можно двигаться
        downButtonClickedOk = false;
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();    // Массив касаний экрана
        game.getInput().getKeyEvents();                                           // Массив нажатий клавиш
        if (state == GameState.Ready)
            updateReady(touchEvents, deltaTime);       // Обновление экрана в зависимости от состояния
        if (state == GameState.Running) updateRunning(touchEvents, deltaTime);
        if (state == GameState.Paused) updatePaused(touchEvents);
        if (state == GameState.GameOver) updateGameOver(touchEvents, deltaTime);
    }

    private void updateReady(List<Input.TouchEvent> touchEvents, float deltaTime) {
        if (touchEvents.size() > 0 && touchEvents.get(0).y > 80) {
            touchEvents = game.getInput().getTouchEvents();                     // ЧТОБЫ СРАЗУ НЕ УПРАВЛЯТЬ РЫБКОЙ
            if (Settings.soundEnabled) Assets.click.play(1);
            world.enemyMode = true;
            state = GameState.Running;
        }
        world.update(deltaTime);

    }                     // Обновление экрана(ГОТОВНОСТЬ) [State 1]   [OK]

    ///////////////////////////////////////////////...................[ETA TOLKO].............................................////////////////////////////////


    private void updateRunning(List<Input.TouchEvent> touchEvents, float deltaTime) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);         // Контроллер игры
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                if (yTouch < 80 && event.y < 80) {                 // МЕНЮ
                    if (event.x < 90) {           // Если нажата клавиша ПАУЗЫ -> состояние ПАУЗА
                        if (Settings.soundEnabled) Assets.click.play(1);
                        state = GameState.Paused;
                        return;
                    }
                    if (event.x > 90 && event.x < 170) {    // Музыка вкл/выкл
                        Settings.soundEnabled = !Settings.soundEnabled;                               // Инвертируем значение музыки
                        if (Settings.soundEnabled)
                            Assets.game_media.play();                          // Музыка (on/off)
                        else Assets.game_media.stop();
                        return;
                    }

                } else if (!Settings.controlEnabled) {
                    //** Контроллер игры[SWIM]                   ПЕРВЫЙ ВИД УПРАВЛЕНИЯ
                    if (event.y - yTouch > 0) {                         // можн еще добавить звук бульк
                        world.mainFish.toDown();
                    } else if (yTouch - event.y > 0) {
                        world.mainFish.toUp();
                    }
                }
                upButtonClicked = false;
                downButtonClicked = false;
            }
            if (event.type == Input.TouchEvent.TOUCH_DOWN) {
                yTouch = event.y;
                ////////////////////////////////////// КОНТРОЛЛЕР -> Кнопки ВТОРОЙ ВИД УПРАВЛЕНИЯ [DEFAULT]
                if (Settings.controlEnabled) {
                    if (yTouch > 80) {
                        if (event.x < 150) {
                            if (yTouch < 360) {
                                if (Settings.soundEnabled) Assets.click.play(1);
                                upButtonClickedOk = world.mainFish.toUp();
                                upButtonClicked = true;
                            } else {
                                if (Settings.soundEnabled) Assets.click.play(1);
                                downButtonClickedOk = world.mainFish.toDown();
                                downButtonClicked = true;
                            }
                        }
                    }
                }
                ///////////////////
            }
        }


        world.update(deltaTime);                                                       // Обновление мира
        if (world.gameOver) {                                                           // Если игра закончена, то к состоянию GameOver
            // if(Settings.soundEnabled) Звук_проигрыша.play()                         // КАКОЙ-НИБУДЬ ЗВУК ПРОИГРЫША =(
            state = GameState.GameOver;
        }// Обновление экрана(ИГРА)

        if (oldScore != world.score) {                                                  // Если отличаются очки, обновить
            oldScore = world.score;
            score = String.format("%05d", oldScore);                                     // 5 нулей -> макс 99 999
            //if(Settings.soundEnabled) Звук_сжирания_бонуса.play                            // СОЖРАТЬ РЫБУ -/ Звук
        }

        coinsCount = String.format("x%02d", world.coinsCount);
    }  // Обновление экрана(ИГРА!!!)  [State 2] [ПОЧТИ РЕАЛИЗОВАНО!!!] !!!!!!!!!!!!!!!!!!!!!!!!


    ////***********************************************************************************************************/////////////
    private void drawRunningUI() {                                     // Визуализация пользовательского интерфейса(Состояние: Игра)
        Graphics g = game.getGraphics();                                      // [NOOOOO =(]


        g.drawPixmap(Assets.pause_button, 10, 0);                             // Пауза

        if (Settings.soundEnabled)
            g.drawPixmap(Assets.mini_sound_button, 92, 0, 0, 0, 80, 80);          // Музыка on
        else g.drawPixmap(Assets.mini_sound_button, 92, 0, 80, 0, 80, 80);          // Музыка off


        g.drawText(score, 2, Color.WHITE);                   // Вывод количества очков

        //g.drawPixmap(Assets.coin, g.getWidth()/2 -120, 0);   // Иконка монет

        if (world.coinsState == 1) g.drawPixmap(Assets.coin, g.getWidth() / 2 - 120, 0);
        else if (world.coinsState == 2) g.drawPixmap(Assets.coin2, g.getWidth() / 2 - 120, 0);
        else g.drawPixmap(Assets.coin3, g.getWidth() / 2 - 120, 0);

        g.drawText(coinsCount, 3, Color.rgb(255, 252, 0));   // Вывод количества монет



        /*/ Вспомогательные полосы
        g.drawLine(0, 80, 960, 80, Color.BLACK);            // ОТДЕЛЕНИЕ МЕНЮШКИ              [ПОСЛЕ УБРАТЬ]
        g.drawLine(710, 0, 710, 80, Color.BLACK);           // Отделение табла очков          [ПОСЛЕ УБРАТЬ]
        g.drawLine(90, 0, 90, 80, Color.BLACK);             // Отделение паузы                [ПОСЛЕ УБРАТЬ]
        g.drawLine(170, 0, 170, 80, Color.BLACK);           // Отделение регулятора громкости [ПОСЛЕ УБРАТЬ]
        g.drawLine(0, 180, 960, 180, Color.BLACK);          // Вспомогательные полосы         [ПОСЛЕ УБРАТЬ]
        g.drawLine(0, 280, 960, 280, Color.BLACK);
        g.drawLine(0, 379, 960, 379, Color.BLACK);
        g.drawLine(0, 480, 960, 480, Color.BLACK);
        g.drawLine(0, 580, 960, 580, Color.BLACK);
        // // // // // // // // // /*/


        // Вьюха для отображения кнопок
        if (Settings.controlEnabled) {
            if (!upButtonClicked) g.drawPixmap(Assets.up_button, 10, 90);
            else {
                if (upButtonClickedOk) g.drawPixmap(Assets.up_click1_button, 10, 90);
                else g.drawPixmap(Assets.up_click2_button, 10, 90);
            }

            if (!downButtonClicked) g.drawPixmap(Assets.down_button, 10, 370);
            else {
                if (downButtonClickedOk) g.drawPixmap(Assets.down_click1_button, 10, 370);
                else g.drawPixmap(Assets.down_click2_button, 10, 370);
            }
        }
        ////


    }


    private void drawWorld(World world) {
        Graphics g = game.getGraphics();

        for (int i = 0; i < 9; i++) {                       // Отрисовка фона
            if (world.backgrounds_x[i] <= 960 && world.backgrounds_x[i] >= -960)
                g.drawPixmap(Assets.backgrounds[i], world.backgrounds_x[i], 0);
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
    }  // Отрисовка вражеской рыбки

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void updatePaused(List<Input.TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {                                       // В зависимости от клавиш переход в состояние
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {

                if (inBounds(event, 111, 300, 172, 172)) {                     // Выход в главное меню
                    if (Settings.soundEnabled) Assets.click.play(1);
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
                if (inBounds(event, 394, 300, 172, 172)) {                       // Нажата кнопка продолжить
                    if (Settings.soundEnabled) Assets.click.play(1);
                    state = GameState.Running;
                    return;
                }
                if (inBounds(event, 677, 300, 172, 172)) {                       // Выход из игры
                    if (Settings.soundEnabled) Assets.click.play(1);
                    Settings.save(game.getFileIO());
                    System.exit(0);
                    return;
                }
            }
        }
    }                   // Обновление экрана(ПАУЗА) [State 3]   [OK]

    private void updateGameOver(List<Input.TouchEvent> touchEvents, float deltaTime) {                       // Если кнопка нажата -> выход в меню
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                if (inBounds(event, 225, 345, 172, 172)) {
                    game.setScreen(new GameScreen(game)); // Начать новую игру
                    if (Settings.soundEnabled) Assets.click.play(1);
                    return;
                }
                if (inBounds(event, 562, 345, 172, 172)) {
                    if (Settings.soundEnabled) Assets.click.play(1);
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }


            }
        }
        world.enemyMode = false;
        world.gameOver = false;
        world.update(deltaTime);
    }   // Обновление экрана(КОНЕЦ ИГРЫ) [State 4] [OK]

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        drawWorld(world);                                                                  // Отрисовка мира
        if (state == GameState.Ready)
            drawReadyUI();                                   // Отрисовка в зависимости от состояния
        if (state == GameState.Running) drawRunningUI();
        if (state == GameState.Paused) drawPausedUI();
        if (state == GameState.GameOver) drawGameOverUI();
        // Вывод кол-ва набранных очков
    }

    private void drawReadyUI() {                                      // Визуализация пользовательского интерфейса(Состояние: Готовность)  [OK]
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.ready, 245, 80);
    }

    private void drawPausedUI() {                                      // Визуализация пользовательского интерфейса(Состояние: Пауза) [OK]
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.pause, 239, 67);
        g.drawPixmap(Assets.back_button, 111, 300);
        g.drawPixmap(Assets.play_button, 394, 300);
        g.drawPixmap(Assets.ex_button, 677, 300);
    }

    private void drawGameOverUI() {                                    // Визуализация пользовательского интерфейса(Состояние: Конец игры) [OK]
        Graphics g = game.getGraphics();

        for (int i = 0; i < 9; i++) {
            if (world.backgrounds_x[i] <= 960 && world.backgrounds_x[i] >= -960)// Отрисовка фона
                g.drawPixmap(Assets.backgrounds[i], world.backgrounds_x[i], 0);
        }

        //g.drawText(score, 100, 100, Color.BLUE);
        score = String.valueOf(Integer.valueOf(score)); // Избавление от нулей (Да, извращенство)
        g.drawText(score, 1, Color.WHITE);

        //g.drawPixmap(Assets.gameOver, 63, 135);
        g.drawPixmap(Assets.repeat_button, 225, 345);
        g.drawPixmap(Assets.back_button, 562, 345);
    }

    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        return (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1);
    }   // Было ли касание в данной области

    @Override
    public void pause() {                                            // Активность ставится на паузу ИЛИ игровой экран заменяется на другой{save settings}
        if (Assets.game_media != null) {                            // Остановка музыки(если играла)
            if (Settings.soundEnabled) Assets.game_media.stop();
        }

        if (state == GameState.Running)
            state = GameState.Paused;                                // Если состояние = ИГРА, то заменим на состояние = ПАУЗА{Закрытие активности}
        if (world.gameOver) {                                        // Если игра в состоянии GameOver
            Settings.addScore(world.score);                          // то добавляем наши очки в таблицу рекордов
            Settings.save(game.getFileIO());                         // Сохраняем все настройки в файл
        }
    }

    @Override
    public void resume() {
        if (Assets.game_media != null) {                           // Возобновление музыки(если включена)
            if (Settings.soundEnabled)
                Assets.game_media.play();                          // Возобновляем музыку
        }
    }

    @Override
    public void dispose() {
    }

}
