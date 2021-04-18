package com.swimgame.base;

// Тип нашего изображения

public interface Pixmap {
    public int getWidth();                          // Возвращает ширину объекта(в пикселях)
    public int getHeight();                         // Возвращает высоту объекта(в пикселях)
    public Graphics.PixmapFormat getFormat();       // Возвращает формат для хранения в RAM
    public void dispose();                          // Освобождение памяти
}