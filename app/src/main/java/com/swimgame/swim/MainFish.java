package com.swimgame.swim;

// Главный герой игры - рыбка ^_^

public class MainFish extends Fish {
    private static final int FRAME_TICK = 22;                       // Количество тиков для анимации

    public boolean isProtected;                                    // Защита Пузырек -> Один раз + Визуализация

    public MainFish(){
        super();
        frameTick = FRAME_TICK;                                     // Максимальное значеие тика[Смена при 0]
        this.line = 2;
        this.x = 150;
        isProtected = false;  // false // Не защищен по умолчанию
    }

    @Override
    public void update() {
        if (frameTick == 0) {
            if (frame != super.FRAME_COUNT - 1) frame ++;
            else frame = 0;

            frameTick = FRAME_TICK;
        }
        else frameTick--;
    }
}