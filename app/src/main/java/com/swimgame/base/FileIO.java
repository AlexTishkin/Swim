package com.swimgame.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*  Интерфейс: ФАЙЛОВЫЙ ВВОД/ВЫВОД    */

public interface FileIO {
    public InputStream readAsset(String fileName) throws IOException;  // Чтение файла ресурсов
    public InputStream readFile(String fileName) throws IOException;   // Чтение файла с SD
    public OutputStream writeFile(String fileName) throws IOException; // Запись в файл
}