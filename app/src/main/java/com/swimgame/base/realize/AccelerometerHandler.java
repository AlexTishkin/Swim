package com.swimgame.base.realize;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

// Выполнение обработки, связанной с акселерометром
// НЕ СИНХРОНИЗИРУЕМ - ОПЕРАЦИИ СЧИТЫВАНИЯ АТОМАРНЫ =)

public class AccelerometerHandler implements SensorEventListener {
    float accelX;                                                   // Ускорение по оси X
    float accelY;                                                   // Ускорение по оси Y
    float accelZ;                                                   // Ускорение по оси Z

    public AccelerometerHandler(Context context) {
        SensorManager manager = (SensorManager) context             // Менеджер доступа к датчику
                .getSystemService(Context.SENSOR_SERVICE);
        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
            Sensor accelerometer = manager.getSensorList(
                    Sensor.TYPE_ACCELEROMETER).get(0);
            manager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Здесь ничего не делаем
    } // Балласт

    @Override
    public void onSensorChanged(SensorEvent event) {                 // Прослушивание датчика акселерометра
        accelX = event.values[0];
        accelY = event.values[1];
        accelZ = event.values[2];
    }

    // Геттеры ускорений по 3-ем координатам //
    public float getAccelX() {
        return accelX;
    }
    public float getAccelY() {
        return accelY;
    }
    public float getAccelZ() {
        return accelZ;
    }
}