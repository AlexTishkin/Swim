package com.swimgame.base.realize;

import java.util.ArrayList;
import java.util.List;

/*
  ФИЧА ПРОТИВ СБОРЩИКА МУСОРА -_-
    getTouchEvents и getKeyEvents возвращают списки, поэтому постоянно будет создавать экземпляры этих классов
    // Хранить их Dalvic будет во внутренних списках обработчика и время от времени будет чистить, замедляя систему
    ПУЛИНГ ЭКЗЕМПЛЯРОВ: Вместо создания новых экземпляров будет обращаться к уже созданным
 */

public class Pool<T> {
    public interface PoolObjectFactory<T> {                 // Фабрика элементов
        public T createObject();
    }

    private final List<T> freeObjects;                     // Хранение объектов пула
    private final PoolObjectFactory<T> factory;            // Генерация новых экземпляров
    private final int maxSize;                             // Максимальное количество элементов

    public Pool(PoolObjectFactory<T> factory, int maxSize) { // Фабрика + макс. кол-во объектов
        this.factory = factory;                              // Сохраняем фабрику
        this.maxSize = maxSize;                              // Сохраняем максимум
        this.freeObjects = new ArrayList<T>(maxSize);        // Инициализируем новый ArrayList
    }

    public T newObject() {                                   // (Передача | Возврат) нового экземпляра класса
        T object = null;                                     // Если нет, создать, иначе вернуть из листа
        if (freeObjects.size() == 0) object = factory.createObject();
        else object = freeObjects.remove(freeObjects.size() - 1);
        return object;
    }

    public void free(T object) {                            // Вставлять объект, если только не заполнен лист
        if (freeObjects.size() < maxSize)
            freeObjects.add(object);
    }
}

/*
Данный класс удобно применять во многих ситуациях.
Если вы многократно используете объекты:  Если они из класса Pool,
то их повторная инициализация, возможно, получится неполной, а этого допускать нельзя.
*/