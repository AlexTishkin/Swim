package com.swimgame.base.realize;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
    AndroidGame game;                                         // Хранение экземпляра игры
    Bitmap framebuffer;                                         // Хранение фреймбуфера
    Thread renderThread = null;                                 // Поток для рендера
    SurfaceHolder holder;
    volatile boolean running = false;

    public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer) {
        super(game);                                                // Конструктор базового класса
        this.game = game;
        this.framebuffer = framebuffer;
        this.holder = getHolder();
    }

    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }

    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();                                    // Начало итерации(в наносекундах)
        while (running) {
            if (!holder.getSurface().isValid())
                continue;
            float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;// Отслеживание дельты времени между кадрами
            startTime = System.nanoTime();                                    // Обновление времени итерации для дельты
            game.getCurrentScreen().update(deltaTime);                        // Обновление на основе дельты
            game.getCurrentScreen().present(deltaTime);                       // Представление на основе дельты
            Canvas canvas = holder.lockCanvas();                              // Удерживаем канву для SurfaceView
            canvas.getClipBounds(dstRect);                                    // Получаем треугольник, растянутый на весь Surface
            canvas.drawBitmap(framebuffer, null, dstRect, null);    // Отрисовка искусственного буфера(автомасштаб)
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        running = false;
        while(renderThread.isAlive()) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                // повтор
            }
        }
    }
}
