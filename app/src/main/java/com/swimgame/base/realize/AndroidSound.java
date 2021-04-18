package com.swimgame.base.realize;

import android.media.SoundPool;

import com.swimgame.base.Sound;

// Реализация интерфейса звукового эффекта

public class AndroidSound implements Sound {
    int soundId;                                                        // Сохранение ID звука
    SoundPool soundPool;                                                // Менеджер звуков

    public AndroidSound(SoundPool soundPool, int soundId) {
        this.soundId = soundId;                                         // Сохранение ID звука
        this.soundPool = soundPool;                                     // Сохранения менеджера звуков
    }

    @Override
    public void play(float volume) {                                    // Воспроизведение звукового эффекта
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    @Override
    public void dispose() {                                             // Освобождение ресурсов
        soundPool.unload(soundId);
    }
}