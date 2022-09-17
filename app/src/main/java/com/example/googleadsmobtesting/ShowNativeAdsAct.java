package com.example.googleadsmobtesting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.googleadsmobtesting.databinding.LayoutNativeAdsBinding;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MediaAspectRatio;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

import java.util.Objects;

public class ShowNativeAdsAct extends Activity {
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110";
    private static final String TAG = "ShowNativeAdsAct";

    private AdLoader adLoader;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_native_ad);

        showInterstitialAd();
        showNativeAdsBanner();
    }

    private void showInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        onInterstitialAdCreated();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });


    }

    private void onInterstitialAdCreated(){
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                mInterstitialAd = null;
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                mInterstitialAd = null;
            }

            @Override
            public void onAdImpression() {
            }

            @Override
            public void onAdShowedFullScreenContent() {
            }
        });

        if (mInterstitialAd != null) {
            mInterstitialAd.show(ShowNativeAdsAct.this);
        }
    }

    private void showNativeAdsBanner() {
        adLoader = new AdLoader.Builder(this, AD_UNIT_ID)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        // Show the ad.
                        LayoutNativeAdsBinding nativeAdsView = LayoutNativeAdsBinding.inflate(LayoutInflater.from(ShowNativeAdsAct.this));
                        populateNativeAdview(nativeAdsView, nativeAd);

                        FrameLayout container = findViewById(R.id.container);
                        container.removeAllViews();
                        container.addView(nativeAdsView.getRoot());

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

    private void populateNativeAdview(LayoutNativeAdsBinding binding, NativeAd nativeAd) {
        binding.adAppIcon.setImageDrawable(Objects.requireNonNull(nativeAd.getIcon()).getDrawable());
        binding.adHeadline.setText(nativeAd.getHeadline());
        binding.body.setText(nativeAd.getBody());
    }
}
