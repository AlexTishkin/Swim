package com.swimgame.swim;

import com.swimgame.base.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

// Настройки: звук, музыка, рекорды

public class Settings {
    public static boolean soundEnabled  = true;                           // Звук/Музыка(default = on)
    public static boolean controlEnabled = true;                          // Управление (default - buttons)
    public static int[] highscores = new int[]{0, 0, 0};                  // 3 рекорда (default = 0)
    private static final String FILE_SETTINGS = "swim.sw";                // Файл, хранящий рекорды и установки

    public static void load(FileIO files) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(files.readFile(FILE_SETTINGS)));  // Открываем входящий поток из файла
            controlEnabled = Boolean.parseBoolean(in.readLine());                           // Считываем значение для управления
            soundEnabled = Boolean.parseBoolean(in.readLine());                             // Считываем значение для музыки
            for (int i = 0; i < 3; i++) highscores[i] = Integer.parseInt(in.readLine());    // Считываем рекорды
        } catch (IOException e) {                                                           // Игнорим ошибки
        } catch (NumberFormatException e) {                                                 // Если неправильный формат файла, то закрываем его
        } finally { try { if (in != null) in.close(); } catch (IOException e) { } }
    }                           // Загрузка настроек из файла

    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(FILE_SETTINGS)));// Открываем исходящий поток в файл
            out.write(Boolean.toString(controlEnabled) + System.lineSeparator());            // Записываем значение для музыки
            out.write(Boolean.toString(soundEnabled) + System.lineSeparator());              // Записываем значение для музыки
            for (int i = 0; i < 3; i++)                                                      // Записываем рекорды
                out.write(Integer.toString(highscores[i]) + System.lineSeparator());

        } catch (IOException e) {                                                            // Игнорим ошибки
        } finally { try {  if (out != null) out.close(); } catch (IOException e){} }         // Если файл открыт, то закрываем его
    }                           // Сохранение настроек в файл

    public static void addScore(int score) {                              // score - новый рекорд
        for (int i = 0; i < 3; i++) {                                     // Простенькая сортировка
            if (highscores[i] < score) {
                for (int j = 2; j > i; j--)
                    highscores[j] = highscores[j - 1];
                highscores[i] = score;
                break;
            }
        }
    }                          // Добавление нового рекорда(+ сортировка)
}


