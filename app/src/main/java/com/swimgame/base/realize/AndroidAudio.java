package com.swimgame.base.realize;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.swimgame.base.Audio;
import com.swimgame.base.Music;
import com.swimgame.base.Sound;

import java.io.IOException;

// Реализация интерфейса Audio //

public class AndroidAudio implements Audio {
    AssetManager assets;                                // Загрузка звуковых эффектов из файлов-ресурсов
    SoundPool soundPool;                                // Звуковые эффекты

    public AndroidAudio(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);       // Установка текущего аудиопотока(громкость)
        this.assets = activity.getAssets();                               // Получение менеджера доступа к ресурсам
        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0); // Создание менеджера доступа к звукам
    }

    @Override
    public Music newMusic(String filename) {
        try {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename); // Создание fd для MediaPlayer
            return new AndroidMusic(assetDescriptor);
        } catch (IOException e) {
            throw new RuntimeException("Невозможно загрузить музыку '" +  // Из-за моего косяка с музыкой
                    filename + "'");
        }
    }        // Создание нового экземпляра AndroidMusic

    @Override
    public Sound newSound(String filename) {
        try {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename); // Создание fd для SoundPool
            int soundId = soundPool.load(assetDescriptor, 0);              // Возвращение ID звука
            return new AndroidSound(soundPool, soundId);                   // Возвращение экземпляра
        } catch (IOException e) {
            throw new RuntimeException("Невозможно загрузить звук '" +    // Из-за моего косяка со звуками
                    filename + "'");
        }
    }        // Создание нового экземпляра AndroidSound
}
