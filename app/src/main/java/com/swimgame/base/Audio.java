package com.swimgame.base;

// Работа со звуком

public interface Audio {
    public Music newMusic(String filename);      // Потоковый аудиофайл
    public Sound newSound(String filename);      // Короткий звуковой эффект
}