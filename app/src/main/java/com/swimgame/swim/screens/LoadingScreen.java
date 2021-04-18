package com.swimgame.swim.screens;

import com.swimgame.base.Game;
import com.swimgame.base.Graphics;
import com.swimgame.base.Pixmap;
import com.swimgame.base.Screen;
import com.swimgame.swim.Assets;
import com.swimgame.swim.Settings;

// Загрузка необходимых ресурсов

public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();                                                                // Загрузка ресурсов:
        Assets.background = g.newPixmap("background.png", Graphics.PixmapFormat.RGB565);                //  * Фоновая картинка

        Assets.backgrounds = new Pixmap[9];
        Assets.backgrounds[0] = g.newPixmap("back/background1.png", Graphics.PixmapFormat.RGB565);      //  * Фоновые изображения
        Assets.backgrounds[1] = g.newPixmap("back/background2.png", Graphics.PixmapFormat.RGB565);
        Assets.backgrounds[2] = g.newPixmap("back/background3.png", Graphics.PixmapFormat.RGB565);
        Assets.backgrounds[3] = g.newPixmap("back/background4.png", Graphics.PixmapFormat.RGB565);
        Assets.backgrounds[4] = g.newPixmap("back/background5.png", Graphics.PixmapFormat.RGB565);
        Assets.backgrounds[5] = g.newPixmap("back/background6.png", Graphics.PixmapFormat.RGB565);
        Assets.backgrounds[6] = g.newPixmap("back/background7.png", Graphics.PixmapFormat.RGB565);
        Assets.backgrounds[7] = g.newPixmap("back/background8.png", Graphics.PixmapFormat.RGB565);
        Assets.backgrounds[8] = g.newPixmap("back/background9.png", Graphics.PixmapFormat.RGB565);

        Assets.fon = g.newPixmap("fon.png", Graphics.PixmapFormat.RGB565);                              //  * Фон
        Assets.logo = g.newPixmap("logo.png", Graphics.PixmapFormat.ARGB4444);                          //  * Логотип
        Assets.buttons = g.newPixmap("buttons.png", Graphics.PixmapFormat.ARGB4444);                    //  * Кнопки
        Assets.help_button = g.newPixmap("help_button.png", Graphics.PixmapFormat.ARGB4444);            //  * Кнопка: помощь
        Assets.play_button = g.newPixmap("play_button.png", Graphics.PixmapFormat.ARGB4444);            //  * Кнопка: начать игру
        Assets.pause_button = g.newPixmap("pause_button.png", Graphics.PixmapFormat.ARGB4444);          //  * Кнопка паузы в игре
        Assets.ok_button = g.newPixmap("ok_button.png", Graphics.PixmapFormat.ARGB4444);                //  * Кнопка: ок
        Assets.ex_button = g.newPixmap("ex_button.png", Graphics.PixmapFormat.ARGB4444);                //  * Кнопка: выход
        Assets.repeat_button = g.newPixmap("repeat_button.png", Graphics.PixmapFormat.ARGB4444);        //  * Кнопка: начать заново
        Assets.back_button = g.newPixmap("back_button.png", Graphics.PixmapFormat.ARGB4444);            //  * Кнопка: Назад
        Assets.records_button = g.newPixmap("records_button.png", Graphics.PixmapFormat.ARGB4444);      //  * Кнопка: рекорды
        Assets.help1 = g.newPixmap("help_screen1.png", Graphics.PixmapFormat.ARGB4444);                 //  * Экран помощи 1
        Assets.help2 = g.newPixmap("help_screen2.png", Graphics.PixmapFormat.ARGB4444);                 //  * Экран помощи 2
        Assets.highscores_label = g.newPixmap("highscores_label.png", Graphics.PixmapFormat.ARGB4444);  //  * Метка: Рекорды
        Assets.highscores3 = g.newPixmap("highscores3.png", Graphics.PixmapFormat.ARGB4444);            //  * Значки рекордов(123)
        Assets.ready = g.newPixmap("ready.png", Graphics.PixmapFormat.ARGB4444);                        //  * Состояние: Готовность
        Assets.pause = g.newPixmap("pause.png", Graphics.PixmapFormat.ARGB4444);                        //  * Состояние: Пауза
        Assets.gameOver = g.newPixmap("gameover.png", Graphics.PixmapFormat.ARGB4444);                  //  * Состояние: Конец игры

        Assets.up_button = g.newPixmap("up_button.png", Graphics.PixmapFormat.ARGB4444);                //  * Кнопка: вверх
        Assets.up_click1_button = g.newPixmap("up_click1_button.png", Graphics.PixmapFormat.ARGB4444);    //  * Кнопка: вверх - нажатие
        Assets.up_click2_button = g.newPixmap("up_click2_button.png", Graphics.PixmapFormat.ARGB4444);    //  * Кнопка: вверх - нажатие
        Assets.down_button = g.newPixmap("down_button.png", Graphics.PixmapFormat.ARGB4444);            //  * Кнопка: вниз
        Assets.down_click1_button = g.newPixmap("down_click1_button.png", Graphics.PixmapFormat.ARGB4444);  //  * Кнопка: вниз  - нажатие
        Assets.down_click2_button = g.newPixmap("down_click2_button.png", Graphics.PixmapFormat.ARGB4444);  //  * Кнопка: вниз  - нажатие

        Assets.click = game.getAudio().newSound("click.ogg");                                           //  * Звук клика
        Assets.menu_media = game.getAudio().newMusic("menu_media.ogg");                                 //  * Музыка: меню
        Assets.menu_media.setLooping(true);                                                             // Включить повтор музыки
        Assets.game_media = game.getAudio().newMusic("game_media.ogg");                                 //  * Музыка: игра
        Assets.game_media.setLooping(true);                                                             // Включить повтор музыки
        Assets.font = g.newFont("font.ttf");                                                            //  * Шрифт

        Assets.mini_sound_button = g.newPixmap("mini_sound_button.png", Graphics.PixmapFormat.ARGB4444);// Свич звука в игре

        Assets.coin = g.newPixmap("coin.png", Graphics.PixmapFormat.ARGB4444);                          // * Монетка
        Assets.coin2 = g.newPixmap("coin2.png", Graphics.PixmapFormat.ARGB4444);                        // * Монетка2
        Assets.coin3 = g.newPixmap("coin3.png", Graphics.PixmapFormat.ARGB4444);                        // * Монетка3

        Assets.protectBubble = g.newPixmap("protect_bubble.png", Graphics.PixmapFormat.ARGB4444);      // * Пузырек защита

        // Главный герой - рыбка
        Assets.person = new Pixmap[6];
        Assets.person[0] = g.newPixmap("person/1.png", Graphics.PixmapFormat.ARGB4444);
        Assets.person[1] = g.newPixmap("person/2.png",Graphics.PixmapFormat.ARGB4444);
        Assets.person[2] = g.newPixmap("person/3.png", Graphics.PixmapFormat.ARGB4444);
        Assets.person[3] = g.newPixmap("person/4.png",Graphics.PixmapFormat.ARGB4444);
        Assets.person[4] = g.newPixmap("person/5.png", Graphics.PixmapFormat.ARGB4444);
        Assets.person[5] = g.newPixmap("person/6.png",Graphics.PixmapFormat.ARGB4444);


        Assets.enemyRed = new Pixmap[6];                                                                  // Враг
        Assets.enemyRed[0] = g.newPixmap("enemyRed/1.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemyRed[1] = g.newPixmap("enemyRed/2.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemyRed[2] = g.newPixmap("enemyRed/3.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemyRed[3] = g.newPixmap("enemyRed/4.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemyRed[4] = g.newPixmap("enemyRed/5.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemyRed[5] = g.newPixmap("enemyRed/6.png",Graphics.PixmapFormat.ARGB4444);

        Assets.enemyGreen = new Pixmap[6];                                                                  // Враг
        Assets.enemyGreen[0] = g.newPixmap("enemyGreen/1.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemyGreen[1] = g.newPixmap("enemyGreen/2.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemyGreen[2] = g.newPixmap("enemyGreen/3.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemyGreen[3] = g.newPixmap("enemyGreen/4.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemyGreen[4] = g.newPixmap("enemyGreen/5.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemyGreen[5] = g.newPixmap("enemyGreen/6.png",Graphics.PixmapFormat.ARGB4444);

        Assets.enemyBlue = new Pixmap[6];                                                                  // Враг
        Assets.enemyBlue[0] = g.newPixmap("enemyBlue/1.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemyBlue[1] = g.newPixmap("enemyBlue/2.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemyBlue[2] = g.newPixmap("enemyBlue/3.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemyBlue[3] = g.newPixmap("enemyBlue/4.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemyBlue[4] = g.newPixmap("enemyBlue/5.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemyBlue[5] = g.newPixmap("enemyBlue/6.png",Graphics.PixmapFormat.ARGB4444);

        Assets.enemy2 = new Pixmap[6];                                                                  // Враг -> Акула
        Assets.enemy2[0] = g.newPixmap("enemy2/1.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemy2[1] = g.newPixmap("enemy2/2.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemy2[2] = g.newPixmap("enemy2/3.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemy2[3] = g.newPixmap("enemy2/4.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemy2[4] = g.newPixmap("enemy2/5.png",Graphics.PixmapFormat.ARGB4444);
        Assets.enemy2[5] = g.newPixmap("enemy2/6.png",Graphics.PixmapFormat.ARGB4444);

        Settings.load(game.getFileIO());                                                                // Загрузка настроек из файла
        game.setScreen(new MainMenuScreen(game));                                                       // Переход в меню
    }

    // Методы - заглушки
    @Override
    public void present(float deltaTime) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void dispose() {}

}
