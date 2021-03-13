package com.rutershok.phrases.utils;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.rutershok.phrases.model.Storage;
import com.rutershok.phrases.R;

public class Billing {

    private static BillingProcessor mBillingProcessor;

    public static void initialize(final Activity activity) {
        mBillingProcessor = new BillingProcessor(activity, Key.GOOGLE_PLAY_KEY, Key.MERCHANT_ID, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
                Snackbar.showText(activity, R.string.product_correctly_purchased);
                Storage.setPremium(activity, true);
            }

            @Override
            public void onPurchaseHistoryRestored() {
            }

            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
            }

            @Override
            public void onBillingInitialized() {
                //Check if user has premium version at the start of the program
                Storage.setPremium(activity, mBillingProcessor != null && mBillingProcessor.isPurchased(Key.PREMIUM_VERSION));
            }
        });
    }

    static void purchasePremium(final Activity activity) {
        if (mBillingProcessor != null && mBillingProcessor.isInitialized()) {
            if (Storage.getPremium(activity)) {
                Toast.makeText(activity, R.string.you_already_have_the_premium_version, Toast.LENGTH_LONG).show();
            } else {
                mBillingProcessor.purchase(activity, Key.PREMIUM_VERSION);
            }
        } else {
            Toast.makeText(activity, R.string.error_billing, Toast.LENGTH_LONG).show();
        }
    }
}