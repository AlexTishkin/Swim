package com.swimgame.swim;

import com.swimgame.base.Screen;
import com.swimgame.base.realize.AndroidGame;

// ���������� ����: ��������� ���������� ������

public class SwimGame extends AndroidGame {                 // ���������� ����� ����
    @Override
    public Screen getStartScreen() {                        // ��������� ���������� ������
        return new LoadingScreen(this);                     // = �������� �����������
    }
}
