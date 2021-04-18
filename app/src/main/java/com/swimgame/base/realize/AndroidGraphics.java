package com.swimgame.base.realize;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.swimgame.swim.Assets;
import com.swimgame.base.Graphics;
import com.swimgame.base.Pixmap;

import java.io.IOException;
import java.io.InputStream;

// Реализация графического модуля

public class AndroidGraphics implements Graphics {
    AssetManager assets;                                                    // Доступ к ресурсам
    Bitmap frameBuffer;                                                     // Искусственный фреймбуфер
    Canvas canvas;                                                          // Отрисовка искусственного фрейма
    Paint paint;                                                            // Для рисования
    Rect srcRect = new Rect();                                              // Для реализациии drawPixmap
    Rect dstRect = new Rect();                                              // нет созданию новых классов ! Один и только Один!!!
    Rect bounds = new Rect();                                               // Ограничение для текста

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;                                               // Получаем менеджер доступа к ресам
        this.frameBuffer = frameBuffer;                                     // Устанавливаем фреймбуфер
        this.canvas = new Canvas(frameBuffer);                              // Создаем канву на И. фреймбуфер
        this.paint = new Paint();                                           // Создаем paint
    }

    @Override
    public Pixmap newPixmap(String fileName, Graphics.PixmapFormat format) {// Загрузка Bitmap из ресурсов(используя Pixmap формат)
        Bitmap.Config config = null;                                        // Переводит Pixmap формат к одному из форматов Config
        if (format == Graphics.PixmapFormat.RGB565) config = Bitmap.Config.RGB_565;
        else if (format == Graphics.PixmapFormat.ARGB4444) config = Bitmap.Config.ARGB_4444;
        else config = Bitmap.Config.ARGB_8888;
        BitmapFactory.Options options = new BitmapFactory.Options();        // Создаем экземпляр опций
        options.inPreferredConfig = config;                                 // Устнавливаем предпочтительный формат цвета
        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in);                        // Загружаем Bitmap из ресурсов
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'"); // НЕТ КАРТИНКИ!!!!
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");     // НЕТ КАРТИНКИ!!!!
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        if (bitmap.getConfig() == Bitmap.Config.RGB_565)                    // Переводим Bitmap в Pixmapformat
            format = Graphics.PixmapFormat.RGB565;
        else if (bitmap.getConfig() == Bitmap.Config.ARGB_4444)
            format = Graphics.PixmapFormat.ARGB4444;
        else
            format = Graphics.PixmapFormat.ARGB8888;
        return new AndroidPixmap(bitmap, format);                           // Создание экза AndroidBitmap
    }

    @Override
    public Typeface newFont(String font) {
        return Typeface.createFromAsset(this.assets, font);
    }                              // Загрузка нового шрифта

    @Override
    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }                                      // Очистка нашего фреймбуфера

    @Override
    public void drawText(String text, int x, int y, int color) {
        paint.setTypeface(Assets.font);                                     // Установка нашего шрифта
        paint.setColor(color);                                              // Установка цвета
        paint.setTextSize(100);                                             // Установка размера шрифта
        paint.setTextAlign(Paint.Align.LEFT);                               // Выравнивание текста(<-)
        canvas.drawText(text, x, y, paint);                                 // Стандартный вызов канвы
    }       // Отрисовка текста[Координаты]


    @Override
    public void drawText(String text, int align, int color) {
        paint.setTypeface(Assets.font);                                     // Установка нашего шрифта
        paint.setColor(color);                                              // Установка цвета
        paint.setTextAlign(Paint.Align.LEFT);                               // Выравнивание текста(<-)

        if (align == 1)
        {
            paint.setTextSize(220);
            paint.getTextBounds(text, 0, text.length(), bounds);
            canvas.drawText(text, (canvas.getWidth() - bounds.width() - 10) / 2, (canvas.getHeight() + 10) / 3 + 30, paint);
        }else
        if (align == 3){ // CENTER MINI
            paint.setTextSize(80);
            paint.getTextBounds(text, 0, text.length(), bounds);
            canvas.drawText(text, (canvas.getWidth() - bounds.width())/2, bounds.height()+10, paint);
        } else

        if (align == 2) // RIGHT
        {
            paint.setTextSize(100);
            paint.getTextBounds(text, 0, text.length(), bounds);
            canvas.drawText(text, canvas.getWidth() - bounds.width()-20, bounds.height()+10, paint);
        }
    }// Отрисовка текста [Расположение] {ALIGN: 0 - LEFT, 1 - CENTER, 2 - RIGHT, 3 - CENTER MINI} Сделано только справа. Остальное пока не надо



    @Override
    public void drawPixel(int x, int y, int color) {
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }                   // Отрисовка пикселя в нашем буфере

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }    // Отрисовка линии в нашем буфере

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + width - 1, paint);
    }// Отрисовка залитого прямоугольника в буфере

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
                           int srcWidth, int srcHeight) {
        srcRect.left = srcX;                                                // Установка исходного srcRect(ОТКУДА)
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;

        dstRect.left = x;                                                   // Установка вывода dstRect (КУДА)
        dstRect.top = y;
        dstRect.right = x + srcWidth - 1;
        dstRect.bottom = y + srcHeight - 1;
        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect, null);
    }              // Отрисовка части Pixmap

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y) {
        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, x, y, null);    // Приведение, чтобы обойти AndroidPixmap
    }              // Отрисовка Pixmap целиком

    @Override
    public int getWidth() {                                                // Возвращаем ширину фреймбуфера
        return frameBuffer.getWidth();
    }               // Возврат ширины

    @Override
    public int getHeight() {                                               // Возвращаем высоту фреймбуфера
        return frameBuffer.getHeight();
    }
}