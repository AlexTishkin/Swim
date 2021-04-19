package com.swimgame.swim.gameobjects;

// Задний фон
public class WorldBackground {
    // Координаты картинок фона
    public int[] backgrounds_x;

    public WorldBackground() {
        backgrounds_x = new int[9];
        for (int i = 0; i < 9; i++)
            backgrounds_x[i] = 960 * i;
    }

    // Движение фонового изображения
    public void move() {
        for (int i = 0; i < 9; i++) {
            backgrounds_x[i]--; // Сдвиг

            if (backgrounds_x[i] <= -960)
                backgrounds_x[i] = 7680;
        }
    }
}
