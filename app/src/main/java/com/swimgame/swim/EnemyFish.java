package com.swimgame.swim;

// Вражеские рыбки [Простая и Акула]

import java.util.Random;

public class EnemyFish extends Fish {
    public static final int TYPE_ENEMY1 = 0;  // Простая рыбка
    public static final int TYPE_ENEMY2 = 1;  // Акула -> 2 клетки

    public static final int COLOR_RED   = 100; // Цвет рыбки -> Для красоты
    public static final int COLOR_GREEN = 101;
    public static final int COLOR_BLUE  = 102;

    public static final int SPEED_SLOW   = 44;  // Медленная рыбка
    public static final int SPEED_MEDIUM = 22;  // Средняя рыбка
    public static final int SPEED_FAST   = 11;  // Быстрая рыбка

    public boolean isTurned;                    // Красная рыба -> Делала повотор или нет

    public boolean isShark;                     // Есть возможность передислоцироваться в Акулу
    public boolean isVisible;                    // Видима, для пузрька

    public int type;                      // Тип рыбки
    public int color;                      // цвет рыбки
    public int speed;                     // Скорость рыбки
    public int tick;                      // Тики для движения врагов

    public static Random random = new Random();

    public EnemyFish(int x){
        super();
        create(x);
        color = random.nextInt(2) + 101; // Случайный цвет Зеленый ИЛИ Синий
        isShark = false;
        isVisible = true;
        isTurned = false;

    } // Создание рыбки в координатах x

    public void create(int x){
        //if(random.nextInt(2) != 1) type = 0; else type = 1;
        if(random.nextInt(5) != 4) type = 0; else type = 1;
        this.line = 2; //random.nextInt(5) + 1;
        speed = SPEED_MEDIUM;                                //random.nextInt(3)+10;  // Равновероятно ИЗМЕНИТЬ!!!!

        frameTick = speed;

        if(x == 0) this.x = 1200; else this.x = x;
    } // Добавление новой рыбки -> после ухода за границу [if x = 0 -> void, else x]

    @Override
    public void update() {
        if (frameTick == 0) {
            if (frame != super.FRAME_COUNT - 1) frame ++; // Анимация
            else frame = 0;

            frameTick = speed;
        }
        else frameTick--;


    }  // Обновление анимации и движения

    public void move(){
        this.x -= 2;                   // Движение
        if (color == COLOR_RED && type == TYPE_ENEMY1 &&random.nextInt(150) == 0 && !isTurned && x > 600 && x < 800){
            if (line == 1 || line == 4) line ++; else
            if (line == 3 || line == 5) line --; else{
                boolean whereTurn = false;
                if (random.nextInt(2) == 0) whereTurn = true;
                if (whereTurn) line ++; else line --;
            }
            isTurned = true;
        }

    }

}
