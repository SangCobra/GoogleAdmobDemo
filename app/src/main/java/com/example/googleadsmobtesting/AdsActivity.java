package com.example.googleadsmobtesting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.googleadsmobtesting.databinding.ActMainBinding;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdsActivity extends Activity {
    private static final String TAG = "AdsActivity";
    private AppOpenAdManager appOpenAdManager;
    private ActMainBinding binding;
    private RewardedAd mRewardedAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        initAdsMob();

        appOpenAdManager = new AppOpenAdManager();
        showAppOpenAds();
        
        showAdBanner();

        loadAdReward();
        
        initEvent();

        
    }

    private void loadAdReward() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        onAdRewardLoaded();
                    }
                });
    }

    private void onAdRewardLoaded(){
        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {

            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.");
                Toast.makeText(AdsActivity.this, adError.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.");
            }
        });
    }

    private void initAdsMob() {
        MobileAds.initialize(
                this,
                initializationStatus -> {
                });
    }

    private void showAdBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }

    private void initEvent() {
        binding.nativeAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdsActivity.this, ShowNativeAdsAct.class));
            }
        });

        binding.reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRewardedAd != null) {
                    Activity activityContext = AdsActivity.this;
                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            Log.d(TAG, "The user earned the reward.");
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                            loadAdReward();
                        }

                    });
                } else {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }
            }
        });
    }


    private void showAppOpenAds() {
        appOpenAdManager.loadAd(this);
    }

    private class AppOpenAdManager {
        private static final String LOG_TAG = "AppOpenAdManager";
        private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294";

        private AppOpenAd appOpenAd = null;


        /**
         * Constructor.
         */
        public AppOpenAdManager() {
        }

        public void showAdIfAvailable(@NonNull final Activity activity) {

            appOpenAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {

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

        /**
         * Request an ad.
         */
        private void loadAd(Activity activity) {

            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(
                    activity, AD_UNIT_ID, request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    new AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            // Called when an app open ad has loaded.
                            Log.d(LOG_TAG, "Ad was loaded.");
                            appOpenAd = ad;

                            showAdIfAvailable(activity);
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            // Called when an app open ad has failed to load.

                        }
                    });
        }

    }

}
