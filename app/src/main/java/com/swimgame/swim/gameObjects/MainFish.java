package com.swimgame.swim.gameObjects;

// Главный герой игры - рыбка ^_^
public class MainFish extends Fish {
    private static final int FRAME_TICK = 22;   // Количество тиков для анимации

    public boolean isProtectedViaBubble;    // Защита Пузырек -> Один раз + Визуализация

    public MainFish() {
        super();
        frameTick = 0; // Максимальное значеие тика[Смена при FRAME_TICK]
        this.line = 2;
        this.x = 150;
        isProtectedViaBubble = false;
    }

    @Override
    public void update() {
        if (frameTick != FRAME_TICK) {
            frameTick++;
            return;
        }
        frame = frame + 1 != FRAME_COUNT ? frame + 1 : 0;
        frameTick = 0;
    }
}