package com.swimgame.swim;

import android.graphics.Typeface;

import com.swimgame.base.Music;
import com.swimgame.base.Pixmap;
import com.swimgame.base.Sound;

// Менеджер доступа к ресурсам: изображения/звуки/шрифты

public class Assets {
    public static Pixmap background;                            // Фон
    public static Pixmap[] backgrounds;                        // Фоновые изображения

    public static Pixmap fon;                                   // Фон
    public static Pixmap logo;                                  // Логотип
    public static Pixmap buttons;                               // Кнопки
    public static Pixmap help_button;                           // Кнопка: помощь
    public static Pixmap play_button;                           // Кнопка: начать игру
    public static Pixmap pause_button;                          // Кнопка паузы в игре
    public static Pixmap ok_button;                             // Кнопка: ок
    public static Pixmap ex_button;                             // Кнопка: выход
    public static Pixmap repeat_button;                         // Кнопка: начать еще раз
    public static Pixmap back_button;                           // Кнопка: Назад
    public static Pixmap records_button;                        // Кнопка: рекорды
    public static Pixmap help1;                                 // Экран помощи 1
    public static Pixmap help2;                                 // Экран помощи 2
    public static Pixmap highscores_label;                      // Метка: Рекорды
    public static Pixmap highscores3;                           // Значки рекордов(123)

    public static Pixmap mini_sound_button;                     // Кнопка: музыка on/off в игре

    public static Pixmap ready;                                 // Игра-состояние(1): Готовность
    public static Pixmap pause;                                 // Игра-состояние(3): Пауза
    public static Pixmap gameOver;                              // Игра-состояние(4): Конец игры

    public static Sound click;                                  // Звук клика
    public static Music menu_media;                             // Музыка: меню
    public static Music game_media;                             // Музыка:игра
    public static Typeface font;                                // Шрифт

    public static Pixmap up_button;                             // Игровая кнопка: вверх
    public static Pixmap up_click1_button;                      // Игровая кнопка: вверх - нажатие
    public static Pixmap up_click2_button;                      // Игровая кнопка: вверх - нажатие
    public static Pixmap down_button;                           // Игровая кнопка: вниз
    public static Pixmap down_click1_button;                    // Игровая кнопка: вниз  - нажатие
    public static Pixmap down_click2_button;                    // Игровая кнопка: вниз  - нажатие

    public static Pixmap coin;                                  // Игровая валюта: золотая монетка
    public static Pixmap coin2;                                 // Игровая валюта: зеленая монетка
    public static Pixmap coin3;                                 // Игровая валюта: красная монетка

    public static Pixmap protectBubble;                        // Защищающий пузырек

    public static Pixmap[] person;                              // Главный герой - рыбка[Анимация] 6 спрайтов

    public static Pixmap[] enemyRed;                            // Вражеская рыбка lite -> Красная
    public static Pixmap[] enemyGreen;                          // Вражеская рыбка lite -> Зеленая
    public static Pixmap[] enemyBlue;                           // Вражеская рыбка lite -> Синяя


    public static Pixmap[] enemy2;                              // Вражеская рыбка hard

}
