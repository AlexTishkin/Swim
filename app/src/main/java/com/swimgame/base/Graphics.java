package com.swimgame.base;

/*
  Граф. модуль, выполняющий след. действия:
    1. Загрузка изображения с диска
    2. Загрузка шрифта с диска
    3. Хранение изображений в памяти для дальнейшей прорисовки
    4. Очистка фреймбуфера цветом для удаления содержимого его прошлого кадра
    5. Установка определенного цвета для определенного положения пикселя во фреймбуфере
    6. Прорисовка линий и прямоугольников во фреймбуфере
    7. Прорисовка ранее загруженных изображений во фреймбуфере(целиком и частично)
    8. Отрисовка картинок со смешиванием и без него
    9. Отрисовка текста
    10.Получение размеров фреймбуфера
*/

import android.graphics.Typeface;

public interface Graphics {
    public static enum PixmapFormat {                                      // Кодирование различных форматов
        ARGB8888, ARGB4444, RGB565
    }

    public Typeface newFont(String font);                                   // Загружает новый шрифт
    public Pixmap newPixmap(String fileName, PixmapFormat format);          // Загружает изображение(JPEG/PNG) -> В формат
    public void clear(int color);                                           // Очистка фреймбуфера цветом color
    public void drawPixel(int x, int y, int color);                         // Отрисовка пикселя
    public void drawLine(int x, int y, int x2, int y2, int color);          // Отрисовка линии
    public void drawRect(int x, int y, int width, int height, int color);   // Отрисовка прямоугольника
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, // Отрисовка Pixmap(Частично)
                           int srcWidth, int srcHeight);
    public void drawPixmap(Pixmap pixmap, int x, int y);                    // Отрисовка Pixmap(Целиком)
    public void drawText(String text, int x, int y, int color);             // Отрисовка текста [Координаты]
    public void drawText(String text, int align, int color);                // Отрисовка текста [Расположение]

    public int getWidth();                                                  // Возвращает ширину фреймбуфера(px)
    public int getHeight();                                                 // Возвращает высоту фреймбуфера(px)
}
