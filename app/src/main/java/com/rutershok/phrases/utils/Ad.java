package com.rutershok.phrases.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.rutershok.phrases.model.Storage;
import com.rutershok.phrases.R;

public class Ad {

    private static InterstitialAd mInterstitialAd;
    private static RewardedVideoAd mRewardedVideoAd;
    private static AdRequest mAdRequest;

    private static void getConsentStatus(final Activity activity) {
        if (!activity.isFinishing()) {
            ConsentInformation.getInstance(activity).requestConsentInfoUpdate(new String[]{Key.ADMOB_PUB_ID}, new ConsentInfoUpdateListener() {
                @Override
                public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                    // User's consent status successfully updated.
                    switch (consentStatus) {
                        case UNKNOWN:
                            Dialog.gdprConsent(activity);
                            break;
                        case NON_PERSONALIZED:
                            ConsentInformation.getInstance(activity).setConsentStatus(ConsentStatus.NON_PERSONALIZED);
                            break;
                        case PERSONALIZED:
                            ConsentInformation.getInstance(activity).setConsentStatus(ConsentStatus.PERSONALIZED);
                            break;
                    }
                }

                @Override
                public void onFailedToUpdateConsentInfo(String errorDescription) {
                    // User's consent status failed to update.
                }
            });
        }
    }

    public static void initialize(final Activity activity) {
        if (!Storage.getPremium(activity)) {
            getConsentStatus(activity);
            MobileAds.initialize(activity,
                    activity.getString(R.string.admob_app_id));

            mAdRequest = new AdRequest.Builder().build();
            mInterstitialAd = new InterstitialAd(activity);
            mInterstitialAd.setAdUnitId(Key.ADMOB_INTERSTITIAL_ID);
            mInterstitialAd.loadAd(mAdRequest);
            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
        }
    }

    public static void showInterstitialWithFrequency(Activity activity) {
        if (!Storage.getPremium(activity) &&
                Storage.getInterstitialCount(activity) % Key.INTERSTITIAL_FREQUENCY == 0) {
            Snackbar.showText(activity, R.string.loading_ads);
            showInterstitial(activity);
        }
        Storage.updateInterstitialCount(activity);
    }

    static void showInterstitial(Context context) {
        if (!Storage.getPremium(context) && mInterstitialAd != null) {
            try {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            mInterstitialAd.loadAd(mAdRequest);
                        }
                    });
                } else {
                    mInterstitialAd.loadAd(mAdRequest);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mInterstitialAd = new InterstitialAd(context);
                mInterstitialAd.setAdUnitId(Key.ADMOB_INTERSTITIAL_ID);
                mInterstitialAd.loadAd(mAdRequest);
            }
        }
    }

    public static void showBanner(final Activity activity) {
        if (!Storage.getPremium(activity)) {
            final AdView bannerView = activity.findViewById(R.id.ads_banner);
            bannerView.loadAd(mAdRequest);
            bannerView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    bannerView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    static void unlockPremium(final Activity activity) {
        mRewardedVideoAd.loadAd(Key.ADMOB_REWARDED_VIDEO_ID, mAdRequest);
        Snackbar.showText(activity, R.string.loading);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                mRewardedVideoAd.show();
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Storage.setPremium(activity, true);
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
    }
}