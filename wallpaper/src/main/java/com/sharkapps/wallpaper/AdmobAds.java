package com.sharkapps.wallpaper;


import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdmobAds {

    private static InterstitialAd mInterstitialAd;
    static Context context;

    public static void fullAd(Context c, String unit){
        context = c;
        if (!BuildConfig.DEBUG || !BuildConfig.FULL_VERSION) {
            try {
                mInterstitialAd = new InterstitialAd(c);

                mInterstitialAd.setAdUnitId(unit);
                AdRequest adRequest = new AdRequest.Builder()
                        .build();
                   mInterstitialAd.loadAd(adRequest);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void show(){
        if (!BuildConfig.DEBUG || !BuildConfig.FULL_VERSION) {
            if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                  }else{
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);

                    }
                });
            }
        }

    }
}
