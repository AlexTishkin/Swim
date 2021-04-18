package com.swimgame.base;

/*    Реализация наших экранов    */

public abstract class Screen {
    protected final Game game;      // Экземпляр игры для доступа к низкоуровневым модулям Game
    // Установка нового текущего экрана при помощи Game.setScreen(Screen)

    public Screen(Game game) {      // Конструктор сохраняет game, доступный всем подклассам
        this.game = game;
    }

    public abstract void update(float deltaTime);   // Обновление состояния экрана
    public abstract void present(float deltaTime);  // Представление экрана пользователю
    public abstract void pause();                   // Постановка игры на паузу
    public abstract void resume();                  // Возврат с паузы
    public abstract void dispose();                 // Освобождение ресурсов & Сохранение инфы

}