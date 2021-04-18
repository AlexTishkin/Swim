package com.swimgame.swim;

import com.swimgame.base.Screen;
import com.swimgame.base.realize.AndroidGame;
import com.swimgame.swim.screens.LoadingScreen;

// Реализация игры: установка стартового экрана

public class SwimGame extends AndroidGame {                 // Реализация нашей игры
    @Override
    public Screen getStartScreen() {                        // Установка стартового экрана
        return new LoadingScreen(this);                     // = Загрузка компонентов
    }
}
