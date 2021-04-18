package com.swimgame.base.realize;

import android.graphics.Bitmap;

import com.swimgame.base.Graphics;
import com.swimgame.base.Pixmap;

// Тип нашего изображения

public class AndroidPixmap implements Pixmap {
    Bitmap bitmap;                                                      // Экземпляр класса Bitmap
    Graphics.PixmapFormat format;                                       // Формат bitmap

    public AndroidPixmap(Bitmap bitmap, Graphics.PixmapFormat format) { // Инициализация
        this.bitmap = bitmap;
        this.format = format;
    }
    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }                 // Возвращение ширины bitmap
    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }               // Возвращение высоты bitmap
    @Override

    public Graphics.PixmapFormat getFormat() {
        return format;
    }        // Возвращение формата BitMap
    @Override

    public void dispose() {
        bitmap.recycle();
    }                        // Очистка данных
}
