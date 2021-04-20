package com.swimgame.base;

import com.google.android.gms.ads.interstitial.InterstitialAd;

// Менеджер для работы с рекламой
public interface Advertisement {

    // Имеется межстраничная реклама
    boolean hasInterstitialAd();

    // Загрузить рекламу
    void loadAd();

    // Показать рекламу
    void showInterstitial();
}