package com.swimgame.swim;

import android.content.SharedPreferences;

// Настройки: звук, музыка, рекорды
public class Settings {
    public static boolean soundEnabled = true;              // Звук/Музыка(default = on)
    public static boolean controlEnabled = true;            // Управление (default - buttons)
    public static int[] highscores = new int[]{0, 0, 0};    // 3 рекорда (default = 0)

    public static void load(SharedPreferences storageIO) {
        soundEnabled = storageIO.getBoolean("soundEnabled", true);
        controlEnabled = storageIO.getBoolean("controlEnabled", true);

        for (int i = 0; i < 3; i++)
            highscores[i] = storageIO.getInt("highscore" + i, 0);
    }

    public static void save(SharedPreferences storageIO) {
        SharedPreferences.Editor storageEditor = storageIO.edit();
        storageEditor.putBoolean("soundEnabled", soundEnabled);
        storageEditor.putBoolean("controlEnabled", controlEnabled);

        for (int i = 0; i < 3; i++)
            storageEditor.putInt("highscore" + i, highscores[i]);

        storageEditor.apply();
    }

    // Добавление нового рекорда(+ сортировка)
    public static void addScore(int score) {                              // score - новый рекорд
        for (int i = 0; i < 3; i++) {                                     // Простенькая сортировка
            if (highscores[i] < score) {
                for (int j = 2; j > i; j--)
                    highscores[j] = highscores[j - 1];
                highscores[i] = score;
                break;
            }
        }
    }
}


