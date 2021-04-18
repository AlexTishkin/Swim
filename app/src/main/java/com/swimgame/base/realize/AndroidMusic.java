package com.swimgame.base.realize;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;


import com.swimgame.base.Music;

import java.io.IOException;

/**  Реализация проигрывания потоковой музыки */

public class AndroidMusic implements Music, MediaPlayer.OnCompletionListener {
    MediaPlayer mediaPlayer;                        // Экземпляр класса, проигрывающего фон. музыку
    boolean isPrepared = false;                     // Состояние муз. потока MediaPlayer

    public AndroidMusic(AssetFileDescriptor assetDescriptor) {
        mediaPlayer = new MediaPlayer();                                    // Создание MediaPlayer
        try {
            mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(), // Инициализация данных для MediaPlayer
                    assetDescriptor.getStartOffset(),
                    assetDescriptor.getLength());
            mediaPlayer.prepare();                                         // Подготовка MediaPlayer
            isPrepared = true;                                             // Взводим флаг готовности
            mediaPlayer.setOnCompletionListener(this);                     // Регистрируем слушатель
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load music");             // Если что-то не так, то я ОСЁЛ(накосячил)
        }
    }

    @Override
    public void dispose() {                                                /////// ОСВОБОЖДЕНИЕ ПАМЯТИ ///////
        if (mediaPlayer.isPlaying())                                       // Проверка, работает ли MediaPlayer
            mediaPlayer.stop();                                            // Если да, то остановить нахрен
        mediaPlayer.release();                                             // Зачистить музяку
    }

    @Override
    public boolean isPlaying() {                                           // Играет ли музыка
        return mediaPlayer.isPlaying();                                    // Метод плеера
    }

    @Override
    public boolean isStopped() {                                           // Остановлен ли муз. поток
        return !isPrepared;                                                // Точная инфа по нашему флагу
    }

    @Override
    public boolean isLooping() {                                          // Циклично ли воспроизведение
        return mediaPlayer.isLooping();                                   // Метод плеера
    }

    @Override
    public void play() {                                                   // Воспроизведение муз. потока
        if (mediaPlayer.isPlaying())
            return;                                                        // Если воспроизводится, выходим
        try {
            synchronized (this) {                                          // Синхронизируем -> возможно изменение isPrepared
                if (!isPrepared)
                    mediaPlayer.prepare();                                 // Если требуется, готовим плеер
                mediaPlayer.start();                                       // Запускаем музыку
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setLooping(boolean isLooping) {                            // Установка цикличного воспроизведения
        mediaPlayer.setLooping(isLooping);                                 // Делегат метода mediaPlayer
    }

    @Override
    public void setVolume(float volume) {                                 // Установка громкости
        mediaPlayer.setVolume(volume, volume);                            // Делегат метода mediaPlayer
    }

    @Override
    public void stop() {                                                  // Остановка муз. потока
        mediaPlayer.stop();                                               // Остановка плеера
        synchronized (this) {                                             // Синхронизируем(другие потоки могут изменить флаг)
            isPrepared = false;                                           // Изменяем значение флага
        }
    }

    @Override
    public void pause() {                                                // Пауза
        mediaPlayer.pause();                                             // Остановка плеера
        synchronized (this) {                                            // Синхронизируем(другие потоки могут изменить флаг)
            isPrepared = false;                                          // Изменяем значение флага
        }
    }

    @Override
    public void onCompletion(MediaPlayer player) {                        // Убираем флаг
        synchronized (this) {
            isPrepared = false;
        }
    }
}
