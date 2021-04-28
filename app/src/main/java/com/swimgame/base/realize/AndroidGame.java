package com.swimgame.base.realize;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;

import com.swimgame.base.Advertisement;
import com.swimgame.base.Audio;
import com.swimgame.base.FileIO;
import com.swimgame.base.Game;
import com.swimgame.base.Graphics;
import com.swimgame.base.Input;
import com.swimgame.base.Screen;

public abstract class AndroidGame extends Activity implements Game { // Активити
    AndroidFastRenderView renderView;                   // Управление потоком основного цикла + отрисовка
    Graphics graphics;                                  // Доступ к графике
    Audio audio;                                        // Доступ к звуку
    Input input;                                        // Обработка поступающих событий от юзера
    Advertisement advertisement;                        // Работа с рекламой (AdMob)
    FileIO fileIO;                                      // Доступ к файловому вводу/выводу
    SharedPreferences storageIO;

    Screen screen;                                      // Экраны переходов
    PowerManager.WakeLock wakeLock;                     // Чтобы экран не гаснул

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                                 // Вызов метода базового класса onCreate
        requestWindowFeature(Window.FEATURE_NO_TITLE);                      // Полноэкранный режим
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        boolean isLandscape = getResources().getConfiguration().orientation // Определяем ориентацию устройства
                == Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape ? 960 : 640;                     // Устанавливаем размеры фреймбуфера {Изменить размер}
        int frameBufferHeight = isLandscape ? 640 : 960;                    // в зависимости от ориентации
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,          // СОЗДАЕМ ФРЕЙМБУФФЕР
                frameBufferHeight, Bitmap.Config.RGB_565);

        // Находим масштабы координат(для касаний)
        float scaleX = (float) frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();

        // инициализируем низкоуровневые модули
        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(getAssets());
        storageIO = getSharedPreferences("common", MODE_PRIVATE);

        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);
        advertisement = new AndroidAdvertisement(this);
        screen = getStartScreen();                                         // метод, реализованный игрой
        setContentView(renderView);                                        // Вывод потока осн. цикла(Отрисовка)
        PowerManager powerManager = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame"); // Чтобы не тух экран
    }

    @Override
    public void onResume() {
        super.onResume();                                                  // Вызываем метод родительского класса
        wakeLock.acquire();                                                // Получаем wakeLock
        screen.resume();                                                   // возобновляем игровое окно
        renderView.resume();                                               // возобновляем поток визуализации
    }

    @Override
    public void onPause() {
        super.onPause();                                                  // Вызываем метод родительского класса
        wakeLock.release();                                               // Высвобождаем wakeLock
        renderView.pause();                                               // Останавливаем поток визуализации
        screen.pause();                                                   // останавливаем игровое окно
        if (isFinishing())                                                // Если активность готовится к смерти
            screen.dispose();                                             // Зачищаем экран
    }

    @Override
    // возвращаем экземпляры классов(Screen)
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }
    @Override
    public SharedPreferences getStorageIO() {
        return storageIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public Advertisement getAdvertisement() {
        return advertisement;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");
        this.screen.pause();                                            // Остановка текущего screen
        this.screen.dispose();                                          // Удаление текущего screen
        screen.resume();                                                // Возобновляем новый screen
        screen.update(0);                                               // Обновляем с дельтой в 0
        this.screen = screen;                                           // Устанавливаем текущим
    }

    public Screen getCurrentScreen() {                                  // Возвращение активного Скрина
        return screen;
    }
}