package com.example.googleadsmobtesting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.googleadsmobtesting.databinding.ActNativeAdBinding;
import com.example.googleadsmobtesting.databinding.LayoutNativeAdsBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

public class ShowNativeAdsAct extends Activity {
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110";
    private static final String TAG = "ShowNativeAdsAct";

    private AdLoader adLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_native_ad);

        adLoader = new AdLoader.Builder(this, AD_UNIT_ID)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        // Show the ad.
                        NativeAdView nativeAdsView = (NativeAdView) getLayoutInflater().inflate(R.layout.layout_native_ads, null);

                        LinearLayout container = findViewById(R.id.container);
                        container.removeAllViews();
                        container.addView(nativeAdsView);

                        if (isDestroyed()) {
                            nativeAd.destroy();
                            return;
                        }
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                        Log.d(TAG, "onAdFailedToLoad: " + adError.getCause());
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder().build())
                .build();
        adLoader.loadAds(new AdRequest.Builder().build(), 3);
    }
}
