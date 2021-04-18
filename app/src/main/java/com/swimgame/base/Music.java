package com.swimgame.base;

// Музыкальный поток

public interface Music {
    public void play();                        // Воспроизведение музыкального потока
    public void stop();                        // Прекращение воспроизведения
    public void pause();                       // Приостановка воспроизведения
    public void setLooping(boolean looping);   // Циклическое воспроизведение
    public void setVolume(float volume);       // Установка громкости [0 - 1]
    public boolean isPlaying();                // Играет ли музыка
    public boolean isStopped();                // Остановлен ли муз. поток
    public boolean isLooping();                // Циклично ли воспроизведение
    public void dispose();                      // Освобождение памяти
}