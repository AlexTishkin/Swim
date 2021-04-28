package com.swimgame.base.realize;

import android.app.Activity;
import android.util.Log;
//import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.swimgame.base.Advertisement;

public class AndroidAdvertisement implements Advertisement {

    // Межстраничный рекламный блок
    private static final String TAG = "Advertisement";
    // test
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";
    //    private static final String AD_UNIT_ID = "ca-app-pub-5118329113135673/5592942496";
    private InterstitialAd interstitialAd;

    private Activity activity;

    // Показывать рекламу раз в 3 поражения
    private int looseCount = 0;
    private static final int maxLoosesForAdvert = 3;

    public AndroidAdvertisement(Activity activity) {
        this.activity = activity;
        initialize(activity);
        loadAd();
    }

    public boolean hasInterstitialAd() {
        return interstitialAd != null;
    }

    // Initialize the Mobile Ads SDK.
    private void initialize(Activity activity) {
        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    @Override
    public void loadAd() {
        if (interstitialAd != null) return;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                activity,
                AD_UNIT_ID,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until an ad is loaded.
                        AndroidAdvertisement.this.interstitialAd = interstitialAd;

                        Log.i(TAG, "onAdLoaded");
//                        Toast.makeText(activity, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't show it a second time.
                                        AndroidAdvertisement.this.interstitialAd = null;
                                        loadAd();
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        AndroidAdvertisement.this.interstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        interstitialAd = null;

                        // String error = String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                        // Toast.makeText(activity, "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    // Show the ad if it's ready. Otherwise toast and restart the game.
    public void showInterstitial() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (interstitialAd != null)
                    interstitialAd.show(activity);
                // else {
                //     Toast.makeText(activity, "Ad did not load", Toast.LENGTH_SHORT).show();
                // }
            }
        });
    }

    @Override
    public boolean canShowAdvert() {
        looseCount++;
        if (looseCount == maxLoosesForAdvert) {
            looseCount = 0;
            return true;
        }
        return false;
    }

}
