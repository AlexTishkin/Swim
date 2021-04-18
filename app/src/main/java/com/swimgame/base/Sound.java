package com.swimgame.base;

// Короткий звуковой эффект, который мы можем хранить в памяти

public interface Sound {
    public void play(float volume);         // Воспроизведение звука
    public void dispose();                  // Освобождение памяти
}