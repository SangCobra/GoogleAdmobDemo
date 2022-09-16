package com.example.googleadsmobtesting;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.googleadsmobtesting.databinding.ActMainBinding;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AdsActivity extends Activity {
    private AppOpenAdManager appOpenAdManager;
    private ActMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActMainBinding.inflate(LayoutInflater.from(this));

        setContentView(binding.getRoot());
        MobileAds.initialize(
                this,
                initializationStatus -> {});

        appOpenAdManager = new AppOpenAdManager();
        preLoadAppOpenAds();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        initEvents();
    }

    private void initEvents(){
        binding.appOpenAds.setOnClickListener(view ->
               appOpenAdManager.showAdIfAvailable(AdsActivity.this)
        );
    }

    private void preLoadAppOpenAds(){
        appOpenAdManager.loadAd(this);
    }

    private class AppOpenAdManager {
        private static final String LOG_TAG = "AppOpenAdManager";
        private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294";

        private AppOpenAd appOpenAd = null;


        /** Constructor. */
        public AppOpenAdManager() {
        }

        public void showAdIfAvailable(@NonNull final Activity activity){

            appOpenAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            loadAd(activity);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {

                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent();
                            Log.d(LOG_TAG, "Ad showed fullscreen content.");
                        }
                    }
            );

            appOpenAd.show(activity);
        }

        /** Request an ad. */
        private void loadAd(Context context) {

            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(
                    context, AD_UNIT_ID, request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    new AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            // Called when an app open ad has loaded.
                            Log.d(LOG_TAG, "Ad was loaded.");
                            appOpenAd = ad;

                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            // Called when an app open ad has failed to load.

                        }
                    });
        }

        /** Check if ad exists and can be shown. */
        private boolean isAdAvailable() {
            return appOpenAd != null;
        }
    }

    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }
}
