package com.swimgame.base.realize;

import android.content.res.AssetManager;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import android.os.Environment;

import com.swimgame.base.FileIO;

import java.io.IOException;
import java.io.File;

/*    Реализация интерфейса FileIO: файловый ввод/вывод    */

public class AndroidFileIO implements FileIO {
    AssetManager assets;
    String externalStoragePath;

    public AndroidFileIO(AssetManager assets) {
        this.assets = assets;                                                 // Сохраняем AssetManager
        this.externalStoragePath = Environment.getExternalStorageDirectory(). // Сохраняем путь к внешнему хранилищу
                getAbsolutePath() + File.separator;
    }

    @Override
    public InputStream readAsset(String fileName) throws IOException {        // Чтение файла ресурсов
        return assets.open(fileName);
    }

    @Override
    public InputStream readFile(String fileName) throws IOException {         // Чтение файла с SD
        return new FileInputStream(externalStoragePath + fileName);
    }

    @Override
    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(externalStoragePath + fileName);          // Запись в файл
    }
}
