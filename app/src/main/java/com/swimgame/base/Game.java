package com.swimgame.base;

/* Интерфейс, реализующий основные компоненты игры
Доступ к низкоуровневым модулям:
  1. Воспроизведение звука,
  2. Прорисовка экрана
  3. получения реакции пользователя
  4. а также чтения и записи файлов;
*/

public interface Game {
    public Input getInput();                    // Доступ к вх. потоку: получение реакции юзера
    public FileIO getFileIO();                  // Доступ к ФС: чтение и запись файлов
    public Graphics getGraphics();              // Доступ к Графике: прорисовка экрана
    public Audio getAudio();                    // Доступ к Звуку: воспроизведение звука
    public Advertisement getAdvertisement();    // Работа с рекламой (admob)

    public void setScreen(Screen screen);  // Установка текущего экрана для игры
    public Screen getCurrentScreen();      // Возвращает текущий активный экран
    public Screen getStartScreen();        // Установка начального экрана
}

