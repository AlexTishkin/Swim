package com.swimgame.swim.gameobjects;

// ������ ���
public class WorldBackground {
    // ���������� �������� ����
    public int[] backgrounds_x;

    public WorldBackground() {
        backgrounds_x = new int[9];
        for (int i = 0; i < 9; i++)
            backgrounds_x[i] = 960 * i;
    }

    // �������� �������� �����������
    public void move() {
        for (int i = 0; i < 9; i++) {
            backgrounds_x[i]--; // �����

            if (backgrounds_x[i] <= -960)
                backgrounds_x[i] = 7680;
        }
    }
}
